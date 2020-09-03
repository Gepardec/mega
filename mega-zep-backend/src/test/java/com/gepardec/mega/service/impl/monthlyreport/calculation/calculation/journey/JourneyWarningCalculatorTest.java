package com.gepardec.mega.service.impl.monthlyreport.calculation.calculation.journey;

import com.gepardec.mega.service.impl.monthlyreport.calculation.journey.JourneyWarningCalculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JourneyWarningCalculatorTest {

    private JourneyWarningCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new JourneyWarningCalculator();
    }

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {

    }

    @Test
    void calculate_whenDataNotForTheCalculator_thenNoWarningsCreated() {

    }

    @Test
    void calculate_whenDataIsValid_thenNoWarningsFound() {

    }

    // Test with invalid data in all expected combinations
}
