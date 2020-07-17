package com.gepardec.mega.application.utils;

import com.gepardec.mega.domain.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateUtilsTest {

    @Test
    void getLastDayOfFollowingMonth_normalDate_returnLastDayOfMonth() {
        assertEquals("2017-12-31", DateUtils.getLastDayOfFollowingMonth("2017-11-10"));
    }

    @Test
    void getLastDayOfFollowingMonth_decemberDate_returnLastDayOfMonthJanuary() {
        assertEquals("2018-01-31", DateUtils.getLastDayOfFollowingMonth("2017-12-01"));
    }

    @Test
    void getLastDayOfFollowingMonth_nullString_throwException() {
        assertThrows(NullPointerException.class, () -> DateUtils.getLastDayOfFollowingMonth(null));
    }

    @Test
    void getLastDayOfFollowingMonth_emptyDate_returnLastDayOfMonthJanuary() {
        assertThrows(DateTimeParseException.class, () -> DateUtils.getLastDayOfFollowingMonth(""));
    }


    @Test
    void getFirstDayOfFollowingMonth_normalDate_returnFirstDayOfNextMonth() {
        assertEquals("2019-02-01", DateUtils.getFirstDayOfFollowingMonth("2019-01-31"));
    }

    @Test
    void getFirstDayOfFollowingMonth_decemberDate_returnJanuaryDate() {
        assertEquals("2020-01-01", DateUtils.getFirstDayOfFollowingMonth("2019-12-01"));
    }


    @Test
    void getFirstDayOfFollowingMonth_nullString_throwException() {
        assertThrows(NullPointerException.class, () -> DateUtils.getFirstDayOfFollowingMonth(null));
    }

    @Test
    void getFirstDayOfFollowingMonth_emptyDate_returnLastDayOfMonthJanuary() {
        assertThrows(DateTimeParseException.class, () -> DateUtils.getLastDayOfFollowingMonth(""));
    }

    @Test
    void toDateTime_correctInput_shouldReturnDateTime() {
        assertEquals(LocalDateTime.of(2019, 11, 18, 10, 11), DateUtils.parseDateTime("2019-11-18", "10:11"));
    }

    @Test
    void toDateTime_emptyDate_shouldThrowException() {
        assertThrows(DateTimeParseException.class, () -> DateUtils.parseDateTime("", "10:11"));
    }

    @Test
    void toDateTime_emptyTime_shouldThrowException() {
        assertThrows(DateTimeParseException.class, () -> DateUtils.parseDateTime("2019-11-18", ""));
    }

    @Test
    void calcDiffInHours_correctInput_shouldReturnHours() {
        assertEquals(8.75d, DateUtils.calcDiffInHours(LocalDateTime.of(2019, 11, 18, 10, 15), LocalDateTime.of(2019, 11, 18, 19, 0)));
    }

    @Test
    void getDayByDate_normalInput_shouldRetornCorrectDay() {
        assertEquals("Montag", DateUtils.getDayByDate(LocalDate.of(2019,11,18)));
    }

}
