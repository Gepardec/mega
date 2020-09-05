package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculatorTest {

    private InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculator();
    }

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {
        assertTrue(calculator.calculate(List.of()).isEmpty());
    }

    @Test
    void calculate_whenInsufficientBreakTimeWith7Hours_thenWarningsCreated() {

    }

    @Test
    void calculate_whenBreakTimeOk_thenNoWarningsCreated() {

    }

    private ProjectTimeEntry createProjectTimeEntry(final int startHour, final int endHour) {
        return ProjectTimeEntry.of(
                LocalDateTime.of(2020, 1, 7, startHour, 0),
                LocalDateTime.of(2020, 1, 7, endHour, 0),
                Task.BEARBEITEN);
    }
}
