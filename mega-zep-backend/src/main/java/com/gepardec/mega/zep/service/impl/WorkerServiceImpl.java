package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.data.BreakWarning;
import com.gepardec.mega.data.JourneyWarning;
import com.gepardec.mega.data.MonthendReport;
import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.security.AuthorizationInterceptor;
import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.*;
import org.apache.http.HttpStatus;
import utils.DateUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static utils.DateUtils.dateToString;

@Interceptors(AuthorizationInterceptor.class)
@ApplicationScoped
public class WorkerServiceImpl implements WorkerService {

    @Inject
    @Named("ZepAuthorizationSOAPPortType")
    ZepSoapPortType zepSoapPortType;

    @Inject
    @Named("ZepAuthorizationRequestHeaderType")
    RequestHeaderType requestHeaderType;

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
            return employees.stream().filter(e -> e.getEmail() != null && e.getEmail().equals(user.getEmail())).findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<MitarbeiterType> getAllEmployees() {
        ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType);
        return filterActiveEmployees(rmrt);
    }

    @Override
    public Integer updateEmployees(final List<MitarbeiterType> employees) {
        final List<Integer> statusCodeList = new LinkedList<>();

        employees.forEach(e -> statusCodeList.add(updateEmployee(e)));

        return statusCodeList.stream().filter(statuscode -> statuscode == HttpStatus.SC_INTERNAL_SERVER_ERROR).findAny().orElse(HttpStatus.SC_OK);
    }

    @Override
    public MonthendReport getMonthendReport(GoogleUser user) {

        MitarbeiterType mitarbeiter = getEmployee(user);
        if (mitarbeiter == null) {
            return Optional.empty();
        }
        String userId = mitarbeiter.getUserId();

        ReadProjektzeitenSearchCriteriaType searchCriteria = new ReadProjektzeitenSearchCriteriaType();

        //show last 3 months
        LocalDate today = DateUtils.now();
        searchCriteria.setVon(dateToString(today.minus(Period.ofMonths(3))));
        searchCriteria.setBis(dateToString(today));

        UserIdListeType userIdListType = new UserIdListeType();
        userIdListType.getUserId().add(userId);
        searchCriteria.setUserIdListe(userIdListType);
        projektzeitenRequest.setReadProjektzeitenSearchCriteria(searchCriteria);

        ReadProjektzeitenResponseType projektzeitenResponse = zepSoapPortType.readProjektzeiten(projektzeitenRequest);

        return calculateMonthendWarnings(projektzeitenResponse);

    }

    private static MonthendReport calculateMonthendWarnings(ReadProjektzeitenResponseType projektzeitenResponse) {

        if(projektzeitenResponse == null
                || projektzeitenResponse.getProjektzeitListe() == null)) {
            return Optional.empty();
        }

        MonthendReport monthendReport = new MonthendReport();
        //TODO: map to monthendReport

        BreakWarning breakWarning = new BreakWarning();
        breakWarning.setDate(LocalDate.of(2019, 11,10));
        breakWarning.setDay("Mo");
        breakWarning.setTooLessBreak(1.5);
        breakWarning.setTooMuchWorkTime(2.0);
        breakWarning.setTooLessRest(3.0);

        JourneyWarning journeyWarning = new JourneyWarning();
        journeyWarning.setDate(LocalDate.of(2019,11,10));
        journeyWarning.setDay("Di");
        journeyWarning.setWarningText("Das ist eine Warnung");

        monthendReport.getBreakWarnings().add(breakWarning);
        monthendReport.getJourneyWarnings().add(journeyWarning);


//        projektzeitenResponse.getProjektzeitListe().getProjektzeiten().stream()
//                .filter()
//                .map();




        return Optional.ofNullable(monthendReport);
    }

    @Override
    public Integer updateEmployee(final MitarbeiterType employee) {
        try {
            final UpdateMitarbeiterRequestType umrt = new UpdateMitarbeiterRequestType();
            umrt.setRequestHeader(requestHeaderType);
            umrt.setMitarbeiter(employee);

            final UpdateMitarbeiterResponseType updateMitarbeiterResponseType = zepSoapPortType.updateMitarbeiter(umrt);
            final ResponseHeaderType responseHeaderType = updateMitarbeiterResponseType != null ? updateMitarbeiterResponseType.getResponseHeader() : null;

            return responseHeaderType != null ? Integer.parseInt(responseHeaderType.getReturnCode()) : HttpStatus.SC_INTERNAL_SERVER_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return HttpStatus.SC_INTERNAL_SERVER_ERROR;
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
