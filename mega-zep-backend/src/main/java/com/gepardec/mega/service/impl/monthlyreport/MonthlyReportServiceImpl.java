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
import com.gepardec.mega.notification.mail.dates.BusinessDayCalculator;
import com.gepardec.mega.rest.model.PmProgress;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.zep.ZepService;
import de.provantis.zep.FehlzeitType;
import de.provantis.zep.ProjektzeitType;

import javax.annotation.Nonnull;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class MonthlyReportServiceImpl implements MonthlyReportService {

    public static final String COMPENSATORY_DAYS = "FA";
    public static final String HOME_OFFICE_DAYS = "HO";
    public static final String VACATION_DAYS = "UB";

    private LocalDate currentMonthYear;

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
        final LocalDate date;

        if (employee != null && employee.releaseDate() != null) {
            date = LocalDate.parse(employee.releaseDate()).plusMonths(1);
        } else {
            date = null;
        }
        return getMonthendReportForUser(userId, date);
    }

    @Override
    public MonthlyReport getMonthendReportForUser(String userId, LocalDate date) {
        Employee employee = zepService.getEmployee(userId);
        currentMonthYear = date;

        return buildMonthlyReport(employee,
                zepService.getProjectTimes(employee, date),
                zepService.getBillableForEmployee(employee, date),
                zepService.getAbsenceForEmployee(employee, date),
                stepEntryService.findEmployeeCheckState(employee, date));
    }

    @Override
    public boolean setOpenAndUnassignedStepEntriesDone(Employee employee, Long stepId) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        return stepEntryService.setOpenAndAssignedStepEntriesDone(employee, stepId, from, to);
    }

    private MonthlyReport buildMonthlyReport(Employee employee, List<ProjectEntry> projectEntries, List<ProjektzeitType> billableEntries, List<FehlzeitType> absenceEntries, Optional<EmployeeState> employeeCheckState) {
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectEntries);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectEntries);

        final List<Comment> comments = new ArrayList<>();
        List<PmProgress> pmProgresses = new ArrayList<>();
        if (employee != null) {
            LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
            LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));
            comments.addAll(commentService.findCommentsForEmployee(employee, fromDate, toDate));

            final List<StepEntry> allOwnedStepEntriesForPMProgress = stepEntryService.findAllOwnedAndUnassignedStepEntriesForPMProgress(employee.email(), employee.releaseDate());
            allOwnedStepEntriesForPMProgress
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
                .billableTime(zepService.getBillableTimesForEmployee(billableEntries, employee, true))
                .totalWorkingTime(zepService.getTotalWorkingTimeForEmployee(billableEntries, employee))
                .compensatoryDays(getAbsenceTimesForEmployee(absenceEntries, COMPENSATORY_DAYS))
                .homeofficeDays(getAbsenceTimesForEmployee(absenceEntries, HOME_OFFICE_DAYS))
                .vacationDays(getAbsenceTimesForEmployee(absenceEntries, VACATION_DAYS))
                .build();
    }

    private int getAbsenceTimesForEmployee(@Nonnull List<FehlzeitType> fehlZeitTypeList, String absenceType) {
        return (int) fehlZeitTypeList.stream()
                .filter(fzt -> fzt.getFehlgrund().equals(absenceType))
                .filter(FehlzeitType::isGenehmigt)
                .map(this::trimDurationToCurrentMonth)
                .flatMap(ftl -> LocalDate.parse(ftl.getStartdatum()).datesUntil(LocalDate.parse(ftl.getEnddatum()).plusDays(1)))// add missing day because datesUntil() does not include the given endDate
                .filter(BusinessDayCalculator::isWorkingDay)
                .count();
    }

    private FehlzeitType trimDurationToCurrentMonth(FehlzeitType fehlzeit) {
        if (LocalDate.parse(fehlzeit.getEnddatum()).getMonthValue() > currentMonthYear.getMonthValue()) {
            fehlzeit.setEnddatum(currentMonthYear.with(TemporalAdjusters.lastDayOfMonth()).toString());
        } else if (LocalDate.parse(fehlzeit.getStartdatum()).getMonthValue() < currentMonthYear.getMonthValue()) {
            fehlzeit.setStartdatum(currentMonthYear.with(TemporalAdjusters.firstDayOfMonth()).toString());
        }
        return fehlzeit;
    }
}
