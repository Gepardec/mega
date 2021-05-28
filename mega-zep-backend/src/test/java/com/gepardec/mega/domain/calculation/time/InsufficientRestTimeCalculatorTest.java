package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.Vehicle;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InsufficientRestTimeCalculatorTest {

    private final InsufficientRestCalculator calculator = new InsufficientRestCalculator();

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {
        assertTrue(calculator.calculate(List.of()).isEmpty());
    }

    private ProjectTimeEntry projectTimeEntryFor(final int day, final int startHour, final int startMinute, final int endHour,
                                                 final int endMinute) {
        return projectTimeEntryFor(day, startHour, startMinute, day, endHour, endMinute);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startDay, final int startHour, final int startMinute, final int endDay, final int endHour,
                                                 final int endMinute) {
        return ProjectTimeEntry.of(
                LocalDateTime.of(2020, 1, startDay, startHour, startMinute),
                LocalDateTime.of(2020, 1, endDay, endHour, endMinute),
                Task.BEARBEITEN,
                WorkingLocation.MAIN);
    }

    private JourneyTimeEntry journeyTimeEntryFor(final int startDay, final int startHour, final int startMinute, final int endDay, final int endHour,
                                                 final int endMinute) {
        return JourneyTimeEntry.newBuilder()
                .fromTime(LocalDateTime.of(2020, startDay, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, endDay, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(WorkingLocation.MAIN)
                .journeyDirection(JourneyDirection.TO)
                .vehicle(Vehicle.OTHER_INACTIVE)
                .build();
    }
    @Nested
    class Calculate {

        @Test
        void whenDataListEmpty_thenNoWarningsCreated() {
            assertTrue(calculator.calculate(List.of()).isEmpty());
        }

        @Test
        void whenWarning_thenOnlyMissingRestTimeSet() {
            final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
            final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(2, 8, 0, 11, 0);

            final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

            assertEquals(1, warnings.size());
            final TimeWarning warning = warnings.get(0);
            assertNotNull(warning.getDate());
            assertNotNull(warning.getMissingRestTime());
            assertNull(warning.getExcessWorkTime());
            assertNull(warning.getMissingBreakTime());
        }

        @Test
        void whenUnordered_thenOrdered() {
            final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
            final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(2, 8, 0, 11, 0);

            final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryTwo, timeEntryOne));

            assertEquals(1, warnings.size());
            assertEquals(1, warnings.get(0).getMissingRestTime());
        }

        @Nested
        class WithWarnings {

            @Test
            void when10HoursRestTime_thenWarning() {
                final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
                final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(2, 8, 0, 11, 0);

                final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getMissingRestTime());
            }

            @Test
            void whenOneJourneyEntryAnd10HoursRestTime_thenWarning() {
                final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
                final JourneyTimeEntry timeEntryTwo = journeyTimeEntryFor(1, 22, 0, 2, 1, 0);
                final ProjectTimeEntry timeEntryThree = projectTimeEntryFor(2, 8, 0, 11, 0);

                final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getMissingRestTime());
            }
        }

        @Nested
        class WithoutWarnings {

            @Test
            void when11HoursRestTime_thenNoWarning() {
                final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
                final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(2, 9, 0, 11, 0);

                final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

                assertTrue(warnings.isEmpty());
            }

            @Test
            void whenOneJourneyEntryAnd11HoursRestTime_thenNoWarning() {
                final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
                final JourneyTimeEntry timeEntryTwo = journeyTimeEntryFor(1, 22, 0, 2, 1, 0);
                final ProjectTimeEntry timeEntryThree = projectTimeEntryFor(2, 9, 0, 11, 0);

                final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

                assertTrue(warnings.isEmpty());
            }
        }
    }
}
