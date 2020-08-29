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
import java.util.List;
import java.util.ResourceBundle;

@RequestScoped
public class MonthlyReportServiceImpl implements MonthlyReportService {

    @Inject
    ZepService zepService;

    @Inject
    ResourceBundle messages;

    @Override
    public MonthlyReport getMonthendReportForUser(final String userId) {
        Employee employee = zepService.getEmployee(userId);
        return calcWarnings(zepService.getProjectTimes(employee), employee);
    }

    private MonthlyReport calcWarnings(List<ProjectTimeEntry> projectTimeList, Employee employee) {
        if (projectTimeList == null || projectTimeList.isEmpty()) {
            return null;
        }
        final WarningCalculator warningCalculator = new WarningCalculator(messages);
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectTimeList);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectTimeList);

        MonthlyReport monthlyReport = new MonthlyReport();
        monthlyReport.setJourneyWarnings(journeyWarnings);
        monthlyReport.setTimeWarnings(timeWarnings);
        monthlyReport.setEmployee(employee);

        return monthlyReport;
    }
}
