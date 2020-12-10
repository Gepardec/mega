package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvalidJourneyCalculatorTest {

    private InvalidJourneyCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new InvalidJourneyCalculator();
    }

    @Nested
    class Calculate {

        @Nested
        class WithWarnings {

            @Test
            void whenOnlyDeparture_thenWarning() {
                final JourneyTimeEntry journeyTimeEntry = journeyTimeEntryFor(8, 9, JourneyDirection.TO, WorkingLocation.MAIN);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntry));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(JourneyWarningType.BACK_MISSING, warnings.get(0).getWarningTypes().get(0));

            }

            @Test
            void whenDepartureAndProjectTimeEntry_thenWarning() {
                final JourneyTimeEntry journeyTimeEntry = journeyTimeEntryFor(1, 8, JourneyDirection.TO, WorkingLocation.MAIN);
                final ProjectTimeEntry projectTimeEntry = projectTimeEntryFor(8, 10);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntry, projectTimeEntry));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(JourneyWarningType.BACK_MISSING, warnings.get(0).getWarningTypes().get(0));
            }

            @Test
            void whenFurtherAndProjectTimeEntryAndArrival_thenWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(1, 8, JourneyDirection.FURTHER, WorkingLocation.MAIN);
                final ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(8, 10);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(10, 12, JourneyDirection.BACK, WorkingLocation.MAIN);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree));

                // TODO: Both journey entries cause TO_MISSING Warning, because all are checked separately
                assertEquals(2, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(JourneyWarningType.TO_MISSING, warnings.get(0).getWarningTypes().get(0));
                assertEquals(JourneyWarningType.TO_MISSING, warnings.get(1).getWarningTypes().get(0));
            }

            @Test
            void whenDepartureAndProjectTimeEntryAndDepartureAgain_thenWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(1, 8, JourneyDirection.TO, WorkingLocation.MAIN);
                final ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(8, 10);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(10, 12, JourneyDirection.TO, WorkingLocation.MAIN);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree));

                // TODO: Both journey entries cause BACK_MISSING Warning, because all are checked separately
                assertEquals(2, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(JourneyWarningType.BACK_MISSING, warnings.get(0).getWarningTypes().get(0));
                assertEquals(JourneyWarningType.BACK_MISSING, warnings.get(1).getWarningTypes().get(0));
            }

            @Test
            void whenArrivalAndProjectTimeEntryAndArrivalAgain_thenWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(1, 8, JourneyDirection.BACK, WorkingLocation.MAIN);
                final ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(8, 10);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(10, 12, JourneyDirection.BACK, WorkingLocation.MAIN);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree));

                // TODO: Both journey entries cause TO_MISSING Warning, because all are checked separately
                assertEquals(2, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(JourneyWarningType.TO_MISSING, warnings.get(0).getWarningTypes().get(0));
                assertEquals(JourneyWarningType.TO_MISSING, warnings.get(1).getWarningTypes().get(0));
            }

            @Test
            void whenArrivalAndProjectTimeEntryAndFurther_thenWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(1, 8, JourneyDirection.BACK, WorkingLocation.MAIN);
                final ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(8, 10);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(10, 12, JourneyDirection.FURTHER, WorkingLocation.MAIN);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree));

                // TODO: Both journey entries cause TO_MISSING Warning, because all are checked separately
                assertEquals(2, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(JourneyWarningType.TO_MISSING, warnings.get(0).getWarningTypes().get(0));
                assertEquals(JourneyWarningType.TO_MISSING, warnings.get(1).getWarningTypes().get(0));
            }
        }

        @Nested
        class WithoutWarnings {

            @Test
            void whenDepartureAndProjectTimeAndArrival_thenNoWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(1, 8, JourneyDirection.TO, WorkingLocation.MAIN);
                final ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(8, 14);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(14, 16, JourneyDirection.BACK, WorkingLocation.MAIN);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree));

                assertTrue(warnings.isEmpty());
            }

            @Test
            void whenDepartureAndArrival_thenNoWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(1, 8, JourneyDirection.TO, WorkingLocation.MAIN);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(14, 16, JourneyDirection.BACK, WorkingLocation.MAIN);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, journeyTimeEntryThree));

                assertTrue(warnings.isEmpty());
            }

            @Test
            void whenDepartureAndProjectTimeEntryAndFurtherAndProjectTimeEntryAndArrival_thenNoWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(8, 9, JourneyDirection.TO, WorkingLocation.MAIN);
                final ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(9, 10);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(10, 11, JourneyDirection.FURTHER, WorkingLocation.MAIN);
                final ProjectTimeEntry projectTimeEntryFour = projectTimeEntryFor(11, 12);
                final JourneyTimeEntry journeyTimeEntryFive = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.MAIN);

                final List<JourneyWarning> warnings = calculator
                        .calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree, projectTimeEntryFour, journeyTimeEntryFive));

                assertTrue(warnings.isEmpty());
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

    private JourneyTimeEntry journeyTimeEntryFor(final int startHour, final int endHour, final JourneyDirection direction,
            final WorkingLocation workingLocation) {
        return journeyTimeEntryFor(startHour, 0, endHour, 0, direction, workingLocation);
    }

    private JourneyTimeEntry journeyTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute,
            final JourneyDirection direction, final WorkingLocation workingLocation) {
        return JourneyTimeEntry.newBuilder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(workingLocation)
                .journeyDirection(direction)
                .build();
    }
}
