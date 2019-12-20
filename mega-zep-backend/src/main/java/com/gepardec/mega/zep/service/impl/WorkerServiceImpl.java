package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.monthlyreport.ProjectTimeManager;
import com.gepardec.mega.monthlyreport.warning.WarningConfig;
import com.gepardec.mega.security.AuthorizationInterceptor;
import com.gepardec.mega.utils.DateUtils;
import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.*;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.LocalDate;
import java.util.*;

import static com.gepardec.mega.utils.DateUtils.getFirstDayOfFollowingMonth;
import static com.gepardec.mega.utils.DateUtils.getLastDayOfFollowingMonth;
import static com.gepardec.mega.zep.service.ZepStatusCodeMapper.toHttpResponseCode;
import static java.lang.String.format;

@Interceptors(AuthorizationInterceptor.class)
@RequestScoped
public class WorkerServiceImpl implements WorkerService {

    @Inject
    Logger logger;

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    RequestHeaderType requestHeaderType;

    @Inject
    WarningConfig warningConfig;

    private static final ReadMitarbeiterRequestType readMitarbeiterRequestType = new ReadMitarbeiterRequestType();
    private static final ReadProjektzeitenRequestType projektzeitenRequest = new ReadProjektzeitenRequestType();

    @PostConstruct
    void init() {
        readMitarbeiterRequestType.setRequestHeader(requestHeaderType);
        projektzeitenRequest.setRequestHeader(requestHeaderType);
    }


    @Override
    public MitarbeiterType getEmployee(final GoogleUser user) {

        try {
            final List<MitarbeiterType> employees = flatMap(zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType));
            return employees.stream()
                    .filter(e -> e.getEmail() != null && e.getEmail().equals(user.getEmail()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            logger.error(format("error getEmployee for user: %s", user.getId()));
            return null;
        }
    }

    @Override
    public List<MitarbeiterType> getAllActiveEmployees() {
        ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType);
        return filterActiveEmployees(rmrt);
    }

    @Override
    public Integer updateEmployees(final List<MitarbeiterType> employees) {
        final List<Integer> statusCodeList = new LinkedList<>();

        employees.forEach(e -> statusCodeList.add(updateEmployee(e)));

        return statusCodeList.stream()
                .filter(statuscode -> statuscode == HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .findAny()
                .orElse(HttpStatus.SC_OK);
    }

    @Override
    public MonthlyReport getMonthendReportForUser(GoogleUser user) {
        MitarbeiterType employee = getEmployee(user);
        if (employee == null) {
            return null;
        }
        ReadProjektzeitenSearchCriteriaType searchCriteria = createProjectTimeSearchCriteria(employee);
        projektzeitenRequest.setReadProjektzeitenSearchCriteria(searchCriteria);

        ReadProjektzeitenResponseType projectTimeResponse = zepSoapPortType.readProjektzeiten(projektzeitenRequest);

        return calcWarnings(projectTimeResponse, employee);

    }

    private static ReadProjektzeitenSearchCriteriaType createProjectTimeSearchCriteria(MitarbeiterType employee) {
        ReadProjektzeitenSearchCriteriaType searchCriteria = new ReadProjektzeitenSearchCriteriaType();

        String releaseDate = employee.getFreigabedatum();
        searchCriteria.setVon(getFirstDayOfFollowingMonth(releaseDate));
        searchCriteria.setBis(getLastDayOfFollowingMonth(releaseDate));

        UserIdListeType userIdListType = new UserIdListeType();
        userIdListType.getUserId().add(employee.getUserId());
        searchCriteria.setUserIdListe(userIdListType);
        return searchCriteria;
    }


    private MonthlyReport calcWarnings(ReadProjektzeitenResponseType projectTimeResponse, MitarbeiterType employee) {
        if (projectTimeResponse == null || projectTimeResponse.getProjektzeitListe() == null) {
            return null;
        }
        MonthlyReport monthlyReport = new MonthlyReport(employee,
                new ProjectTimeManager(projectTimeResponse.getProjektzeitListe().getProjektzeiten()), warningConfig);
        monthlyReport.calculateWarnings();
        return monthlyReport;
    }


    @Override
    public Integer updateEmployee(final MitarbeiterType employee) {
        try {
            final UpdateMitarbeiterRequestType umrt = new UpdateMitarbeiterRequestType();
            umrt.setRequestHeader(requestHeaderType);
            umrt.setMitarbeiter(employee);

            final UpdateMitarbeiterResponseType updateMitarbeiterResponseType = zepSoapPortType.updateMitarbeiter(umrt);
            final ResponseHeaderType responseHeaderType = updateMitarbeiterResponseType != null ? updateMitarbeiterResponseType.getResponseHeader() : null;

            return toHttpResponseCode(responseHeaderType);
        } catch (Exception e) {
            logger.error(format("Errro updatingEmployee, id: %s", employee.getUserId()));
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    private List<MitarbeiterType> filterActiveEmployees(ReadMitarbeiterResponseType readMitarbeiterResponseType) {
        final List<MitarbeiterType> activeEmployees = new ArrayList<>();
        final List<MitarbeiterType> allEmployees = flatMap(readMitarbeiterResponseType);

        allEmployees.forEach(employee -> {
            final BeschaeftigungszeitListeType beschaeftigungszeitListeType = employee.getBeschaeftigungszeitListe();
            final List<BeschaeftigungszeitType> beschaeftigungszeitTypeList = beschaeftigungszeitListeType.getBeschaeftigungszeit();
            final BeschaeftigungszeitType last = beschaeftigungszeitTypeList
                    .stream()
                    .sorted(Comparator.comparing(BeschaeftigungszeitType::getStartdatum))
                    .reduce((first, second) -> second)
                    .orElse(null);

            if (last != null) {
                // if enddatum (sic!) is null => employee is active
                if (last.getEnddatum() == null) {
                    activeEmployees.add(employee);
                } else {
                    final LocalDate endDate = DateUtils.toLocalDate(Objects.requireNonNull(last).getEnddatum());
                    if (!endDate.isBefore(LocalDate.now())) {
                        activeEmployees.add(employee);
                    }
                }
            }
        });

        return activeEmployees;
    }

    private List<MitarbeiterType> flatMap(final ReadMitarbeiterResponseType readMitarbeiterResponseType) {
        final MitarbeiterListeType mitarbeiterListeType = readMitarbeiterResponseType.getMitarbeiterListe();
        return mitarbeiterListeType != null ? mitarbeiterListeType.getMitarbeiter() : new LinkedList<>();
    }
}
