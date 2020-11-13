package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvalidWorkingLocationInJourneyCalculatorTest {

    private InvalidWorkingLocationInJourneyCalculator calculator;

    @BeforeEach
    void init() {
        calculator = new InvalidWorkingLocationInJourneyCalculator();
    }

    @Nested
    class Calculate {

        @Nested
        class WithWarnings {

            @Test
            void whenOneProjectTimeEntryWithinJourneyWithWorkingLocationMain_thenWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
                final ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.MAIN);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(Warning.JOURNEY_INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
            }

            @Test
            void whenOneProjectTimeEntryWithinJourneyWithDifferentWorkingLocation_thenWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
                final ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.CH);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(Warning.JOURNEY_INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
            }

            @Test
            void whenTwoProjectTimeEntryWithinJourneyWithWorkingLocationMain_thenOneWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
                final ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.MAIN);
                final ProjectEntry projectEntryThree = projectTimeEntryFor(9, 10, WorkingLocation.MAIN);
                final JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.BACK, WorkingLocation.A);

                final List<JourneyWarning> warnings = calculator
                        .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(Warning.JOURNEY_INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
            }

            @Test
            void whenTwoProjectTimeEntryWithinJourneyWithOneInvalidWorkingLocation_thenOneWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
                final ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
                final ProjectEntry projectEntryThree = projectTimeEntryFor(9, 10, WorkingLocation.MAIN);
                final JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.BACK, WorkingLocation.A);

                final List<JourneyWarning> warnings = calculator
                        .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(Warning.JOURNEY_INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
            }

            @Test
            void whenTwoProjectTimeEntryWithinTwoJourneysWithOneInvalidWorkingLocation_thenOneWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
                final ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.MAIN);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);
                final JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.TO, WorkingLocation.P);
                final ProjectEntry projectEntryFive = projectTimeEntryFor(11, 12, WorkingLocation.P);
                final JourneyTimeEntry journeyTimeEntrySix = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);

                final List<JourneyWarning> warnings = calculator
                        .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree, journeyTimeEntryFour, projectEntryFive,
                                journeyTimeEntrySix));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getWarningTypes().size());
                assertEquals(Warning.JOURNEY_INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
            }
        }

        @Nested
        class WithoutWarnings {

            @Test
            void whenOneProjectTimeEntryWithinJourney_thenNoWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
                final ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);

                final List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree));

                assertTrue(warnings.isEmpty());
            }

            @Test
            void whenTwoProjectTimeEntryWithinJourney_thenNoWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
                final ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
                final ProjectEntry projectEntryThree = projectTimeEntryFor(9, 10, WorkingLocation.A);
                final JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.BACK, WorkingLocation.A);

                final List<JourneyWarning> warnings = calculator
                        .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

                assertTrue(warnings.isEmpty());
            }

            @Test
            void whenTwoProjectTimeEntryWithinTwoJourneys_thenNoWarning() {
                final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
                final ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
                final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);
                final JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.TO, WorkingLocation.P);
                final ProjectEntry projectEntryFive = projectTimeEntryFor(11, 12, WorkingLocation.P);
                final JourneyTimeEntry journeyTimeEntrySix = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);

                final List<JourneyWarning> warnings = calculator
                        .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree, journeyTimeEntryFour, projectEntryFive,
                                journeyTimeEntrySix));

                assertTrue(warnings.isEmpty());
            }
        }
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startHour, final int endHour, final WorkingLocation workingLocation) {
        return projectTimeEntryFor(startHour, 0, endHour, 0, workingLocation);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute,
            final WorkingLocation workingLocation) {
        return ProjectTimeEntry.of(
                LocalDateTime.of(2020, 1, 7, startHour, startMinute),
                LocalDateTime.of(2020, 1, 7, endHour, endMinute),
                Task.BEARBEITEN,
                workingLocation);
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
