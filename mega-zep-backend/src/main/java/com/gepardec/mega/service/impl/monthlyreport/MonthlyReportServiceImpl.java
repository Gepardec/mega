package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.*;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.service.comment.CommentService;
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

    @Inject
    CommentService commentService;

    @Inject
    StepEntryService stepEntryService;

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
        final List<CommentDTO> comments = commentService.findCommentsForEmployee(employee);
        final State emcState = stepEntryService.getEmcState(employee);

        MonthlyReport monthlyReport = new MonthlyReport();
        monthlyReport.setJourneyWarnings(journeyWarnings);
        monthlyReport.setTimeWarnings(timeWarnings);
        monthlyReport.setEmployee(employee);
        monthlyReport.setComments(comments);
        monthlyReport.setEmcState(emcState);

        return monthlyReport;
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
