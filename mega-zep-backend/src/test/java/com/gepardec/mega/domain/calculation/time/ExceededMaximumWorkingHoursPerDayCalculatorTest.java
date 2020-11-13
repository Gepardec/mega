package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.time.ExceededMaximumWorkingHoursPerDayCalculator;
import com.gepardec.mega.domain.model.monthlyreport.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExceededMaximumWorkingHoursPerDayCalculatorTest {

    private ExceededMaximumWorkingHoursPerDayCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new ExceededMaximumWorkingHoursPerDayCalculator();
    }

    @Nested
    class Calculate {

        @Test
        void calculate_whenDataListEmpty_thenNoWarningsCreated() {
            assertTrue(calculator.calculate(Collections.emptyList()).isEmpty());
        }

        @Test
        void whenWarning_thenOnlyExcessWorkTimeSet() {
            final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
            final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 19);

            final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

            assertEquals(1, warnings.size());
            final TimeWarning warning = warnings.get(0);
            assertNotNull(warning.getDate());
            assertNotNull(warning.getExcessWorkTime());
            assertNull(warning.getMissingRestTime());
            assertNull(warning.getMissingBreakTime());
        }

        @Test
        void whenUnordered_thenOrdered() {
            final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
            final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 19);

            final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryTwo, timeEntryOne));

            assertEquals(1, warnings.size());
            assertEquals(1, warnings.get(0).getExcessWorkTime());
        }

        @Nested
        class WithWarnings {

            @Test
            void when11HoursPerDay_thenWarning() {
                final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
                final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 19);

                final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getExcessWorkTime());
            }

            @Test
            void whenOneJourneyEntry11HoursPerDay_thenWarning() {
                final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
                final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 19);
                final JourneyTimeEntry timeEntryThree = journeyTimeEntryFor(19, 22);

                final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

                assertEquals(1, warnings.size());
                assertEquals(1, warnings.get(0).getExcessWorkTime());
            }
        }

        @Nested
        class WithoutWarnings {

            @Test
            void when10HoursPerDay_thenNoWarning() {
                final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 0, 12, 0);
                final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 0, 18, 0);

                final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

                assertTrue(warnings.isEmpty());
            }

            @Test
            void whenOneJourneyEntry10HoursPerDay_thenWarning() {
                final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
                final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 18);
                final JourneyTimeEntry timeEntryThree = journeyTimeEntryFor(18, 22);

                final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

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

    private JourneyTimeEntry journeyTimeEntryFor(final int startHour, final int endHour) {
        return journeyTimeEntryFor(startHour, 0, endHour, 0);
    }

    private JourneyTimeEntry journeyTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute) {
        return JourneyTimeEntry.newBuilder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(WorkingLocation.MAIN)
                .journeyDirection(JourneyDirection.TO)
                .build();
    }
}
