package com.gepardec.mega.service.impl;

import com.gepardec.mega.domain.model.*;
import com.gepardec.mega.service.monthlyreport.WarningCalculator;
import com.gepardec.mega.service.monthlyreport.WarningConfig;
import com.gepardec.mega.service.api.WorkerService;
import com.gepardec.mega.zep.ZepService;
import de.provantis.zep.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

import static com.gepardec.mega.domain.utils.DateUtils.getFirstDayOfFollowingMonth;
import static com.gepardec.mega.domain.utils.DateUtils.getLastDayOfFollowingMonth;

@RequestScoped
public class WorkerServiceImpl implements WorkerService {

    @Inject
    ZepService zepService;

    @Inject
    WarningConfig warningConfig;

    @Override
    public MonthlyReport getMonthendReportForUser(final String userId) {

        Employee employee = zepService.getEmployee(userId);

        return calcWarnings(zepService.getProjectTimes(employee), employee);
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

    private MonthlyReport calcWarnings(List<ProjectTimeEntry> projectTimeList, Employee employee) {
        if (projectTimeList == null || projectTimeList.isEmpty()) {
            return null;
        }
        final WarningCalculator warningCalculator = new WarningCalculator(warningConfig);
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectTimeList);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectTimeList);

        final MonthlyReport monthlyReport  = new MonthlyReport();
        monthlyReport.setJourneyWarnings(journeyWarnings);
        monthlyReport.setTimeWarnings(timeWarnings);
        monthlyReport.setEmployee(employee);
        return monthlyReport;
    }
}
