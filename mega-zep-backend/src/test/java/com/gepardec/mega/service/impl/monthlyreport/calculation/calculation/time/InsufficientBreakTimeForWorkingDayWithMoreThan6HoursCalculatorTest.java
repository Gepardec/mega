package com.gepardec.mega.service.impl.monthlyreport.calculation.calculation.time;

import com.gepardec.mega.service.impl.monthlyreport.calculation.time.InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculatorTest {

    private InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculator();
    }

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {

    }

    @Test
    void calculate_whenDataNotForTheCalculator_thenNoWarningsCreated() {

    }

    @Test
    void calculate_whenInsufficientBreakTime_thenWarningsCreated() {

    }

    @Test
    void calculate_whenBreakTimeOk_thenNoWarningsCreated() {

    }

    // Test with invalid data in all expected combinations
}
