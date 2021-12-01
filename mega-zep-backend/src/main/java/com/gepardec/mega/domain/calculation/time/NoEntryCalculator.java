package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.provantis.zep.FehlzeitType;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NoEntryCalculator extends AbstractTimeWarningCalculationStrategy {

    public List<TimeWarning> calculate(@NotNull List<ProjectEntry> projectTimeEntries, @NotNull List<FehlzeitType> absenceEntries) {
        if (projectTimeEntries.isEmpty()) {
            TimeWarning timeWarning = new TimeWarning();
            timeWarning.getWarningTypes().add(TimeWarningType.EMPTY_ENTRY_LIST);
            List<TimeWarning> timeWarnings = new ArrayList<>();
            timeWarnings.add(timeWarning);

            return timeWarnings;
        } else {
            Set<TimeWarning> warnings = new HashSet<>();
            List<LocalDate> datesWithBookings = new ArrayList<>();
            List<LocalDate> businessDays = getBusinessDaysOfMonth(projectTimeEntries.get(0).getDate().getYear(), projectTimeEntries.get(0).getDate().getMonth().getValue());
            //create lists of all compensatory and vacation days
            List<LocalDate> compensatoryDays = filterAbscenceTypesAndCompileLocalDateList("FA", absenceEntries);
            List<LocalDate> vacationDays = filterAbscenceTypesAndCompileLocalDateList("UB", absenceEntries);

            //remove compensatoryDays and vacationDays from businessDays list
            businessDays.removeAll(compensatoryDays);
            businessDays.removeAll(vacationDays);

            //check business days against days which have booking entries
            projectTimeEntries.forEach(projectTimeEntry -> {
                businessDays.forEach(date -> {
                    if(date.equals(projectTimeEntry.getDate())) {
                        datesWithBookings.add(date);
                    }
                });
            });

            //remove dates with booking entries
            businessDays.removeAll(datesWithBookings);
            //create warnings for remaining business days
            businessDays.forEach(date -> {
                warnings.add(createTimeWarning(date));
            });

            return new ArrayList<>(warnings);
        }

    }

    private List<LocalDate> getBusinessDaysOfMonth(@NotNull int year, @NotNull int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, daysInMonth);
        return getBusinessDays(startDate, endDate);
    }

    private List<LocalDate> getBusinessDays(@NotNull LocalDate startDate, @NotNull LocalDate endDate) {
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
        Predicate<LocalDate> isHoliday = this::isHoliday;
        endDate = endDate.plusDays(1);
        return startDate.datesUntil(endDate)
                .filter(isWeekend.or(isHoliday).negate())
                .collect(Collectors.toList());
    }

    private boolean isHoliday(@NotNull LocalDate date) {
        HolidayManager holidayManager = HolidayManager.getInstance(HolidayCalendar.AUSTRIA);
        return holidayManager.isHoliday(date);
    }

    private TimeWarning createTimeWarning(final LocalDate date) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(date);
        timeWarning.getWarningTypes().add(TimeWarningType.NO_TIME_ENTRY);

        return timeWarning;
    }

    private List<LocalDate> convertAbsenceEntriesToDates(@NotNull List<FehlzeitType> absenceEntries) {
        List<LocalDate> absenceDates = new ArrayList<>();
        absenceEntries.forEach(fzt -> {
            LocalDate startDate = LocalDate.parse(fzt.getStartdatum());
            LocalDate endDate = LocalDate.parse(fzt.getEnddatum());
        });

        return absenceDates;
    }

    private List<LocalDate> filterAbscenceTypesAndCompileLocalDateList(@NotNull String type, @NotNull List<FehlzeitType> absenceEntries) {
        List<LocalDate> compensatoryDays = new ArrayList<>();
        absenceEntries.stream()
                .filter(fzt -> fzt.getFehlgrund().equals(type))
                .forEach(fzt -> {
                    if (fzt.getStartdatum().equals(fzt.getEnddatum())) {
                        LocalDate date = LocalDate.parse(fzt.getStartdatum());
                        compensatoryDays.add(date);
                    } else {
                        LocalDate startDate = LocalDate.parse(fzt.getStartdatum());
                        LocalDate endDate = LocalDate.parse(fzt.getEnddatum());
                        List<LocalDate> datesBetweenStartAndEnd = Stream.iterate(startDate, date -> date.plusDays(1))
                                .limit(ChronoUnit.DAYS.between(startDate, endDate))
                                .collect(Collectors.toList());
                        compensatoryDays.add(startDate);
                        compensatoryDays.addAll(datesBetweenStartAndEnd);
                        compensatoryDays.add(endDate);
                    }
                });

        return compensatoryDays;
    }
}
