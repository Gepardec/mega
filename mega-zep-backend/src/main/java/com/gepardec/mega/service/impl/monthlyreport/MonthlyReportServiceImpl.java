package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.*;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import com.gepardec.mega.service.impl.monthlyreport.calculation.WarningCalculator;
import com.gepardec.mega.zep.ZepService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class MonthlyReportServiceImpl implements MonthlyReportService {

    @Inject
    ZepService zepService;

    @Inject
    WarningCalculator warningCalculator;

    @Override
    public MonthlyReport getMonthendReportForUser(final String userId) {
        Employee employee = zepService.getEmployee(userId);
        return calcWarnings(zepService.getProjectTimes(employee), employee);
    }

    private MonthlyReport calcWarnings(List<ProjectEntry> projectEntries, Employee employee) {
        if (projectEntries == null || projectEntries.isEmpty()) {
            return null;
        }
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(filterJourneyTimeEntries(projectEntries));
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(filterProjectTimeEntries(projectEntries));

        return MonthlyReport.of(employee, timeWarnings, journeyWarnings);
    }

    private List<ProjectTimeEntry> filterProjectTimeEntries(List<ProjectEntry> projectEntries) {
        return projectEntries.stream().filter(entry -> entry instanceof ProjectTimeEntry).map(ProjectTimeEntry.class::cast).collect(Collectors
                .toList());
    }

    private List<JourneyTimeEntry> filterJourneyTimeEntries(List<ProjectEntry> projectEntries) {
        return projectEntries.stream().filter(entry -> entry instanceof JourneyTimeEntry).map(JourneyTimeEntry.class::cast).collect(Collectors
                .toList());
    }
}
