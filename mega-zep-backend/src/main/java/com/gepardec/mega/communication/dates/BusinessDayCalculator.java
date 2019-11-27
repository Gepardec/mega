package com.gepardec.mega.communication.dates;

import com.gepardec.mega.communication.Reminder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import static com.gepardec.mega.communication.Reminder.*;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;

@ApplicationScoped
public class BusinessDayCalculator {

    @Inject
    AustrianHolidays austrianHolidays;

    public Optional<Reminder> getEventForDate(LocalDate actualDate) {
        LocalDate firstWorkingDayOfMonth = calcFirstWorkingDayOfMonthForDate(actualDate);
        if (actualDate.isEqual(firstWorkingDayOfMonth)) {
            return Optional.of(USER_CHECK_PROJECTTIMES);
        }

        LocalDate userCheckContentWorkingDay = addWorkingdays(firstWorkingDayOfMonth, OM_CHECK_USER_CONTENT.getWorkingDay() - USER_CHECK_PROJECTTIMES.getWorkingDay());
        if (actualDate.isEqual(userCheckContentWorkingDay)) {
            return Optional.of(OM_CHECK_USER_CONTENT);
        }
        LocalDate releaseWorkingDay = addWorkingdays(firstWorkingDayOfMonth, OM_RELEASE.getWorkingDay() - USER_CHECK_PROJECTTIMES.getWorkingDay());
        if (actualDate.isEqual(releaseWorkingDay)) {
            return Optional.of(OM_RELEASE);
        }

        LocalDate salaryChargingWorkingDay = addWorkingdays(firstWorkingDayOfMonth, OM_SALARY_CHARGING.getWorkingDay() - USER_CHECK_PROJECTTIMES.getWorkingDay());
        if (actualDate.isEqual(salaryChargingWorkingDay)) {
            return Optional.of(OM_SALARY_CHARGING);
        }

        LocalDate salaryTransferWorkingDay = removeWorkingdaysFromNextMonth(actualDate, OM_SALARY_TRANSFER.getWorkingDay());
        if (actualDate.isEqual(salaryTransferWorkingDay)) {
            return Optional.of(OM_SALARY_TRANSFER);
        }
        return Optional.empty();
    }


    private LocalDate calcFirstWorkingDayOfMonthForDate(LocalDate actual) {
        LocalDate firstDayOfMonth = actual.with(DAY_OF_MONTH, 1);

        LocalDate firstWorkingDayOfMonth = firstDayOfMonth;
        while (!isWorkingDay(firstDayOfMonth)) {
            firstDayOfMonth = firstDayOfMonth.plusDays(1);
        }
        return firstDayOfMonth;
    }


    private LocalDate addWorkingdays(LocalDate date, int workdaysToAdd) {
        if (workdaysToAdd < 1) {
            return date;
        }
        LocalDate resultDate = date;
        for (int addedDays = 0; addedDays < workdaysToAdd; ) {
            resultDate = resultDate.plusDays(1);
            if (isWorkingDay(resultDate)) {
                ++addedDays;
            }
        }
        return resultDate;
    }


    private LocalDate removeWorkingdaysFromNextMonth(LocalDate date, int workingdaysToRemove) {
        if (workingdaysToRemove < 0) {
            workingdaysToRemove *= -1;
        }
        LocalDate resultDate = date.with(TemporalAdjusters.firstDayOfNextMonth());
        for (int removedDays = 0; removedDays < workingdaysToRemove; ) {
            resultDate = resultDate.minusDays(1);
            if (isWorkingDay(resultDate)) {
                ++removedDays;
            }
        }
        return resultDate;
    }


    private boolean isWorkingDay(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY
                && date.getDayOfWeek() != DayOfWeek.SUNDAY
                && !austrianHolidays.getHolidates().contains(date);
    }
}
