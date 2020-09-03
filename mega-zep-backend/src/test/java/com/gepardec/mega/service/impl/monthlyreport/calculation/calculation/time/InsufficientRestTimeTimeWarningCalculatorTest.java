package com.gepardec.mega.service.impl.monthlyreport.calculation.calculation.time;

import com.gepardec.mega.service.impl.monthlyreport.calculation.time.InsufficientRestTimeTimeWarningCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsufficientRestTimeTimeWarningCalculatorTest {

    private InsufficientRestTimeTimeWarningCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new InsufficientRestTimeTimeWarningCalculator();
    }

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {

    }

    @Test
    void calculate_whenDataNotForTheCalculator_thenNoWarningsCreated() {

    }

    @Test
    void calculate_whenInsufficientRestTime_thenWarningsCreated() {

    }

    @Test
    void calculate_whenRestTimeOk_thenNoWarningsCreated() {

    }

    // Test with invalid data in all expected combinations
}
