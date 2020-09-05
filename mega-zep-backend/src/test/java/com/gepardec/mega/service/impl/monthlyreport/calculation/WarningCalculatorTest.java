package com.gepardec.mega.service.impl.monthlyreport.calculation;

import com.gepardec.mega.domain.model.monthlyreport.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarningCalculatorTest {

    @InjectMocks
    WarningCalculator warningCalculator;

    @Mock
    ResourceBundle messages;

    // Here we test combinations of time and journy warnings
    // 1. Only data for  time calculators
    // 2. Only data for journey calculators
    // 3. Data for all calculators
    // 4. Empty Data List

    @Test
    void determineTimeWarnings_oneEntryMoreThan6Hours_warning() {
        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(createEntriesMoreThan6Hours());
        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertNull(warnings.get(0).getMissingRestTime()),
                () -> assertEquals(0.5, warnings.get(0).getMissingBreakTime()),
                () -> assertNull(warnings.get(0).getExcessWorkTime()));
    }

    @Test
    void determineTimeWarnings_moreThan6hoursButSummarizedButNotMoreThan6HoursWorkingTime_noWarning() {
        assertTrue(warningCalculator.determineTimeWarnings(createEntriesMoreThan6HoursWithTravelTime()).isEmpty());
    }


    @Test
    void determineTimeWarnings_moreThan10HoursADay_Warning() {
        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(createEntriesMoreThan10HoursADay());

        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertNull(warnings.get(0).getMissingRestTime()),
                () -> assertNull(warnings.get(0).getMissingBreakTime()),
                () -> assertEquals(4.25, warnings.get(0).getExcessWorkTime()));
    }

    @Test
    void determineJourneyWarnings_validateWarningStrings() {

        List<ProjectTimeEntry> projectTimeEntries = List.of(
                new JourneyEntry(LocalDateTime.now(), LocalDateTime.now().plusHours(2), Task.REISEN, JourneyDirection.BACK),
                new JourneyEntry(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5), Task.REISEN, JourneyDirection.TO_AIM)
        );

        when(messages.getString("warning.WARNING_JOURNEY_TO_AIM_MISSING")).thenReturn("WARNING_JOURNEY_TO_AIM_MISSING");
        when(messages.getString("warning.WARNING_JOURNEY_BACK_MISSING")).thenReturn("WARNING_JOURNEY_BACK_MISSING");

        List<JourneyWarning> warnings = warningCalculator.determineJourneyWarnings(projectTimeEntries);

        assertAll(
                () -> assertEquals(1, warnings.size()),
                () -> assertEquals(2, warnings.get(0).getWarnings().size()),
                () -> assertEquals("WARNING_JOURNEY_TO_AIM_MISSING", warnings.get(0).getWarnings().get(0)),
                () -> assertEquals("WARNING_JOURNEY_BACK_MISSING", warnings.get(0).getWarnings().get(1))
        );
    }

    private static List<ProjectTimeEntry> createEntriesMoreThan6Hours() {
        return List.of(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 9, 0),
                        LocalDateTime.of(2020, 1, 7, 16, 0),
                        Task.BEARBEITEN));
    }

    private static List<ProjectTimeEntry> createEntriesMoreThan6HoursWithTravelTime() {
        return Arrays.asList(

                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 9, 0),
                        LocalDateTime.of(2020, 1, 7, 14, 0),
                        Task.BEARBEITEN),
                new JourneyEntry(LocalDateTime.of(2020, 1, 7, 14, 0),
                        LocalDateTime.of(2020, 1, 7, 16, 0),
                        Task.REISEN,
                        JourneyDirection.TO_AIM),
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 16, 0),
                        LocalDateTime.of(2020, 1, 7, 17, 0),
                        Task.BEARBEITEN));
    }


    private static List<ProjectTimeEntry> createEntriesMoreThan10HoursADay() {
        List<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 7, 0),
                        LocalDateTime.of(2020, 1, 7, 10, 0),
                        Task.BEARBEITEN));
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 10, 30),
                        LocalDateTime.of(2020, 1, 7, 17, 45),
                        Task.BEARBEITEN));
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 18, 0),
                        LocalDateTime.of(2020, 1, 7, 22, 0),
                        Task.BEARBEITEN));
        return projectTimes;
    }


}
