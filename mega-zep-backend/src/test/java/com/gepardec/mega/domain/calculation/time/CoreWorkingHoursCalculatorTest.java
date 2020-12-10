package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoreWorkingHoursCalculatorTest {

    private CoreWorkingHoursCalculator calculator;

    @BeforeEach
    void init() {
        calculator = new CoreWorkingHoursCalculator();
    }

    @Nested
    class Calculate {

        @Test
        void calculate_whenDataListEmpty_thenNoWarningsCreated() {
            assertTrue(calculator.calculate(List.of()).isEmpty());
        }

        @Test
        void whenValid_thenNoWarning() {
            final ProjectTimeEntry start = projectTimeEntryFor(6, 12);
            final ProjectTimeEntry end = projectTimeEntryFor(13, 22);

            final List<TimeWarning> result = calculator.calculate(List.of(start, end));

            assertTrue(result.isEmpty());
        }

        @Nested
        class WithWarnings {

            @Test
            void whenWarning_thenTimeWarningTypeSet() {
                final ProjectTimeEntry start = projectTimeEntryFor(5, 12);
                final ProjectTimeEntry end = projectTimeEntryFor(13, 16);

                final List<TimeWarning> result = calculator.calculate(List.of(start, end));

                assertFalse(result.isEmpty(), "Warnings should have been detected");
                assertEquals(1, result.get(0).getWarningTypes().size(), "One WarningType should have been set");
                assertEquals(TimeWarningType.OUTSIDE_CORE_WORKING_TIME, result.get(0).getWarningTypes().get(0));
            }

            @Test
            void whenStartedToEarlyAt5_thenWarning() {
                final ProjectTimeEntry start = projectTimeEntryFor(5, 12);
                final ProjectTimeEntry end = projectTimeEntryFor(13, 16);

                final List<TimeWarning> result = calculator.calculate(List.of(start, end));

                assertEquals(1, result.size());
            }

            @Test
            void whenStoppedToLateAt23_thenWarning() {
                final ProjectTimeEntry start = projectTimeEntryFor(6, 12);
                final ProjectTimeEntry end = projectTimeEntryFor(18, 23);

                final List<TimeWarning> result = calculator.calculate(List.of(start, end));

                assertEquals(1, result.size());
            }

            @Test
            void whenStartedToEarlyAt5AndStoppedToLateAt23_thenWarning() {
                final ProjectTimeEntry start = projectTimeEntryFor(5, 12);
                final ProjectTimeEntry end = projectTimeEntryFor(18, 23);

                final List<TimeWarning> result = calculator.calculate(List.of(start, end));

                assertEquals(1, result.size());
            }
        }
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startHour, final int endHour) {
        return projectTimeEntryFor(startHour, 0, endHour, 0);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute) {
        return ProjectTimeEntry.of(
                LocalDateTime.of(2020, 1, 7, startHour, startMinute),
                LocalDateTime.of(2020, 1, 7, endHour, endMinute),
                Task.BEARBEITEN,
                WorkingLocation.MAIN);
    }
}
