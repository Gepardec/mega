package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeOverlapWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeOverlapWarningType;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class TimeOverlapCalculatorTest {

    private final TimeOverlapCalculator timeOverlapCalculator = new TimeOverlapCalculator();

    @Test
    void calculate_whenProjectEntriesWithoutOverlap_thenNoWarning() {
        List<ProjectEntry> projectEntries = generateProjectEntriesListWithoutOverlap();

        List<TimeOverlapWarning> warnings = timeOverlapCalculator.calculate(projectEntries);

        assertTrue(warnings.isEmpty());
    }

    @Test
    void calculate_whenProjectEntriesStartAndEndOverlap_thenNoWarning() {
        List<ProjectEntry> projectEntries = generateProjectEntriesListWhereEndAndStartOverlap();

        List<TimeOverlapWarning> warnings = timeOverlapCalculator.calculate(projectEntries);

        assertTrue(warnings.isEmpty());
    }

    @Test
    void calculate_whenProjectEntriesOverlap_thenWarning() {
        List<ProjectEntry> projectEntries = generateProjectEntriesWithOverlap();

        List<TimeOverlapWarning> warnings = timeOverlapCalculator.calculate(projectEntries);

        assertFalse(warnings.isEmpty());
    }

    @Test
    void calculate_whenProjectEntriesOverlap_thenCorrectDate() {
        List<ProjectEntry> projectEntries = generateProjectEntriesWithOverlap();

        List<TimeOverlapWarning> warnings = timeOverlapCalculator.calculate(projectEntries);

        assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate());
    }

    @Test
    void calculate_whenProjectEntryListContainsOverlappingEntries_thenReturnsTheCorrectAmountOfWarnings() {
        ProjectEntry entryOne = projectTimeEntry(7, 8, 0, 12, 0);
        ProjectEntry entryTwo = projectTimeEntry(7, 8, 30, 10, 0);
        ProjectEntry entryThree = projectTimeEntry(8, 8, 0, 12, 0);
        ProjectEntry entryFour = projectTimeEntry(9, 8, 0, 12, 0);
        ProjectEntry entryFive = projectTimeEntry(9, 9, 0, 10, 0);
        ProjectEntry entrySix = projectTimeEntry(10, 9, 0, 10, 0);

        List<ProjectEntry> projectEntries = new ArrayList<>();
        projectEntries.add(entryOne);
        projectEntries.add(entryTwo);
        projectEntries.add(entryThree);
        projectEntries.add(entryFour);
        projectEntries.add(entryFive);

        List<TimeOverlapWarning> warnings = timeOverlapCalculator.calculate(projectEntries);

        assertEquals(2, warnings.size());
    }

    private ProjectTimeEntry projectTimeEntry(int startHour, int startMinute, int endHour, int endMinute) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.BEARBEITEN)
                .workingLocation(WorkingLocation.MAIN)
                .build();
    }

    private ProjectTimeEntry projectTimeEntry(int day, int startHour, int startMinute, int endHour, int endMinute) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, day, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, day, endHour, endMinute))
                .task(Task.BEARBEITEN)
                .workingLocation(WorkingLocation.MAIN)
                .build();
    }

    private List<ProjectEntry> generateProjectEntriesListWithoutOverlap() {
        List<ProjectEntry> projectEntries = new ArrayList<>();
        projectEntries.add(projectTimeEntry(7, 8, 0, 11, 30));
        projectEntries.add(projectTimeEntry(7, 12, 0, 16, 30));
        projectEntries.add(projectTimeEntry(8, 8, 0, 11, 30));
        projectEntries.add(projectTimeEntry(8, 12, 0, 16, 30));

        return projectEntries;
    }

    private List<ProjectEntry> generateProjectEntriesListWhereEndAndStartOverlap() {
        List<ProjectEntry> projectEntries = new ArrayList<>();
        projectEntries.add(projectTimeEntry(7, 8, 0, 11, 30));
        projectEntries.add(projectTimeEntry(7, 11, 30, 16, 30));

        return projectEntries;
    }

    private List<ProjectEntry> generateProjectEntriesWithOverlap() {
        List<ProjectEntry> projectEntries = new ArrayList<>();
        projectEntries.add(projectTimeEntry(7, 8, 0, 11, 30));
        projectEntries.add(projectTimeEntry(7, 12, 0, 16, 30));
        projectEntries.add(projectTimeEntry(7, 8, 0, 11, 0));
        projectEntries.add(projectTimeEntry(7, 12, 0, 16, 30));

        return projectEntries;
    }
}
