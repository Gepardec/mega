package com.gepardec.mega.domain.calculation.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoEntryCalculatorTest {
    NoEntryCalculator noEntryCalculator;

    @BeforeEach
    void init() {
        noEntryCalculator = new NoEntryCalculator();
    }


    @Test
    void isHoliday_whenDayIsHoliday_thenReturnsTrue() {
        LocalDate holidayDate = LocalDate.of(2021, 1, 1);
        boolean result = noEntryCalculator.isHoliday(holidayDate);

        assertTrue(result);
    }

    @Test
    void isHoliday_whenDayIsntHoliday_thenReturnsFalse() {
        LocalDate holidayDate = LocalDate.of(2021, 1, 2);
        boolean result = noEntryCalculator.isHoliday(holidayDate);

        assertFalse(result);
    }

    @Test
    void getBusinessDays_whenTwoWeeks_thenReturnsListWithoutWeekends() {
        LocalDate startDate = LocalDate.of(2021, 2, 1);
        LocalDate endDate = LocalDate.of(2021, 2, 15);
        List<LocalDate> result = noEntryCalculator.getBusinessDays(startDate, endDate);

        List<LocalDate> expected = getLocalDateList(2, 1, 5);
        expected.addAll(getLocalDateList(2, 8, 12));
        expected.addAll(getLocalDateList(2, 15, 15));

        assertEquals(expected, result);
    }

    @Test
    void getBusinessDays_whenTwoWeeksAndHolidays_thenReturnsListWithoutWeekendsAndHoliDays() {
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 1, 15);
        List<LocalDate> result = noEntryCalculator.getBusinessDays(startDate, endDate);


        List<LocalDate> expected = getLocalDateList(1,4, 5);
        expected.addAll(getLocalDateList(1, 7, 8));
        expected.addAll(getLocalDateList(1, 11, 15));

        assertEquals(expected, result);
    }

    private List<LocalDate> getLocalDateList(int month, int from, int to) {
        List<LocalDate> list = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            list.add(LocalDate.of(2021, month, i));
        }

        return list;
    }
}
