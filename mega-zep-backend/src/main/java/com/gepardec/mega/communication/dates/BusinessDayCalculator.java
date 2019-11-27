package com.gepardec.mega.communication.dates;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BusinessDayCalculator {

    @Inject
    AustrianHolidays austrianHolidays;

    public LocalDate calculateFirstWorkingDayForMonth(LocalDateTime actal) {
        return null;
    }


    private LocalDate addWorkingdays(LocalDate date, int workdaysToAdd) {
        if (workdaysToAdd < 1) {
            return date;
        }
        LocalDate resultDate = date;
        for (int addedDays = 0; addedDays < workdaysToAdd; ) {
            resultDate = resultDate.plusDays(1);
            if (resultDate.getDayOfWeek() != DayOfWeek.SATURDAY
                    && resultDate.getDayOfWeek() != DayOfWeek.SUNDAY
                    && !austrianHolidays.getHolidays().contains(resultDate)) {
                ++addedDays;
            }
        }
        return resultDate;
    }
}
