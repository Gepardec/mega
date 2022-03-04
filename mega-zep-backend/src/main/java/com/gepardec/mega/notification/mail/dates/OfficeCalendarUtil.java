package com.gepardec.mega.notification.mail.dates;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OfficeCalendarUtil {

    private static final HolidayManager HOLIDAY_MANAGER = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.AUSTRIA));
    private static final Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    private static final Predicate<LocalDate> isHoliday = OfficeCalendarUtil::isHoliday;

    public static List<LocalDate> getWorkingDaysBetween(LocalDate startDate, LocalDate endDateInclusive) {
        return startDate.datesUntil(endDateInclusive.plusDays(1))
                .filter(isWeekend.or(isHoliday).negate())
                .collect(Collectors.toList());
    }

    public static boolean isHoliday(LocalDate date) {
        return HOLIDAY_MANAGER.isHoliday(date);
    }

    public static boolean isWorkingDay(LocalDate date) {
        return isWeekend.or(isHoliday).negate().test(date);
    }

    public static Stream<LocalDate> getHolidaysForYear(int year) {
        return HOLIDAY_MANAGER.getHolidays(year).stream().map(Holiday::getDate);
    }
}
