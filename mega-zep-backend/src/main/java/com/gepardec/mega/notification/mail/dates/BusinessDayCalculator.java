package com.gepardec.mega.notification.mail.dates;

import com.gepardec.mega.notification.mail.Mail;
import com.gepardec.mega.notification.mail.MailType;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.gepardec.mega.notification.mail.dates.OfficeCalendarUtil.isWorkingDay;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

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
        LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        return Stream.iterate(firstDayOfMonth, theDate -> theDate.plusDays(1))
                .dropWhile(theDate -> !isWorkingDay(theDate))
                .findFirst()
                .orElse(firstDayOfMonth);
    }

    LocalDate addWorkingdays(LocalDate date, int workdaysToAdd) {
        if (workdaysToAdd < 1) {
            return date;
        }

        // has to be the next day of the given date, because we want to ignore the given date
        LocalDate result = date.plusDays(1);
        return Stream.iterate(result, d -> d.plusDays(1))
                .filter(OfficeCalendarUtil::isWorkingDay)
                .limit(workdaysToAdd)
                .max(Comparator.naturalOrder())
                .orElse(result);
    }

    LocalDate removeWorkingdaysFromNextMonth(LocalDate date, int workingdaysToRemove) {
        // has to be the last day of given month, because we want to ignore the "seed" value of the stream
        LocalDate result = date.with(lastDayOfMonth());
        workingdaysToRemove = Math.abs(workingdaysToRemove);
        return Stream.iterate(result, d -> d.minusDays(1))
                .filter(OfficeCalendarUtil::isWorkingDay)
                .limit(workingdaysToRemove)
                .min(Comparator.naturalOrder())
                .orElse(result);
    }
}
