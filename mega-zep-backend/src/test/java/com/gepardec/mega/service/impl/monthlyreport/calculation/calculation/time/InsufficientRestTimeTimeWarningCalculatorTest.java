package com.gepardec.mega.service.impl.monthlyreport.calculation.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.InsufficientRestTimeTimeWarningCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InsufficientRestTimeTimeWarningCalculatorTest {

    private InsufficientRestTimeTimeWarningCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new InsufficientRestTimeTimeWarningCalculator();
    }

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {
        assertTrue(calculator.calculate(Collections.emptyList()).isEmpty());
    }

    @Test
    void calculate_whenDataNotForTheCalculator_thenNoWarningsCreated() {
        final ProjectTimeEntry entry = new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 7, 0), LocalDateTime.of(2020, 1, 7, 17, 0), Task.BEARBEITEN);
        assertTrue(calculator.calculate(Collections.singletonList(entry)).isEmpty());
    }

    @Test
    void calculate_whenInsufficientRestTime_thenWarningsCreated() {
        List<ProjectTimeEntry> projectTimeEntries = Arrays.asList(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 7, 0), LocalDateTime.of(2020, 1, 7, 17, 0), Task.BEARBEITEN),
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 8, 2, 0), LocalDateTime.of(2020, 1, 7, 7, 0), Task.BEARBEITEN)
        );
        assertEquals(1, calculator.calculate(projectTimeEntries).size());
    }

    @Test
    void calculate_whenRestTimeOk_thenNoWarningsCreated() {
        List<ProjectTimeEntry> projectTimeEntries = Arrays.asList(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 7, 0), LocalDateTime.of(2020, 1, 7, 17, 0), Task.BEARBEITEN),
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 8, 7, 0), LocalDateTime.of(2020, 1, 7, 17, 0), Task.BEARBEITEN)
        );
        assertTrue(calculator.calculate(projectTimeEntries).isEmpty());
    }

    // Test with invalid data in all expected combinations
}
