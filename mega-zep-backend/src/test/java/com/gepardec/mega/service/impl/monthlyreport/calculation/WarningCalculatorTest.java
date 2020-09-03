package com.gepardec.mega.service.impl.monthlyreport.calculation;

import com.gepardec.mega.domain.model.monthlyreport.*;
import com.gepardec.mega.service.impl.monthlyreport.calculation.WarningCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WarningCalculatorTest {

    @InjectMocks
    private WarningCalculator warningCalculator;

    @Mock
    private ResourceBundle resourceBundle;

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
        assertEquals(0, warningCalculator.determineTimeWarnings(createEntriesMoreThan6HoursWithTravelTime()).size());
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

    @Test
    void determineJourneyWarnings_journeyToAimMissing_Warning() {
        ArrayList<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 14, 0),
                        LocalDateTime.of(2020, 1, 7, 16, 0),
                        Task.BEARBEITEN));
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 1, 7, 14, 0),
                        LocalDateTime.of(2020, 1, 7, 16, 0),
                        Task.REISEN,
                        JourneyDirection.BACK));

        List<JourneyWarning> warnings = warningCalculator.determineJourneyWarnings(projectTimes);
        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertEquals(1, warnings.get(0).getWarnings().size()));
    }

    @Test
    void determineJourneyWarnings_journeyBackMissing_Warning() {
        ArrayList<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 1, 7, 10, 0),
                        LocalDateTime.of(2020, 1, 7, 11, 0),
                        Task.REISEN,
                        JourneyDirection.TO_AIM));
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 7, 11, 0),
                        LocalDateTime.of(2020, 1, 7, 16, 0),
                        Task.BEARBEITEN));

        List<JourneyWarning> warnings = warningCalculator.determineJourneyWarnings(projectTimes);
        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertEquals(1, warnings.get(0).getWarnings().size()));
    }

    @Test
    void determineJourneyWarnings_TwoJourneyToAimMissingAndTwoJourneyBackMissing_Warning() {
        List<ProjectTimeEntry> projectTimes = createJourneyEntriesWithFourJourneyWarnings();

        String missingJourneyBack = "Warnung: Rückreise fehlt oder ist nach dem Zeitraum";
        Mockito.when(resourceBundle.getString("warning." + Warning.WARNING_JOURNEY_BACK_MISSING.name())).thenReturn(missingJourneyBack);

        String missingJourneyToAim = "Warnung: Hinreise fehlt oder ist vor dem Zeitraum";
        Mockito.when(resourceBundle.getString("warning." + Warning.WARNING_JOURNEY_TO_AIM_MISSING.name())).thenReturn(missingJourneyToAim);

        List<JourneyWarning> warnings = warningCalculator.determineJourneyWarnings(projectTimes);
        assertAll(
                () -> assertEquals(4, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 7, 21), warnings.get(0).getDate()),
                () -> assertEquals(1, warnings.get(0).getWarnings().size()),
                () -> assertEquals(missingJourneyToAim, warnings.get(0).getWarnings().get(0)),

                () -> assertEquals(LocalDate.of(2020, 7, 24), warnings.get(1).getDate()),
                () -> assertEquals(1, warnings.get(1).getWarnings().size()),
                () -> assertEquals(missingJourneyBack, warnings.get(1).getWarnings().get(0)),

                () -> assertEquals(LocalDate.of(2020, 7, 28), warnings.get(2).getDate()),
                () -> assertEquals(1, warnings.get(2).getWarnings().size()),
                () -> assertEquals(missingJourneyToAim, warnings.get(2).getWarnings().get(0)),

                () -> assertEquals(LocalDate.of(2020, 7, 29), warnings.get(3).getDate()),
                () -> assertEquals(1, warnings.get(3).getWarnings().size()),
                () -> assertEquals(missingJourneyBack, warnings.get(3).getWarnings().get(0))
        );
    }

    @Test
    void determineJourneyWarnings_TwoWarningsOnSameDay() {
        List<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 1, 7, 10, 0),
                        LocalDateTime.of(2020, 1, 7, 11, 0),
                        Task.REISEN,
                        JourneyDirection.FURTHER));
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 1, 7, 11, 0),
                        LocalDateTime.of(2020, 1, 7, 12, 0),
                        Task.REISEN,
                        JourneyDirection.BACK));

        String missingJourneyToAim = "Warnung: Hinreise fehlt oder ist vor dem Zeitraum";
        Mockito.when(resourceBundle.getString("warning." + Warning.WARNING_JOURNEY_TO_AIM_MISSING.name())).thenReturn(missingJourneyToAim);

        List<JourneyWarning> warnings = warningCalculator.determineJourneyWarnings(projectTimes);
        assertAll(
                () -> assertEquals(1, warnings.size()),
                () -> assertEquals(2, warnings.get(0).getWarnings().size()),
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertEquals(missingJourneyToAim, warnings.get(0).getWarnings().get(0)),
                () -> assertEquals(missingJourneyToAim, warnings.get(0).getWarnings().get(1))
        );
    }

    private static List<ProjectTimeEntry> createEntriesMoreThan6Hours() {
        return Arrays.asList(
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

    /**
     * This method creates journey entries and some project time entries to simulate a month with
     * 4 invalid journey entries that have to be detected by the {@code WarningCalculator}.
     * The aim is to cover as much cases that can occur as possible to guarantee a trustful detection of invalid
     * journey entries
     *
     * @return A list that consists of project time entries distributed over 7 business days.
     */
    private static List<ProjectTimeEntry> createJourneyEntriesWithFourJourneyWarnings() {
        List<ProjectTimeEntry> projectTimes = new ArrayList<>();

        // Day 1 (TO_AIM missing)
        // JourneyEntry with JourneyDirection set to BACK which is invalid because no journey TO_AIM booked before
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 7, 21, 15, 0),
                        LocalDateTime.of(2020, 7, 21, 16, 0),
                        Task.REISEN,
                        JourneyDirection.BACK));

        // Day 2 (valid)
        // Just a usual day with journey TO_AIM and BACK to check correct working of the algorithm
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 7, 22, 8, 0),
                        LocalDateTime.of(2020, 7, 22, 9, 0),
                        Task.REISEN,
                        JourneyDirection.TO_AIM));
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 7, 22, 15, 0),
                        LocalDateTime.of(2020, 7, 22, 16, 0),
                        Task.REISEN,
                        JourneyDirection.BACK));

        // Day 3 (BACK missing)
        // JourneyEntry with JourneyDirection set to TO_AIM which is valid
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 7, 23, 8, 0),
                        LocalDateTime.of(2020, 7, 23, 9, 0),
                        Task.REISEN,
                        JourneyDirection.TO_AIM));

        // Day 4 (valid)
        // A usual day with journey TO_AIM and BACK but has to recognize that the BACK-journey is missing on the day before
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 7, 24, 15, 0),
                        LocalDateTime.of(2020, 7, 24, 16, 0),
                        Task.REISEN,
                        JourneyDirection.TO_AIM));
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 7, 24, 16, 0),
                        LocalDateTime.of(2020, 7, 24, 17, 0),
                        Task.REISEN,
                        JourneyDirection.BACK));

        // Day 5 (TO_AIM missing)
        // JourneyEntry with JourneyDirection set to BACK which is invalid because the most recent JourneyEntry is a TO_AIM-journey
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 7, 28, 15, 0),
                        LocalDateTime.of(2020, 7, 28, 16, 0),
                        Task.REISEN,
                        JourneyDirection.BACK));

        // Day 6 (BACK missing)
        // Last JourneyEntry for this month with JourneyDirection set to TO_AIM which is valid at this point
        projectTimes.add(
                new JourneyEntry(LocalDateTime.of(2020, 7, 29, 15, 0),
                        LocalDateTime.of(2020, 7, 29, 16, 0),
                        Task.REISEN,
                        JourneyDirection.TO_AIM));

        // ProjectTimeEntry which is the very last entry in the whole month. Even though this is not a JourneyEntry
        // If at this time the most recent journey is not finsihed yet (JourneyDirection set to BACK in current entry),
        // it has to be detected that there is a invalid journey on Day 6
        projectTimes.add(
                new ProjectTimeEntry(LocalDateTime.of(2020, 7, 30, 15, 0),
                        LocalDateTime.of(2020, 7, 30, 16, 0),
                        Task.BEARBEITEN));

        return projectTimes;
    }
}
