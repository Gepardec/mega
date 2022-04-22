package com.gepardec.mega.domain.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfNextMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public final class DateUtils {

    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm", Locale.getDefault());
    private static final Locale GERMAN_LOCALE = Locale.GERMANY;

    private DateUtils() {
    }

    public static LocalDate parseDate(String dateAsString) {
        return LocalDate.parse(dateAsString, DEFAULT_DATE_FORMATTER);
    }

    public static String formatDate(LocalDate date) {
        return date.format(DEFAULT_DATE_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateAsString, String timeAsString) {
        return LocalDateTime.parse(dateAsString + timeAsString, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * when we have to secure our tests with specific time, we could set it here
     *
     * @return
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDate getFirstDayOfFollowingMonth(String dateAsString) {
        Objects.requireNonNull(dateAsString, "Date must not be null!");
        return parseDate(dateAsString).with(firstDayOfNextMonth());
    }

    public static String getFirstDayOfFollowingMonth(LocalDate date) {
        Objects.requireNonNull(date, "Date must not be null!");
        return formatDate(date.with(firstDayOfNextMonth()));
    }

    public static LocalDate getFirstDayOfCurrentMonth(String dateAsString) {
        Objects.requireNonNull(dateAsString, "Date must not be null!");
        return parseDate(dateAsString).with(firstDayOfMonth());
    }

    public static String getFirstDayOfCurrentMonth(LocalDate date) {
        Objects.requireNonNull(date, "Date must not be null!");
        return formatDate(date.with(firstDayOfMonth()));
    }

    public static LocalDate getLastDayOfFollowingMonth(String dateAsString) {
        Objects.requireNonNull(dateAsString, "Date must not be null!");
        return parseDate(dateAsString).plusMonths(1).with(lastDayOfMonth());
    }

    public static String getLastDayOfFollowingMonth(LocalDate date) {
        Objects.requireNonNull(date, "Date must not be null!");
        return formatDate(date.plusMonths(1).with(lastDayOfMonth()));
    }

    public static LocalDate getLastDayOfCurrentMonth(String dateAsString) {
        Objects.requireNonNull(dateAsString, "Date must not be null!");
        return parseDate(dateAsString).with(lastDayOfMonth());
    }

    public static String getLastDayOfCurrentMonth(LocalDate date) {
        Objects.requireNonNull(date, "Date must not be null!");
        return formatDate(date.with(lastDayOfMonth()));
    }

    public static double calcDiffInHours(LocalDateTime from, LocalDateTime to) {
        return Duration.between(from, to).toMinutes() / 60.0;
    }

    public static String getDayByDate(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, GERMAN_LOCALE);
    }
}
