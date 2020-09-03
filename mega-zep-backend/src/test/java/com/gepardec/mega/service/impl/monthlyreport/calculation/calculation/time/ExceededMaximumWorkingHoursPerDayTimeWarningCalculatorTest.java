package com.gepardec.mega.service.impl.monthlyreport.calculation.calculation.time;

import com.gepardec.mega.service.impl.monthlyreport.calculation.time.ExceededMaximumWorkingHoursPerDayTimeWarningCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExceededMaximumWorkingHoursPerDayTimeWarningCalculatorTest {

    private ExceededMaximumWorkingHoursPerDayTimeWarningCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new ExceededMaximumWorkingHoursPerDayTimeWarningCalculator();
    }

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {

    }

    @Test
    void calculate_whenDataNotForTheCalculator_thenNoWarningsCreated() {

    }

    @Test
    void calculate_whenWorkingTimeExceeded_thenWarningsCreated() {

    }

    @Test
    void calculate_whenWorkingTimeOk_thenNoWarningsCreated() {

    }

    // Test with invalid data in all expected combinations
}
