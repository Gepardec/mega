package com.gepardec.mega.service.impl;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntryWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.notification.mail.dates.OfficeCalendarUtil;
import com.gepardec.mega.rest.model.PmProgressDto;
import com.gepardec.mega.service.api.CommentService;
import com.gepardec.mega.service.api.MonthlyReportService;
import com.gepardec.mega.service.api.StepEntryService;
import com.gepardec.mega.service.helper.WarningCalculator;
import com.gepardec.mega.zep.ZepService;
import de.provantis.zep.FehlzeitType;
import de.provantis.zep.ProjektzeitType;

import javax.annotation.Nonnull;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequestScoped
public class MonthlyReportServiceImpl implements MonthlyReportService {

    public static final String COMPENSATORY_DAYS = "FA";
    public static final String HOME_OFFICE_DAYS = "HO";
    public static final String VACATION_DAYS = "UB";
    public static final String NURSING_DAYS = "PU";
    public static final String MATERNITY_LEAVE_DAYS = "KA";
    public static final String EXTERNAL_TRAINING_DAYS = "EW";
    public static final String CONFERENCE_DAYS = "KO";
    public static final String MATERNITY_PROTECTION_DAYS = "MU";
    public static final String FATHER_MONTH_DAYS = "PA";
    public static final String PAID_SPECIAL_LEAVE_DAYS = "SU";
    public static final String NON_PAID_VACATION_DAYS = "UU";

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
        final LocalDate date = Optional.ofNullable(zepService.getEmployee(userId))
                .flatMap(emp -> Optional.ofNullable(emp.getReleaseDate()))
                .filter(this::checkReleaseDate)
                .map(releaseDate -> LocalDate.parse(Objects.requireNonNull(releaseDate)).plusMonths(1))
                .orElse(null);

        return getMonthendReportForUser(userId, date);
    }

    private boolean checkReleaseDate(String releaseDate) {
        try {
            LocalDate.parse(Objects.requireNonNull(releaseDate));
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
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
        LocalDate from = DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate());
        LocalDate to = DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate());

        return stepEntryService.setOpenAndAssignedStepEntriesDone(employee, stepId, from, to);
    }

    private MonthlyReport buildMonthlyReport(Employee employee, List<ProjectEntry> projectEntries, List<ProjektzeitType> billableEntries, List<FehlzeitType> absenceEntries, Optional<EmployeeState> employeeCheckState) {
        final List<JourneyWarning> journeyWarnings = warningCalculator.determineJourneyWarnings(projectEntries);
        final List<TimeWarning> timeWarnings = warningCalculator.determineTimeWarnings(projectEntries);
        timeWarnings.addAll(warningCalculator.determineNoTimeEntries(projectEntries, absenceEntries));
        timeWarnings.sort(Comparator.comparing(ProjectEntryWarning::getDate));

        final List<Comment> comments = new ArrayList<>();
        List<PmProgressDto> pmProgressDtos = new ArrayList<>();
        if (employee != null) {

            if (checkReleaseDate(employee.getReleaseDate())) {
                LocalDate fromDate = DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate());
                LocalDate toDate = DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate());
                comments.addAll(commentService.findCommentsForEmployee(employee, fromDate, toDate));
            }

            final List<StepEntry> allOwnedStepEntriesForPMProgress = stepEntryService.findAllOwnedAndUnassignedStepEntriesForPMProgress(employee.getEmail(), employee.getReleaseDate());
            allOwnedStepEntriesForPMProgress
                    .forEach(stepEntry -> pmProgressDtos.add(
                            PmProgressDto.builder()
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
                .employeeProgresses(pmProgressDtos)
                .otherChecksDone(otherChecksDone)
                .billableTime(zepService.getBillableTimesForEmployee(billableEntries, employee))
                .totalWorkingTime(zepService.getTotalWorkingTimeForEmployee(billableEntries, employee))
                .compensatoryDays(getAbsenceTimesForEmployee(absenceEntries, COMPENSATORY_DAYS))
                .homeofficeDays(getAbsenceTimesForEmployee(absenceEntries, HOME_OFFICE_DAYS))
                .vacationDays(getAbsenceTimesForEmployee(absenceEntries, VACATION_DAYS))
                .nursingDays(getAbsenceTimesForEmployee(absenceEntries, NURSING_DAYS))
                .maternityLeaveDays(getAbsenceTimesForEmployee(absenceEntries, MATERNITY_LEAVE_DAYS))
                .externalTrainingDays(getAbsenceTimesForEmployee(absenceEntries, EXTERNAL_TRAINING_DAYS))
                .conferenceDays(getAbsenceTimesForEmployee(absenceEntries, CONFERENCE_DAYS))
                .maternityProtectionDays(getAbsenceTimesForEmployee(absenceEntries, MATERNITY_PROTECTION_DAYS))
                .fatherMonthDays(getAbsenceTimesForEmployee(absenceEntries, FATHER_MONTH_DAYS))
                .paidSpecialLeaveDays(getAbsenceTimesForEmployee(absenceEntries, PAID_SPECIAL_LEAVE_DAYS))
                .nonPaidVacationDays(getAbsenceTimesForEmployee(absenceEntries, NON_PAID_VACATION_DAYS))
                .build();
    }

    private int getAbsenceTimesForEmployee(@Nonnull List<FehlzeitType> fehlZeitTypeList, String absenceType) {
        return (int) fehlZeitTypeList.stream()
                .filter(fzt -> fzt.getFehlgrund().equals(absenceType))
                .filter(FehlzeitType::isGenehmigt)
                .map(this::trimDurationToCurrentMonth)
                .mapToLong(ftl -> OfficeCalendarUtil.getWorkingDaysBetween(LocalDate.parse(ftl.getStartdatum()), LocalDate.parse(ftl.getEnddatum())).size())
                .sum();
    }

    private FehlzeitType trimDurationToCurrentMonth(FehlzeitType fehlzeit) {
        if (LocalDate.parse(fehlzeit.getEnddatum()).getMonthValue() > currentMonthYear.getMonthValue()) {
            fehlzeit.setEnddatum(currentMonthYear.with(TemporalAdjusters.lastDayOfMonth()).toString());
        }
        if (LocalDate.parse(fehlzeit.getStartdatum()).getMonthValue() < currentMonthYear.getMonthValue()) {
            fehlzeit.setStartdatum(currentMonthYear.with(TemporalAdjusters.firstDayOfMonth()).toString());
        }
        return fehlzeit;
    }
}
