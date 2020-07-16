package com.gepardec.mega.service.impl;

import com.gepardec.mega.domain.MonthlyReport;
import com.gepardec.mega.service.monthlyreport.ProjectTimeManager;
import com.gepardec.mega.domain.JourneyWarning;
import com.gepardec.mega.domain.TimeWarning;
import com.gepardec.mega.service.monthlyreport.WarningCalculator;
import com.gepardec.mega.service.monthlyreport.WarningConfig;
import com.gepardec.mega.service.api.EmployeeService;
import com.gepardec.mega.domain.Employee;
import com.gepardec.mega.service.api.WorkerService;
import com.gepardec.mega.zep.ZepSoapProvider;
import de.provantis.zep.*;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.gepardec.mega.application.utils.DateUtils.getFirstDayOfFollowingMonth;
import static com.gepardec.mega.application.utils.DateUtils.getLastDayOfFollowingMonth;

@RequestScoped
public class WorkerServiceImpl implements WorkerService {

    @Inject
    Logger logger;

    @Inject
    EmployeeService employeeService;

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    ZepSoapProvider zepSoapProvider;

    @Inject
    WarningConfig warningConfig;

    @Override
    public MonthlyReport getMonthendReportForUser(final String userId) {
        Employee employee = employeeService.getEmployee(userId);
        if (employee == null) {
            return null;
        }
        final ReadProjektzeitenRequestType projektzeitenRequest = new ReadProjektzeitenRequestType();
        projektzeitenRequest.setRequestHeader(zepSoapProvider.createRequestHeaderType());

        final ReadProjektzeitenSearchCriteriaType searchCriteria;
        try {
            searchCriteria = createProjectTimeSearchCriteria(employee);
        } catch (DateTimeParseException e) {
            logger.error("invalid release date {0}", e);
            return null;
        }
        projektzeitenRequest.setReadProjektzeitenSearchCriteria(searchCriteria);

        ReadProjektzeitenResponseType projectTimeResponse = zepSoapPortType.readProjektzeiten(projektzeitenRequest);

        return calcWarnings(projectTimeResponse, employee);

    }

    private ReadProjektzeitenSearchCriteriaType createProjectTimeSearchCriteria(Employee employee) {
        ReadProjektzeitenSearchCriteriaType searchCriteria = new ReadProjektzeitenSearchCriteriaType();

        final String releaseDate = employee.getReleaseDate();
        searchCriteria.setVon(getFirstDayOfFollowingMonth(releaseDate));
        searchCriteria.setBis(getLastDayOfFollowingMonth(releaseDate));

        UserIdListeType userIdListType = new UserIdListeType();
        userIdListType.getUserId().add(employee.getUserId());
        searchCriteria.setUserIdListe(userIdListType);
        return searchCriteria;
    }

    private MonthlyReport calcWarnings(ReadProjektzeitenResponseType projectTimeResponse, Employee employee) {
        if (projectTimeResponse == null || projectTimeResponse.getProjektzeitListe() == null) {
            return null;
        }
        final ProjectTimeManager projectTimeManager = new ProjectTimeManager(projectTimeResponse.getProjektzeitListe().getProjektzeiten());
        final WarningCalculator warningCalculator = new WarningCalculator(warningConfig);
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectTimeManager);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectTimeManager);

        final MonthlyReport monthlyReport  = new MonthlyReport();
        monthlyReport.setJourneyWarnings(journeyWarnings);
        monthlyReport.setTimeWarnings(timeWarnings);
        monthlyReport.setEmployee(employee);
        return monthlyReport;
    }
}
