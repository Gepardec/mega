package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
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

    @Override
    public boolean setOpenAndUnassignedStepEntriesDone(Employee employee, Long stepId) {
        return stepEntryService.setOpenAndAssignedStepEntriesDone(employee, stepId);
    }

    private MonthlyReport calcWarnings(List<ProjectEntry> projectEntries, Employee employee) {
        if (projectEntries == null || projectEntries.isEmpty()) {
            return null;
        }
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(filterJourneyTimeEntries(projectEntries));
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(filterProjectTimeEntries(projectEntries));
        final List<Comment> comments = commentService.findCommentsForEmployee(employee);
        final Optional<State> employeeCheckState = stepEntryService.findEmployeeCheckState(employee);
        final boolean isAssigned = employeeCheckState.isPresent();
        final boolean otherChecksDone = stepEntryService.areOtherChecksDone(employee);

        return MonthlyReport.of(employee, timeWarnings, journeyWarnings, comments, employeeCheckState.orElse(State.OPEN), isAssigned, otherChecksDone);
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
