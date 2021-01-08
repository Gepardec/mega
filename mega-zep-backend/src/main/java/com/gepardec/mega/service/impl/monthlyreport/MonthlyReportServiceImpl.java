package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.rest.model.PmProgress;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.zep.ZepService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Override
    public boolean setOpenAndUnassignedStepEntriesDone(Employee employee, Long stepId) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        return stepEntryService.setOpenAndAssignedStepEntriesDone(employee, stepId, from, to);
    }

    private MonthlyReport calcWarnings(List<ProjectEntry> projectEntries, Employee employee) {
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectEntries);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectEntries);

        final List<Comment> comments = new ArrayList<>();
        if (employee != null) {
            LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
            LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));
            comments.addAll(commentService.findCommentsForEmployee(employee, fromDate, toDate));
        }
        final Optional<State> employeeCheckState = stepEntryService.findEmployeeCheckState(employee);
        final boolean isAssigned = employeeCheckState.isPresent();

        final List<StepEntry> allOwnedStepEntriesForPMProgress = stepEntryService.findAllOwnedAndUnassignedStepEntriesForPMProgress(employee.email(), employee.releaseDate());
        List<PmProgress> pmProgresses = new ArrayList<>();
        allOwnedStepEntriesForPMProgress.stream()
                .forEach(stepEntry -> pmProgresses.add(
                        PmProgress.builder()
                                .project(stepEntry.getProject())
                                .assigneeEmail(stepEntry.getAssignee().getEmail())
                                .firstname(stepEntry.getAssignee().getFirstname())
                                .lastname(stepEntry.getAssignee().getLastname())
                                .state(stepEntry.getState())
                                .stepId(stepEntry.getStep().getId())
                                .build()
                ));

        final List<StepEntry> allOwnedAndAssignedStepEntries = stepEntryService.findAllOwnedAndUnassignedStepEntriesForOtherChecks(employee);
        final boolean otherChecksDone = allOwnedAndAssignedStepEntries.stream().allMatch(stepEntry -> stepEntry.getState() == State.DONE);

        return MonthlyReport.of(employee, timeWarnings, journeyWarnings, comments, employeeCheckState.orElse(State.OPEN), isAssigned, pmProgresses, otherChecksDone);
    }
}
