package com.gepardec.mega.monthlyreport.warning;

import com.gepardec.mega.domain.model.*;
import com.gepardec.mega.service.monthlyreport.WarningCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WarningCalculatorTest {

    @InjectMocks
    private WarningCalculator warningCalculator;


    @Test
    void determineTimeWarnings_oneEntryMoreThan6Hours_warning() {
        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(createEntriesMoreThan6Hours());
        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 01, 07), warnings.get(0).getDate()),
                () -> assertNull(warnings.get(0).getMissingRestTime()),
                () -> assertEquals(0.5, warnings.get(0).getMissingBreakTime()),
                () -> assertNull(warnings.get(0).getExcessWorkTime()));
    }

    @Test
    void determineTimeWarnings_moreThan6hoursButSummarizedButNotMoreThan6HoursWorkingTime_noWarning() {
        assertEquals(0, warningCalculator.determineTimeWarnings(createEntriesMoreThan6HoursWithTravelTime()).size());
    }


    @Test
    void determineTimeWarnings_moreThan10HoursADay_Warning() {
        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(createEntriesMoreThan10HoursADay());

        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 01, 07), warnings.get(0).getDate()),
                () -> assertNull(warnings.get(0).getMissingRestTime()),
                () -> assertNull(warnings.get(0).getMissingBreakTime()),
                () -> assertEquals(4.25, warnings.get(0).getExcessWorkTime()));
    }


    @Test
    void determineTimeWarnings_lessThan11HoursRest_Warning() {
        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(createEntriesLessThan11HoursRest());

        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 1, 8), warnings.get(0).getDate()),
                () -> assertEquals(3.0, warnings.get(0).getMissingRestTime()),
                () -> assertNull(warnings.get(0).getMissingBreakTime()),
                () -> assertNull(warnings.get(0).getExcessWorkTime()));
    }

    @Test
    void determineTimeWarnings_lessThan11HoursRestAndMoreThan10HoursADayAndThan6Hours_Warnings() {
        ArrayList<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.addAll(createEntriesLessThan11HoursRest());
        projectTimes.addAll(createEntriesMoreThan6Hours());

        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(projectTimes);
        assertAll(() -> assertEquals(2, warnings.size()),
                //first day
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertNull(warnings.get(0).getMissingRestTime()),
                () -> assertEquals(0.5, warnings.get(0).getMissingBreakTime()),
                () -> assertEquals(3.0, warnings.get(0).getExcessWorkTime()),
                //second day
                () -> assertEquals(LocalDate.of(2020, 1, 8), warnings.get(1).getDate()),
                () -> assertEquals(3.0, warnings.get(1).getMissingRestTime()),
                () -> assertNull(warnings.get(1).getMissingBreakTime()),
                () -> assertNull(warnings.get(1).getExcessWorkTime()));
    }

    private static List<ProjectTimeEntry> createEntriesMoreThan6Hours() {
        return Arrays.asList(
                new ProjectTimeEntry(LocalDateTime.of(2020, 01, 07, 9, 00),
                        LocalDateTime.of(2020, 01, 07, 16, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
    }

    private static List<ProjectTimeEntry> createEntriesMoreThan6HoursWithTravelTime() {
        return Arrays.asList(

                new ProjectTimeEntry(LocalDateTime.of(2020, 01, 07, 9, 00),
                        LocalDateTime.of(2020, 01, 07, 14, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION),
                new JourneyEntry(LocalDateTime.of(2020, 01, 07, 14, 00),
                        LocalDateTime.of(2020, 01, 07, 16, 00),
                        Task.REISEN,
                        WorkingLocation.AUSTRIA,
                        JourneyDirection.TO_AIM),
                new ProjectTimeEntry(LocalDateTime.of(2020, 01, 07, 16, 00),
                        LocalDateTime.of(2020, 01, 07, 17, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
    }


    private static List<ProjectTimeEntry> createEntriesMoreThan10HoursADay() {
        List<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 01, 07, 7, 00),
                        LocalDateTime.of(2020, 01, 07, 10, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 01, 07, 10, 30),
                        LocalDateTime.of(2020, 01, 07, 17, 45),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 01, 07, 18, 00),
                        LocalDateTime.of(2020, 01, 07, 22, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        return projectTimes;
    }

    private static List<ProjectTimeEntry> createEntriesLessThan11HoursRest() {
        List<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 01, 07, 16, 00),
                        LocalDateTime.of(2020, 01, 07, 22, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 8, 6, 00),
                        LocalDateTime.of(2020, 1, 8, 8, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        return projectTimes;
    }
}
