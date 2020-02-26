package com.gepardec.mega.communication.dates;

import com.gepardec.mega.communication.Reminder;
import org.slf4j.Logger;

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
    Logger logger;

    public Optional<Reminder> getEventForDate(LocalDate actualDate) {

        logger.info("starting getEventForDate with date {}", actualDate.toString());
        LocalDate firstWorkingDayOfMonth = calcFirstWorkingDayOfMonthForDate(actualDate);
        if (actualDate.isEqual(firstWorkingDayOfMonth)) {
            logReminder(EMPLOYEE_CHECK_PROJECTTIME);
            return Optional.of(EMPLOYEE_CHECK_PROJECTTIME);
        }

        LocalDate userCheckContentWorkingDay = addWorkingdays(firstWorkingDayOfMonth, OM_CONTROL_EMPLOYEES_CONTENT.getWorkingDay() - EMPLOYEE_CHECK_PROJECTTIME.getWorkingDay());
        if (actualDate.isEqual(userCheckContentWorkingDay)) {
            logReminder(OM_CONTROL_EMPLOYEES_CONTENT);
            return Optional.of(OM_CONTROL_EMPLOYEES_CONTENT);
        }

        LocalDate plControllingDay = addWorkingdays(firstWorkingDayOfMonth, PL_PROJECT_CONTROLLING.getWorkingDay() - EMPLOYEE_CHECK_PROJECTTIME.getWorkingDay());
        if (actualDate.isEqual(plControllingDay)) {
            logReminder(PL_PROJECT_CONTROLLING);
            return Optional.of(PL_PROJECT_CONTROLLING);
        }

        LocalDate releaseWorkingDay = addWorkingdays(firstWorkingDayOfMonth, OM_RELEASE.getWorkingDay() - EMPLOYEE_CHECK_PROJECTTIME.getWorkingDay());
        if (actualDate.isEqual(releaseWorkingDay)) {
            logReminder(OM_RELEASE);
            return Optional.of(OM_RELEASE);
        }

        LocalDate salaryChargingWorkingDay = addWorkingdays(firstWorkingDayOfMonth, OM_ADMINISTRATIVE.getWorkingDay() - EMPLOYEE_CHECK_PROJECTTIME.getWorkingDay());
        if (actualDate.isEqual(salaryChargingWorkingDay)) {
            logReminder(OM_ADMINISTRATIVE);
            return Optional.of(OM_ADMINISTRATIVE);
        }

        LocalDate salaryTransferWorkingDay = removeWorkingdaysFromNextMonth(actualDate, OM_SALARY.getWorkingDay());
        if (actualDate.isEqual(salaryTransferWorkingDay)) {
            logReminder(OM_SALARY);
            return Optional.of(OM_SALARY);
        }
        return Optional.empty();
    }

    private void logReminder(Reminder reminder) {
        logger.info("{}. workingday of month was calculated: - Event: {}", reminder.getWorkingDay(), reminder.name());
    }


    private LocalDate calcFirstWorkingDayOfMonthForDate(LocalDate actual) {

        LocalDate firstWorkingDayOfMonth = actual.with(DAY_OF_MONTH, 1);

        while (!isWorkingDay(firstWorkingDayOfMonth)) {
            firstWorkingDayOfMonth = firstWorkingDayOfMonth.plusDays(1);
        }
        return firstWorkingDayOfMonth;
    }


    private LocalDate addWorkingdays(LocalDate date, int workdaysToAdd) {
        if (workdaysToAdd < 1) {
            return date;
        }
        LocalDate resultDate = date;
        int addedDays = 0;
        while (addedDays < workdaysToAdd) {
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
        int removedDays = 0;
        while (removedDays < workingdaysToRemove) {
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
                && !AustrianHolidays.getHolidates().contains(date);
    }
}
