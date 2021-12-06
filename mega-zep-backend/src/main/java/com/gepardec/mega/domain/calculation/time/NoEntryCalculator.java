package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.AbsenteeType;
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

    public List<TimeWarning> calculate(@NotNull List<ProjectEntry> projectEntries, @NotNull List<FehlzeitType> absenceEntries) {
        if (projectEntries.isEmpty()) {
            TimeWarning timeWarning = new TimeWarning();
            timeWarning.getWarningTypes().add(TimeWarningType.EMPTY_ENTRY_LIST);
            List<TimeWarning> timeWarnings = new ArrayList<>();
            timeWarnings.add(timeWarning);

            return timeWarnings;
        }
        Set<TimeWarning> warnings = new HashSet<>();
        List<LocalDate> datesWithBookings = new ArrayList<>();

        List<LocalDate> businessDays = getBusinessDaysOfMonth(projectEntries.get(0).getDate().getYear(), projectEntries.get(0).getDate().getMonth().getValue());
        List<LocalDate> compensatoryDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.COMPENSATORY_DAYS.getType(), absenceEntries);
        List<LocalDate> vacationDays = filterAbsenceTypesAndCompileLocalDateList(AbsenteeType.VACATION_DAYS.getType(), absenceEntries);

        businessDays.removeAll(compensatoryDays);
        businessDays.removeAll(vacationDays);

        projectEntries.forEach(projectTimeEntry -> businessDays.forEach(date -> {
            if(date.equals(projectTimeEntry.getDate())) {
                datesWithBookings.add(date);
            }
        }));

        businessDays.removeAll(datesWithBookings);
        businessDays.forEach(date -> {
            warnings.add(createTimeWarning(date));
        });

        return new ArrayList<>(warnings);
    }

    private List<LocalDate> getBusinessDaysOfMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, daysInMonth);
        return getBusinessDays(startDate, endDate);
    }

    private List<LocalDate> getBusinessDays(LocalDate startDate, LocalDate endDate) {
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
        Predicate<LocalDate> isHoliday = this::isHoliday;
        endDate = endDate.plusDays(1);
        return startDate.datesUntil(endDate)
                .filter(isWeekend.or(isHoliday).negate())
                .collect(Collectors.toList());
    }

    private boolean isHoliday(LocalDate date) {
        HolidayManager holidayManager = HolidayManager.getInstance(HolidayCalendar.AUSTRIA);
        return holidayManager.isHoliday(date);
    }

    private TimeWarning createTimeWarning(final LocalDate date) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(date);
        timeWarning.getWarningTypes().add(TimeWarningType.NO_TIME_ENTRY);

        return timeWarning;
    }

    private List<LocalDate> filterAbsenceTypesAndCompileLocalDateList(String type, List<FehlzeitType> absenceEntries) {
        List<LocalDate> compensatoryDays = new ArrayList<>();

        if(!absenceEntries.isEmpty()) {
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
        }

        return compensatoryDays;
    }
}
