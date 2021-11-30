package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;
import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.provantis.zep.FehlzeitType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NoEntryCalculator extends AbstractTimeWarningCalculationStrategy {
    //TODO
    public List<TimeWarning> calculate(List<ProjectEntry> projectTimeEntries, List<FehlzeitType> absenceEntries) {
        final List<TimeWarning> warnings = new ArrayList<>();
        List<LocalDate> businessDays = getBusinessDaysOfMonth(projectTimeEntries.get(0).getDate().getYear(), projectTimeEntries.get(0).getDate().getMonth().getValue());

        businessDays.forEach(date -> {
            projectTimeEntries.forEach(projectTimeEntry -> {
                if (!date.equals(projectTimeEntry.getDate())) {
                    warnings.add(createTimeWarning(date));
                }
            });
        });

        return warnings;
    }
    //TODO change to private
    public List<LocalDate> getBusinessDaysOfMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, daysInMonth);
        return getBusinessDays(startDate, endDate);
    }
    //TODO change to private
    public List<LocalDate> getBusinessDays(LocalDate startDate, LocalDate endDate) {
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
        Predicate<LocalDate> isHoliday = this::isHoliday;
        endDate = endDate.plusDays(1);
        return startDate.datesUntil(endDate)
                .filter(isWeekend.or(isHoliday).negate())
                .collect(Collectors.toList());
    }
    //TODO change to private
    public boolean isHoliday(LocalDate date) {
        HolidayManager holidayManager = HolidayManager.getInstance(HolidayCalendar.AUSTRIA);
        return holidayManager.isHoliday(date);
    }

    private TimeWarning createTimeWarning(final LocalDate date) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(date);
        timeWarning.getWarningTypes().add(TimeWarningType.NO_TIME_ENTRY);

        return timeWarning;
    }
    //TODO change to private
    public List<LocalDate> convertAbsenceEntriesToDates(List<FehlzeitType> absenceEntries) {
        List<LocalDate> absenceDates = new ArrayList<>();
        absenceEntries.forEach(fzt -> {
            LocalDate startDate = LocalDate.parse(fzt.getStartdatum());
            LocalDate endDate = LocalDate.parse(fzt.getEnddatum());


        });

        return absenceDates;
    }

    public List<LocalDate> getLocalDateList(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> list = new ArrayList<>();
        for (int i = startDate.getDayOfMonth(); i <= endDate.getDayOfMonth(); i++) {

        }
    }
}
