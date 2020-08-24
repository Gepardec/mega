package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import com.gepardec.mega.zep.ZepService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@RequestScoped
public class MonthlyReportServiceImpl implements MonthlyReportService {

    @Inject
    ZepService zepService;

    @Inject
    WarningConfig warningConfig;

    @Override
    public MonthlyReport getMonthendReportForUser(final String userId) {

        Employee employee = zepService.getEmployee(userId);

        return calcWarnings(zepService.getProjectTimes(employee), employee);
    }

    private MonthlyReport calcWarnings(List<ProjectTimeEntry> projectTimeList, Employee employee) {
        MonthlyReport monthlyReport = new MonthlyReport();

        if (projectTimeList == null || projectTimeList.isEmpty()) {
            monthlyReport = new MonthlyReport();
            monthlyReport.setEmployee(employee);
            monthlyReport.setJourneyWarnings(Collections.emptyList());
            monthlyReport.setTimeWarnings(Collections.emptyList());

            return monthlyReport;
        }

        final WarningCalculator warningCalculator = new WarningCalculator(warningConfig);
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectTimeList);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectTimeList);

        monthlyReport.setJourneyWarnings(journeyWarnings);
        monthlyReport.setTimeWarnings(timeWarnings);
        monthlyReport.setEmployee(employee);

        return monthlyReport;
    }
}
