package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.Vehicle;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InsufficientBreakTimeCalculatorTest {

    private InsufficientBreakCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new InsufficientBreakCalculator();
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startHour, final int endHour) {
        return projectTimeEntryFor(startHour, 0, endHour, 0);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.BEARBEITEN)
                .workingLocation(WorkingLocation.MAIN)
                .build();
    }

    private JourneyTimeEntry journeyTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute) {
        return JourneyTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(WorkingLocation.MAIN)
                .journeyDirection(JourneyDirection.TO)
                .vehicle(Vehicle.OTHER_INACTIVE)
                .build();
    }

    @Test
    void when6Hours_thenNoWarning() {
        final ProjectTimeEntry timeEntry = projectTimeEntryFor(7, 13);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntry));

        assertTrue(warnings.isEmpty());
    }

    @Test
    void when3And6HoursAnd30MinBreakTime_thenNoWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 30, 16, 0);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertTrue(warnings.isEmpty());
    }

    @Test
    void when3EntriesAndTwo30MinutesBreak_thenNoWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 15, 12, 15);
        final ProjectTimeEntry timeEntryThree = projectTimeEntryFor(12, 30, 15, 30);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertTrue(warnings.isEmpty());
    }

    @Test
    void when4EntriesAnd30MinBreakAndWithOneJourneyTimeEntry_thenWarningButIgnoredJourneyTimeEntry() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 30, 17, 15);
        final JourneyTimeEntry timeEntryThree = journeyTimeEntryFor(17, 15, 19, 30);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertTrue(warnings.isEmpty());
    }

    @Test
    void whenDataListEmpty_thenNoWarningsCreated() {
        assertTrue(calculator.calculate(List.of()).isEmpty());
    }

    @Test
    void whenWarning_thenOnlyMissingBreakTimeSet() {
        final ProjectTimeEntry timeEntry = projectTimeEntryFor(7, 14);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntry));

        assertEquals(1, warnings.size());
        assertEquals(0.5, warnings.get(0).getMissingBreakTime());

        assertEquals(1, warnings.size());
        final TimeWarning warning = warnings.get(0);
        assertNotNull(warning.getDate());
        assertNotNull(warning.getMissingBreakTime());
        assertNull(warning.getMissingRestTime());
        assertNull(warning.getExcessWorkTime());
    }

    @Test
    void whenUnordered_thenOrdered() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 16);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryTwo, timeEntryOne));

        assertEquals(1, warnings.size());
        assertEquals(0.5, warnings.get(0).getMissingBreakTime());
    }

    @Test
    void when7Hours_thenWarning() {
        final ProjectTimeEntry timeEntry = projectTimeEntryFor(7, 14);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntry));

        assertEquals(1, warnings.size());
        assertEquals(0.5, warnings.get(0).getMissingBreakTime());
    }

    @Test
    void when3And6HoursAndNoBreakTime_thenWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 16);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertEquals(1, warnings.size());
        assertEquals(0.5, warnings.get(0).getMissingBreakTime());
    }

    @Test
    void when3And6HoursAnd15MinBreakTime_thenWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 15, 16, 15);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertEquals(1, warnings.size());
        assertEquals(0.25, warnings.get(0).getMissingBreakTime());
    }

    @Test
    void when3EntriesAndTwo20MinutesBreak_thenWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 10, 12, 10);
        final ProjectTimeEntry timeEntryThree = projectTimeEntryFor(12, 20, 15, 20);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertEquals(1, warnings.size());
        assertEquals(0.16, warnings.get(0).getMissingBreakTime());
    }

    @Test
    void when4EntriesAnd15MinBreakAndWithOneJourneyTimeEntry_thenWarningButIgnoredJourneyTimeEntry() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 15, 16, 45);
        final JourneyTimeEntry timeEntryThree = journeyTimeEntryFor(16, 45, 19, 15);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertEquals(1, warnings.size());
        assertEquals(0.25, warnings.get(0).getMissingBreakTime());
    }
}
