package com.gepardec.mega.notification.mail.dates;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class OfficeCalendarUtilTest {

    private static final Set<LocalDate> holidates = Set.of(
            LocalDate.of(2022, 1, 1),
            LocalDate.of(2022, 1, 6),
            LocalDate.of(2022, 4, 17),
            LocalDate.of(2022, 4, 18),
            LocalDate.of(2022, 5, 1),
            LocalDate.of(2022, 5, 26),
            LocalDate.of(2022, 6, 6),
            LocalDate.of(2022, 6, 16),
            LocalDate.of(2022, 8, 15),
            LocalDate.of(2022, 10, 26),
            LocalDate.of(2022, 11, 1),
            LocalDate.of(2022, 12, 8),
            LocalDate.of(2022, 12, 24),
            LocalDate.of(2022, 12, 25),
            LocalDate.of(2022, 12, 26),
            LocalDate.of(2022, 12, 31)
    );

    @Test
    void doesHolidayApiReturnCorrectHolidays() {
        List<LocalDate> actual = OfficeCalendarUtil.getHolidaysForYear(2022).collect(Collectors.toList());

        assertThat(actual).containsExactlyInAnyOrderElementsOf(holidates);
    }
}
