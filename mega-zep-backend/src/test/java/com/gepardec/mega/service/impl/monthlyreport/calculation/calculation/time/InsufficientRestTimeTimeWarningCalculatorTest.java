package com.gepardec.mega.service.impl.monthlyreport.calculation.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.InsufficientRestTimeTimeWarningCalculator;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class InsufficientRestTimeTimeWarningCalculatorTest {

    private InsufficientRestTimeTimeWarningCalculator calculator = new InsufficientRestTimeTimeWarningCalculator();

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {
        assertTrue(calculator.calculate(Collections.emptyList()).isEmpty());
    }

    @Test
    void calculate_whenDataNotForTheCalculator_thenNoWarningsCreated() {
        List<ProjectTimeEntry> projectTimeEntries = new ArrayList<>();
        projectTimeEntries.addAll(Arrays.asList(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 7, 0), LocalDateTime.of(2020, 1, 7, 17, 0), Task.BEARBEITEN)
        ));
        assertTrue(calculator.calculate(projectTimeEntries).isEmpty());
    }

    @Test
    void calculate_whenRestTimeOk_thenNoWarningsCreated() {
        List<ProjectTimeEntry> projectTimeEntries = new ArrayList<>();
        projectTimeEntries.addAll(Arrays.asList(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 7, 0), LocalDateTime.of(2020, 1, 7, 17, 0), Task.BEARBEITEN),
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 8, 7, 0), LocalDateTime.of(2020, 1, 7, 17, 0), Task.BEARBEITEN)
        ));
        assertTrue(calculator.calculate(projectTimeEntries).isEmpty());
    }

    private static List<ProjectTimeEntry> createEntriesLessThan11HoursRest() {
        List<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 16, 0),
                        LocalDateTime.of(2020, 1, 7, 22, 0),
                        Task.BEARBEITEN));
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 8, 6, 0),
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
