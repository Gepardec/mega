package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExceededMaximumWorkingHoursPerDayTimeWarningCalculatorTest {

    private ExceededMaximumWorkingHoursPerDayTimeWarningCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new ExceededMaximumWorkingHoursPerDayTimeWarningCalculator();
    }

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {
        assertTrue(calculator.calculate(Collections.emptyList()).isEmpty());
    }

    @Test
    void calculate_whenValidWorkingHoursWith9Hours_thenNoWarningsCreated() {
        final List<ProjectTimeEntry> timeEntries = List.of(
                createProjectTimeEntry(7, 11),
                createProjectTimeEntry(13, 18));

        assertTrue(calculator.calculate(timeEntries).isEmpty());
    }

    @Test
    void calculate_whenValidWorkingHoursWith10Hours_thenNoWarningsCreated() {
        final List<ProjectTimeEntry> timeEntries = List.of(
                createProjectTimeEntry(7, 12),
                createProjectTimeEntry(13, 18));

        assertTrue(calculator.calculate(timeEntries).isEmpty());
    }

    @Test
    void calculate_whenWorkingTimeExceededWith11Hours_thenWarningsCreated() {
        final List<ProjectTimeEntry> timeEntries = List.of(createProjectTimeEntry(7, 18));

        List<TimeWarning> warnings = calculator.calculate(timeEntries);

        assertEquals(1, warnings.size());
        final TimeWarning warning = warnings.get(0);
        assertAll(
                () -> assertEquals(LocalDate.of(2020, 1, 7), warning.getDate()),
                () -> assertEquals(1.0, warning.getExcessWorkTime()));
    }

    private ProjectTimeEntry createProjectTimeEntry(final int startHour, final int endHour) {
        return ProjectTimeEntry.of(
                LocalDateTime.of(2020, 1, 7, startHour, 0),
                LocalDateTime.of(2020, 1, 7, endHour, 0),
                Task.BEARBEITEN);
    }
}
