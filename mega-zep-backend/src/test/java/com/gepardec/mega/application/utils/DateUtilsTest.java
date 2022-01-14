package com.gepardec.mega.application.utils;

import com.gepardec.mega.domain.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateUtilsTest {

    @Test
    void getLastDayOfFollowingMonth_normalDate_returnLastDayOfMonth() {
        assertThat(DateUtils.getLastDayOfFollowingMonth("2017-11-10")).isEqualTo("2017-12-31");
    }

    @Test
    void getLastDayOfFollowingMonth_decemberDate_returnLastDayOfMonthJanuary() {
        assertThat(DateUtils.getLastDayOfFollowingMonth("2017-12-01")).isEqualTo("2018-01-31");
    }

    @Test
    void getLastDayOfFollowingMonth_nullString_throwException() {
        assertThatThrownBy(() -> DateUtils.getLastDayOfFollowingMonth(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void getLastDayOfFollowingMonth_emptyDate_returnLastDayOfMonthJanuary() {
        assertThatThrownBy(() -> DateUtils.getLastDayOfFollowingMonth("")).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void getFirstDayOfFollowingMonth_normalDate_returnFirstDayOfNextMonth() {
        assertThat(DateUtils.getFirstDayOfFollowingMonth("2019-01-31")).isEqualTo("2019-02-01");
    }

    @Test
    void getFirstDayOfFollowingMonth_decemberDate_returnJanuaryDate() {
        assertThat(DateUtils.getFirstDayOfFollowingMonth("2019-12-01")).isEqualTo("2020-01-01");
    }

    @Test
    void getFirstDayOfFollowingMonth_nullString_throwException() {
        assertThatThrownBy(() -> DateUtils.getFirstDayOfFollowingMonth(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void getFirstDayOfFollowingMonth_emptyDate_returnLastDayOfMonthJanuary() {
        assertThatThrownBy(() -> DateUtils.getLastDayOfFollowingMonth("")).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void toDateTime_correctInput_shouldReturnDateTime() {
        assertThat(DateUtils.parseDateTime("2019-11-18", "10:11")).isEqualTo(LocalDateTime.of(2019, 11, 18, 10, 11));
    }

    @Test
    void toDateTime_emptyDate_shouldThrowException() {
        assertThatThrownBy(() -> DateUtils.parseDateTime("", "10:11")).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void toDateTime_emptyTime_shouldThrowException() {
        assertThatThrownBy(() -> DateUtils.parseDateTime("2019-11-18", "")).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void calcDiffInHours_correctInput_shouldReturnHours() {
        double diffInHours = DateUtils.calcDiffInHours(LocalDateTime.of(2019, 11, 18, 10, 15), LocalDateTime.of(2019, 11, 18, 19, 0));
        assertThat(diffInHours).isEqualTo(8.75d);
    }

    @Test
    void getDayByDate_normalInput_shouldRetornCorrectDay() {
        assertThat(DateUtils.getDayByDate(LocalDate.of(2019, 11, 18))).isEqualTo("Montag");
    }
}
