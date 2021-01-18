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
        void whenUnordered_thenNoWarning() {
            final JourneyTimeEntry travelBefore = journeyTimeEntryFor(1, 6, Vehicle.OTHER_INACTIVE);
            final ProjectTimeEntry start = projectTimeEntryFor(6, 12);
            final ProjectTimeEntry end = projectTimeEntryFor(13, 22);
            final JourneyTimeEntry travelAfter = journeyTimeEntryFor(22, 23, Vehicle.OTHER_INACTIVE);

            final List<TimeWarning> result = calculator.calculate(List.of(travelAfter, end, start, travelBefore));

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

            @Test
            void whenInactiveTravelerOnJourneyAndStartedToEarly_thenWarning() {
                final JourneyTimeEntry start = journeyTimeEntryFor(3, 4, Vehicle.OTHER_INACTIVE);
                final ProjectTimeEntry end = projectTimeEntryFor(5, 8);

                final List<TimeWarning> result = calculator.calculate(List.of(start, end));

                assertEquals(1, result.size());
            }

            @Test
            void whenActiveTravelerOnJourneyAndStartedToEarly_thenWarning() {
                final JourneyTimeEntry start = journeyTimeEntryFor(3, 6, Vehicle.CAR_ACTIVE);

                final List<TimeWarning> result = calculator.calculate(List.of(start));

                assertEquals(1, result.size());
            }
        }

        @Nested
        class WithoutWarnings {

            @Test
            void whenDataListEmpty_thenNoWarnings() {
                assertTrue(calculator.calculate(List.of()).isEmpty());
            }

            @Test
            void whenValid_thenNoWarning() {
                final JourneyTimeEntry travelInactiveBefore = journeyTimeEntryFor(1, 6, Vehicle.OTHER_INACTIVE);
                final JourneyTimeEntry travelActiveBefore = journeyTimeEntryFor(6, 10, Vehicle.CAR_ACTIVE);
                final ProjectTimeEntry start = projectTimeEntryFor(10, 12);
                final ProjectTimeEntry end = projectTimeEntryFor(13, 16);
                final JourneyTimeEntry travelActiveAfter = journeyTimeEntryFor(17, 22, Vehicle.CAR_ACTIVE);
                final JourneyTimeEntry travelInactiveAfter = journeyTimeEntryFor(22, 23, Vehicle.OTHER_INACTIVE);

                final List<TimeWarning> result = calculator
                        .calculate(List.of(travelInactiveBefore, travelActiveBefore, start, end, travelActiveAfter, travelInactiveAfter));

                assertTrue(result.isEmpty());
            }

            @Test
            void whenOnlyInactiveTravelerOnJourney_thenNoWarning() {
                final JourneyTimeEntry start = journeyTimeEntryFor(3, 10, Vehicle.OTHER_INACTIVE);
                final JourneyTimeEntry end = journeyTimeEntryFor(10, 23, Vehicle.OTHER_INACTIVE);

                final List<TimeWarning> result = calculator.calculate(List.of(start, end));

                assertTrue(result.isEmpty());
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

    private JourneyTimeEntry journeyTimeEntryFor(final int startHour, final int endHour, final Vehicle vehicle) {
        return journeyTimeEntryFor(startHour, 0, endHour, 0, vehicle);
    }

    private JourneyTimeEntry journeyTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute, final Vehicle vehicle) {
        return JourneyTimeEntry.newBuilder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(WorkingLocation.MAIN)
                .journeyDirection(JourneyDirection.TO)
                .vehicle(vehicle)
                .build();
    }
}
