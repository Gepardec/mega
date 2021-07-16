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

class ExceededMaximumWorkingHoursPerDayCalculatorTest {

    private ExceededMaximumWorkingHoursPerDayCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new ExceededMaximumWorkingHoursPerDayCalculator();
    }

    private ProjectTimeEntry projectTimeEntryFor(int startHour, int endHour) {
        return projectTimeEntryFor(startHour, 0, endHour, 0);
    }

    private ProjectTimeEntry projectTimeEntryFor(int startHour, int startMinute, int endHour, int endMinute) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.BEARBEITEN)
                .workingLocation(WorkingLocation.MAIN)
                .build();
    }

    private JourneyTimeEntry journeyTimeEntryFor(int startHour, int endHour) {
        return journeyTimeEntryFor(startHour, 0, endHour, 0);
    }

    private JourneyTimeEntry journeyTimeEntryFor(int startHour, int startMinute, int endHour, int endMinute) {
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
    void when10HoursPerDay_thenNoWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 0, 12, 0);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 0, 18, 0);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertTrue(warnings.isEmpty());
    }

    @Test
    void whenOneJourneyEntry10HoursPerDay_thenWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 18);
        JourneyTimeEntry timeEntryThree = journeyTimeEntryFor(18, 22);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertTrue(warnings.isEmpty());
    }

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {
        assertTrue(calculator.calculate(List.of()).isEmpty());
    }

    @Test
    void whenWarning_thenOnlyExcessWorkTimeSet() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 19);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertEquals(1, warnings.size());
        TimeWarning warning = warnings.get(0);
        assertNotNull(warning.getDate());
        assertNotNull(warning.getExcessWorkTime());
        assertNull(warning.getMissingRestTime());
        assertNull(warning.getMissingBreakTime());
    }

    @Test
    void whenUnordered_thenOrdered() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 19);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryTwo, timeEntryOne));

        assertEquals(1, warnings.size());
        assertEquals(1, warnings.get(0).getExcessWorkTime());
    }

    @Test
    void when11HoursPerDay_thenWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 19);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertEquals(1, warnings.size());
        assertEquals(1, warnings.get(0).getExcessWorkTime());
    }

    @Test
    void whenOneJourneyEntry11HoursPerDay_thenWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 12);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(13, 19);
        JourneyTimeEntry timeEntryThree = journeyTimeEntryFor(19, 22);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertEquals(1, warnings.size());
        assertEquals(1, warnings.get(0).getExcessWorkTime());
    }
}
