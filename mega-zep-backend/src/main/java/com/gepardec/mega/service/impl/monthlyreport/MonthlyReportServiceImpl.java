package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.service.impl.monthlyreport.calculation.WarningCalculator;
import com.gepardec.mega.zep.ZepService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

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
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectEntries);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectEntries);
        final List<Comment> comments = commentService.findCommentsForEmployee(employee);
        final Optional<State> employeeCheckState = stepEntryService.findEmployeeCheckState(employee);
        final boolean otherChecksDone = stepEntryService.areOtherChecksDone(employee);

        return MonthlyReport.of(employee, timeWarnings, journeyWarnings, comments, employeeCheckState.orElse(State.OPEN), otherChecksDone);
    }
}
