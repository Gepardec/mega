package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.AbsenteeType;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;
import com.gepardec.mega.notification.mail.dates.OfficeCalendarUtil;
import de.provantis.zep.FehlzeitType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NoEntryCalculator extends AbstractTimeWarningCalculationStrategy {

    public List<TimeWarning> calculate(@NotNull List<ProjectEntry> projectEntries, @NotNull List<FehlzeitType> absenceEntries) {
        if (projectEntries.isEmpty()) {
            TimeWarning timeWarning = new TimeWarning();
            timeWarning.getWarningTypes().add(TimeWarningType.EMPTY_ENTRY_LIST);
            List<TimeWarning> timeWarnings = new ArrayList<>();
            timeWarnings.add(timeWarning);

            return timeWarnings;
        }

        List<LocalDate> businessDays = getBusinessDaysOfMonth(projectEntries.get(0).getDate().getYear(), projectEntries.get(0).getDate().getMonth().getValue());
        List<LocalDate> compensatoryDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.COMPENSATORY_DAYS.getType(), absenceEntries);
        List<LocalDate> vacationDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.VACATION_DAYS.getType(), absenceEntries);
        List<LocalDate> sicknessDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.SICKNESS_DAYS.getType(), absenceEntries);
        List<LocalDate> nursingDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.NURSING_DAYS.getType(), absenceEntries);
        List<LocalDate> maternityLeaveDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.MATERNITY_LEAVE_DAYS.getType(), absenceEntries);
        List<LocalDate> externalTrainingDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.EXTERNAL_TRAINING_DAYS.getType(), absenceEntries);
        List<LocalDate> conferenceDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.CONFERENCE_DAYS.getType(), absenceEntries);
        List<LocalDate> maternityProtectionDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.MATERNITY_PROTECTION_DAYS.getType(), absenceEntries);
        List<LocalDate> fatherMonthDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.FATHER_MONTH_DAYS.getType(), absenceEntries);
        List<LocalDate> paidSpecialLeaveDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.PAID_SPECIAL_LEAVE_DAYS.getType(), absenceEntries);
        List<LocalDate> nonPaidVacationDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.NON_PAID_VACATION_DAYS.getType(), absenceEntries);
        List<LocalDate> bookedDays = projectEntries.stream()
                .map(ProjectEntry::getDate)
                .collect(Collectors.toList());

        return businessDays.stream()
                .filter(date -> !compensatoryDays.contains(date))
                .filter(date -> !vacationDays.contains(date))
                .filter(date -> !sicknessDays.contains(date))
                .filter(date -> !bookedDays.contains(date))
                .filter(date -> !nursingDays.contains(date))
                .filter(date -> !maternityLeaveDays.contains(date))
                .filter(date -> !externalTrainingDays.contains(date))
                .filter(date -> !conferenceDays.contains(date))
                .filter(date -> !maternityProtectionDays.contains(date))
                .filter(date -> !fatherMonthDays.contains(date))
                .filter(date -> !paidSpecialLeaveDays.contains(date))
                .filter(date -> !nonPaidVacationDays.contains(date))
                .map(this::createTimeWarning)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<LocalDate> getBusinessDaysOfMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return OfficeCalendarUtil.getWorkingDaysBetween(startDate, endDate);
    }

    private TimeWarning createTimeWarning(final LocalDate date) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(date);
        timeWarning.getWarningTypes().add(TimeWarningType.NO_TIME_ENTRY);

        return timeWarning;
    }

    private List<LocalDate> filterAbsenceTypesAndCompileLocalDateList(String type, List<FehlzeitType> absenceEntries) {
        return absenceEntries.stream()
                .filter(fzt -> fzt.getFehlgrund().equals(type))
                .flatMap(this::extractFehlzeitenDateRange)
                .collect(Collectors.toList());
    }

    private Stream<LocalDate> extractFehlzeitenDateRange(FehlzeitType fzt) {
        return LocalDate.parse(fzt.getStartdatum()).datesUntil(LocalDate.parse(fzt.getEnddatum()).plusDays(1));
    }
}
