package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.StepEntry;
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
import de.provantis.zep.FehlzeitType;
import de.provantis.zep.ProjektzeitType;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.annotation.Nonnull;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RequestScoped
public class MonthlyReportServiceImpl implements MonthlyReportService {

    private static final String BILLABLE_TIME_FORMAT = "HH:mm";

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

        return buildMonthlyReport(employee, zepService.getProjectTimes(employee), zepService.getBillableForEmployee(employee), zepService.getAbsenceForEmployee(employee));
    }

    @Override
    public boolean setOpenAndUnassignedStepEntriesDone(Employee employee, Long stepId) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        return stepEntryService.setOpenAndAssignedStepEntriesDone(employee, stepId, from, to);
    }

    private MonthlyReport buildMonthlyReport(Employee employee, List<ProjectEntry> projectEntries, List<ProjektzeitType> billableEntries, List<FehlzeitType> absenceEntries) {
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectEntries);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectEntries);

        final List<Comment> comments = new ArrayList<>();
        List<PmProgress> pmProgresses = new ArrayList<>();
        if (employee != null) {
            LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
            LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));
            comments.addAll(commentService.findCommentsForEmployee(employee, fromDate, toDate));

            final List<StepEntry> allOwnedStepEntriesForPMProgress = stepEntryService.findAllOwnedAndUnassignedStepEntriesForPMProgress(employee.email(), employee.releaseDate());
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
        }

        final Optional<EmployeeState> employeeCheckState = stepEntryService.findEmployeeCheckState(employee);
        final boolean isAssigned = employeeCheckState.isPresent();

        final List<StepEntry> allOwnedAndAssignedStepEntries = stepEntryService.findAllOwnedAndUnassignedStepEntriesForOtherChecks(employee);
        final boolean otherChecksDone = allOwnedAndAssignedStepEntries.stream().allMatch(stepEntry -> stepEntry.getState() == EmployeeState.DONE);

        return MonthlyReport.builder()
                .employee(employee)
                .timeWarnings(timeWarnings)
                .journeyWarnings(journeyWarnings)
                .comments(comments)
                .employeeCheckState(employeeCheckState.orElse(EmployeeState.OPEN))
                .isAssigned(isAssigned)
                .employeeProgresses(pmProgresses)
                .otherChecksDone(otherChecksDone)
                .billableTime(getBillableTimesForEmployee(billableEntries, employee, true))
                .totalWorkingTime(getTotalWorkingTimeForEmployee(billableEntries, employee))
                .compensatoryDays(getAbsenceTimesForEmployee(absenceEntries, employee, "FA"))
                .homeofficeDays(getAbsenceTimesForEmployee(absenceEntries, employee, "HO"))
                .vacationDays(getAbsenceTimesForEmployee(absenceEntries, employee, "UB"))
                .build();
    }

    private String getBillableTimesForEmployee(@Nonnull List<ProjektzeitType> projektzeitTypeList, @Nonnull Employee employee, boolean billable) {
        Duration totalBillable = projektzeitTypeList.stream()
                .filter(pzt -> pzt.getUserId().equals(employee.userId()))
                .filter(billable ? ProjektzeitType::isIstFakturierbar : Predicate.not(ProjektzeitType::isIstFakturierbar))
                .map(pzt -> LocalTime.parse(pzt.getDauer()))
                .map(lt -> Duration.between(LocalTime.MIN, lt))
                .reduce(Duration.ZERO, Duration::plus);

        return DurationFormatUtils.formatDuration(totalBillable.toMillis(), BILLABLE_TIME_FORMAT);
    }

    private String getTotalWorkingTimeForEmployee(@Nonnull List<ProjektzeitType> projektzeitTypeList, @Nonnull Employee employee) {
        Duration totalBillable = projektzeitTypeList.stream()
                .filter(pzt -> pzt.getUserId().equals(employee.userId()))
                .map(pzt -> LocalTime.parse(pzt.getDauer()))
                .map(lt -> Duration.between(LocalTime.MIN, lt))
                .reduce(Duration.ZERO, Duration::plus);

        return DurationFormatUtils.formatDuration(totalBillable.toMillis(), BILLABLE_TIME_FORMAT);
    }

    private int getAbsenceTimesForEmployee(@Nonnull List<FehlzeitType> fehlZeitTypeList, @Nonnull Employee employee, String absenceType) {
        Period totalAbsence = Period.ofDays((int) fehlZeitTypeList.stream().filter(fzt -> fzt.getFehlgrund().equals(absenceType)).count());

        return totalAbsence.getDays();
    }
}
