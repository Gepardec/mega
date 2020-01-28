package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.monthlyreport.ProjectTimeManager;
import com.gepardec.mega.monthlyreport.journey.JourneyWarning;
import com.gepardec.mega.monthlyreport.warning.TimeWarning;
import com.gepardec.mega.monthlyreport.warning.WarningCalculator;
import com.gepardec.mega.monthlyreport.warning.WarningConfig;
import com.gepardec.mega.aplication.utils.DateUtils;
import com.gepardec.mega.zep.exception.ZepServiceException;
import com.gepardec.mega.zep.service.api.WorkerService;
import com.gepardec.mega.zep.soap.ZepSoapProvider;
import de.provantis.zep.*;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;

import static com.gepardec.mega.aplication.utils.DateUtils.getFirstDayOfFollowingMonth;
import static com.gepardec.mega.aplication.utils.DateUtils.getLastDayOfFollowingMonth;
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
    public MitarbeiterType getEmployee(final String userId) {
        try {
            final ReadMitarbeiterRequestType readMitarbeiterRequestType = new ReadMitarbeiterRequestType();
            readMitarbeiterRequestType.setRequestHeader(zepSoapProvider.createRequestHeaderType());
            final ReadMitarbeiterSearchCriteriaType searchCriteria = new ReadMitarbeiterSearchCriteriaType();
            searchCriteria.setUserId(userId);

            final List<MitarbeiterType> employees = flatMap(zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType));
            return employees.stream()
                    .filter(e -> e.getUserId().equals(userId))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            logger.error(format("error getEmployee for user: %s", userId));
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
    public List<String> updateEmployeesReleaseDate(final Map<String, String> emailReleaseDates) {
        final List<String> failedEmails = new LinkedList<>();

        for (final Map.Entry<String, String> entry : emailReleaseDates.entrySet()) {
            try {
                updateEmployeeReleaseDate(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                failedEmails.add(entry.getKey());
            }
        }

        return failedEmails;
    }

    @Override
    public MonthlyReport getMonthendReportForUser(final String userId) {
        MitarbeiterType employee = getEmployee(userId);
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


    private MonthlyReport calcWarnings(ReadProjektzeitenResponseType projectTimeResponse, MitarbeiterType mitarbeiterType) {
        if (projectTimeResponse == null || projectTimeResponse.getProjektzeitListe() == null) {
            return null;
        }
        final ProjectTimeManager projectTimeManager = new ProjectTimeManager(projectTimeResponse.getProjektzeitListe().getProjektzeiten());
        final WarningCalculator warningCalculator = new WarningCalculator(warningConfig);
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectTimeManager);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectTimeManager);

        return new MonthlyReport(timeWarnings, journeyWarnings, mitarbeiterType);
    }


    @Override
    public void updateEmployeeReleaseDate(final String id, final String releaseDate) {
        final UpdateMitarbeiterRequestType umrt = new UpdateMitarbeiterRequestType();
        umrt.setRequestHeader(zepSoapProvider.createRequestHeaderType());

        final MitarbeiterType mitarbeiter = new MitarbeiterType();
        mitarbeiter.setUserId(id);
        mitarbeiter.setFreigabedatum(releaseDate);
        umrt.setMitarbeiter(mitarbeiter);

        final UpdateMitarbeiterResponseType updateMitarbeiterResponseType = zepSoapPortType.updateMitarbeiter(umrt);
        final ResponseHeaderType responseHeaderType = updateMitarbeiterResponseType != null ? updateMitarbeiterResponseType.getResponseHeader() : null;

        if (responseHeaderType != null && Integer.parseInt(responseHeaderType.getReturnCode()) == 0) {
            throw new ZepServiceException("updateEmployeeReleaseDate failed with code: " + responseHeaderType.getReturnCode());
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
                    final LocalDate endDate = DateUtils.parseDate(Objects.requireNonNull(last).getEnddatum());
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
