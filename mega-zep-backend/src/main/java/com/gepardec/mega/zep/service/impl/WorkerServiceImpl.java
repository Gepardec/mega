package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.monthlyreport.ProjectTimeManager;
import com.gepardec.mega.monthlyreport.warning.WarningConfig;
import com.gepardec.mega.utils.DateUtils;
import com.gepardec.mega.zep.service.api.WorkerService;
import com.gepardec.mega.zep.soap.ZepSoapProvider;
import de.provantis.zep.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;

import static com.gepardec.mega.utils.DateUtils.getFirstDayOfFollowingMonth;
import static com.gepardec.mega.utils.DateUtils.getLastDayOfFollowingMonth;
import static com.gepardec.mega.zep.service.ZepStatusCodeMapper.toHttpResponseCode;
import static java.lang.String.format;

@RequestScoped
public class WorkerServiceImpl implements WorkerService {

    @Inject
    Logger logger;

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    ZepSoapProvider zepSoapProvider;

    @Inject
    WarningConfig warningConfig;

    @Override
    public MitarbeiterType getEmployee(final String eMail) {

        try {
            final ReadMitarbeiterRequestType readMitarbeiterRequestType = new ReadMitarbeiterRequestType();
            readMitarbeiterRequestType.setRequestHeader(zepSoapProvider.createRequestHeaderType());
            final List<MitarbeiterType> employees = flatMap(zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType));
            return employees.stream()
                    .filter(e -> e.getEmail() != null && e.getEmail().equals(eMail))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            logger.error(format("error getEmployee for user: %s", eMail));
            return null;
        }
    }

    @Override
    public List<MitarbeiterType> getAllActiveEmployees() {
        final ReadMitarbeiterRequestType readMitarbeiterRequestType = new ReadMitarbeiterRequestType();
        readMitarbeiterRequestType.setRequestHeader(zepSoapProvider.createRequestHeaderType());
        ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType);
        return filterActiveEmployees(rmrt);
    }

    @Override
    public Integer updateEmployeesReleaseDate(final List<Pair<String, String>> pairs) {
        final List<Integer> statusCodeList = new LinkedList<>();

        pairs.forEach(e -> statusCodeList.add(updateEmployeeReleaseDate(e.getLeft(), e.getRight())));

        return statusCodeList.stream()
                .filter(statuscode -> statuscode == HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .findAny()
                .orElse(HttpStatus.SC_OK);
    }

    @Override
    public MonthlyReport getMonthendReportForUser(final String eMail) {
        MitarbeiterType employee = getEmployee(eMail);
        if (employee == null) {
            return null;
        }
        final ReadProjektzeitenRequestType projektzeitenRequest = new ReadProjektzeitenRequestType();
        projektzeitenRequest.setRequestHeader(zepSoapProvider.createRequestHeaderType());
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
    public Integer updateEmployeeReleaseDate(final String eMail, final String releaseDate) {
        try {
            final UpdateMitarbeiterRequestType umrt = new UpdateMitarbeiterRequestType();
            umrt.setRequestHeader(zepSoapProvider.createRequestHeaderType());

//            TODO: check api
//            umrt.getMitarbeiter().setFreigabedatum(releaseDate);

            MitarbeiterType employee = getEmployee(eMail);
            employee.setFreigabedatum(releaseDate);
            umrt.setMitarbeiter(employee);

            final UpdateMitarbeiterResponseType updateMitarbeiterResponseType = zepSoapPortType.updateMitarbeiter(umrt);
            final ResponseHeaderType responseHeaderType = updateMitarbeiterResponseType != null ? updateMitarbeiterResponseType.getResponseHeader() : null;

            return toHttpResponseCode(responseHeaderType);
        } catch (Exception e) {
            logger.error(format("Errro updatingEmployee, id: %s", eMail));
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
