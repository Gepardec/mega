package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientRestTimeTimeWarningCalculatorTest {

    private InsufficientRestTimeTimeWarningCalculator calculator = new InsufficientRestTimeTimeWarningCalculator();

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {
        assertTrue(calculator.calculate(Collections.emptyList()).isEmpty());
    }

    @Test
    void calculate_whenDataNotForTheCalculator_thenNoWarningsCreated() {
        final LocalDateTime start = LocalDateTime.of(2020, 1, 7, 7, 0);
        final LocalDateTime end = LocalDateTime.of(2020, 1, 7, 17, 0);
        final ProjectTimeEntry timeEntry = ProjectTimeEntry.of(start, end, Task.BEARBEITEN);
        assertTrue(calculator.calculate(List.of(timeEntry)).isEmpty());
    }

    @Test
    void calculate_whenRestTimeOk_thenNoWarningsCreated() {
        final List<ProjectTimeEntry> projectTimeEntries = List.of(
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 7, 7, 0), LocalDateTime.of(2020, 1, 7, 17, 0), Task.BEARBEITEN),
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 8, 7, 0), LocalDateTime.of(2020, 1, 7, 17, 0), Task.BEARBEITEN)
        );
        assertTrue(calculator.calculate(projectTimeEntries).isEmpty());
    }

    private static List<ProjectTimeEntry> createEntriesLessThan11HoursRest() {
        List<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.add(
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 7, 16, 0),
                        LocalDateTime.of(2020, 1, 7, 22, 0),
                        Task.BEARBEITEN));
        projectTimes.add(
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 8, 6, 0),
                        LocalDateTime.of(2020, 1, 8, 8, 0),
                        Task.BEARBEITEN));
        return projectTimes;
    }

    @Test
    void calculate_whenInsufficientRestTime_thenWarningsCreated() {
        List<TimeWarning> warnings = calculator.calculate(createEntriesLessThan11HoursRest());

        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 1, 8), warnings.get(0).getDate()),
                () -> assertEquals(3.0, warnings.get(0).getMissingRestTime()),
                () -> assertNull(warnings.get(0).getMissingBreakTime()),
                () -> assertNull(warnings.get(0).getExcessWorkTime()));
    }

    // Test with invalid data in all expected combinations
}
