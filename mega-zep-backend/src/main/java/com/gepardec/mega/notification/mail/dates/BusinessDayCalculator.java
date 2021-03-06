package com.gepardec.mega.notification.mail.dates;

import com.gepardec.mega.notification.mail.Mail;
import com.gepardec.mega.notification.mail.MailType;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;

@ApplicationScoped
public class BusinessDayCalculator {

    @Inject
    Logger logger;

    public Optional<Mail> getEventForDate(LocalDate actualDate) {

        logger.info("starting getEventForDate with date {}", actualDate);

        Map<LocalDate, Mail> reminderByDate = new HashMap<>(0);
        LocalDate firstWorkingDayOfMonth = calcFirstWorkingDayOfMonthForDate(actualDate);

        Arrays.stream(Mail.values())
                .forEach(reminder -> reminderByDate.put(calcDateForReminder(firstWorkingDayOfMonth, reminder), reminder));

        Optional<Mail> relevantReminder = Optional.ofNullable(reminderByDate.get(actualDate));
        relevantReminder.ifPresent(reminder -> logger.info("Reminder {} was calculated", reminder.name()));

        return relevantReminder;
    }

    private LocalDate calcDateForReminder(LocalDate firstWorkingdayOfMonth, Mail mail) {
        if (mail.getType() == MailType.DAY_OF_MONTH_BASED) {
            return calcNextWorkingdayForDayOfMonth(firstWorkingdayOfMonth, mail.getDay());
        } else if (mail.getType() == MailType.WORKING_DAY_BASED) {
            if (mail.getDay() > 0) {
                return addWorkingdays(firstWorkingdayOfMonth, mail.getDay() - 1);
            } else {
                return removeWorkingdaysFromNextMonth(firstWorkingdayOfMonth, mail.getDay());
            }
        } else {
            return LocalDate.MIN;
        }
    }

    private LocalDate calcNextWorkingdayForDayOfMonth(LocalDate actualDate, int dayOfMonth) {
        LocalDate dayOfMonthDate = actualDate.withDayOfMonth(dayOfMonth);
        if (isWorkingDay(dayOfMonthDate)) {
            return dayOfMonthDate;
        } else {
            return addWorkingdays(dayOfMonthDate, 1);
        }
    }

    private LocalDate calcFirstWorkingDayOfMonthForDate(LocalDate date) {

        LocalDate firstWorkingDayOfMonth = date.with(DAY_OF_MONTH, 1);

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
