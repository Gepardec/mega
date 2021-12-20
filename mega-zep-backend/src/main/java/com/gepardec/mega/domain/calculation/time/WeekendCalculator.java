package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WeekendCalculator extends AbstractTimeWarningCalculationStrategy implements WarningCalculationStrategy<TimeWarning> {
    @Override
    public List<TimeWarning> calculate(List<ProjectEntry> projectTimeEntries) {
        List<TimeWarning> warnings = new ArrayList<>();
        if (projectTimeEntries.isEmpty()) {
            return new ArrayList<>();
        } else {
            int year = projectTimeEntries.get(0).getDate().getYear();
            int month = projectTimeEntries.get(0).getDate().getMonth().getValue();

            List<LocalDate> weekendDays = getWeekEndDaysOfMonth(year, month);
            projectTimeEntries.forEach(projectTimeEntry -> {
                if(isWeekEndDay(projectTimeEntry.getDate(), weekendDays)) {
                    warnings.add(createTimeWarning(projectTimeEntry.getDate()));
                }
            });
        }

        return warnings;
    }

    private List<LocalDate> getWeekEndDaysOfMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, daysInMonth);
        return getWeekEndDays(startDate, endDate);
    }

    private List<LocalDate> getWeekEndDays(LocalDate startDate, LocalDate endDate) {
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
        endDate = endDate.plusDays(1);
        return startDate.datesUntil(endDate)
                .filter(isWeekend)
                .collect(Collectors.toList());
    }

    private boolean isWeekEndDay(LocalDate date, List<LocalDate> listOfWeekendDays) {
        return listOfWeekendDays.contains(date);
    }

    private TimeWarning createTimeWarning(final LocalDate date) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(date);
        timeWarning.getWarningTypes().add(TimeWarningType.WEEKEND);

        return timeWarning;
    }
}
