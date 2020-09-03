package com.gepardec.mega.service.impl.monthlyreport.calculation.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.*;
import com.gepardec.mega.service.impl.monthlyreport.calculation.journey.JourneyWarningCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JourneyWarningCalculatorTest {

    private JourneyWarningCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new JourneyWarningCalculator();
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

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {
        assertEquals(Collections.emptyList(), calculator.calculate(Collections.emptyList()));
    }

    @Test
    void calculate_whenDataNotForTheCalculator_thenNoWarningsCreated() {
        List<ProjectTimeEntry> projectTimeEntries = new ArrayList<>();
        projectTimeEntries.addAll(Arrays.asList(
                new ProjectTimeEntry(LocalDateTime.now(), LocalDateTime.now().plusHours(7), Task.BEARBEITEN)
        ));

        assertEquals(Collections.emptyList(), calculator.calculate(projectTimeEntries));
    }

    @Test
    void calculate_whenDataIsValid_thenNoWarningsFound() {
        List<ProjectTimeEntry> projectTimeEntries = new ArrayList<>();
        projectTimeEntries.addAll(Arrays.asList(
                new JourneyEntry(LocalDateTime.now(), LocalDateTime.now().plusHours(1), Task.REISEN, JourneyDirection.TO_AIM),
                new JourneyEntry(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), Task.REISEN, JourneyDirection.BACK)
        ));

        assertEquals(Collections.emptyList(), calculator.calculate(projectTimeEntries));
    }

    @Test
    void calculate_journeyToAimMissing_Warning() {
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

        List<JourneyWarning> warnings = calculator.calculate(projectTimes);
        assertAll(
                () -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertEquals(1, warnings.get(0).getWarningTypes().size()),
                () -> assertEquals(Warning.WARNING_JOURNEY_TO_AIM_MISSING, warnings.get(0).getWarningTypes().get(0))
        );
    }

    @Test
    void calculate_journeyBackMissing_Warning() {
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

        List<JourneyWarning> warnings = calculator.calculate(projectTimes);
        assertAll(
                () -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertEquals(1, warnings.get(0).getWarningTypes().size()),
                () -> assertEquals(Warning.WARNING_JOURNEY_BACK_MISSING, warnings.get(0).getWarningTypes().get(0)));
    }

    @Test
    void calculate_TwoJourneyToAimMissingAndTwoJourneyBackMissing_Warning() {
        List<ProjectTimeEntry> projectTimes = createJourneyEntriesWithFourJourneyWarnings();

        List<JourneyWarning> warnings = calculator.calculate(projectTimes);
        assertAll(
                () -> assertEquals(4, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 7, 21), warnings.get(0).getDate()),
                () -> assertEquals(1, warnings.get(0).getWarningTypes().size()),
                () -> assertEquals(Warning.WARNING_JOURNEY_TO_AIM_MISSING, warnings.get(0).getWarningTypes().get(0)),

                () -> assertEquals(LocalDate.of(2020, 7, 24), warnings.get(1).getDate()),
                () -> assertEquals(1, warnings.get(1).getWarningTypes().size()),
                () -> assertEquals(Warning.WARNING_JOURNEY_BACK_MISSING, warnings.get(1).getWarningTypes().get(0)),

                () -> assertEquals(LocalDate.of(2020, 7, 28), warnings.get(2).getDate()),
                () -> assertEquals(1, warnings.get(2).getWarningTypes().size()),
                () -> assertEquals(Warning.WARNING_JOURNEY_TO_AIM_MISSING, warnings.get(2).getWarningTypes().get(0)),

                () -> assertEquals(LocalDate.of(2020, 7, 29), warnings.get(3).getDate()),
                () -> assertEquals(1, warnings.get(3).getWarningTypes().size()),
                () -> assertEquals(Warning.WARNING_JOURNEY_BACK_MISSING, warnings.get(3).getWarningTypes().get(0))
        );
    }

    @Test
    void calculate_ToAimMissingWhenFirstEntryEqualsFurther_Warning() {
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

        List<JourneyWarning> warnings = calculator.calculate(projectTimes);
        assertAll(
                () -> assertEquals(1, warnings.size()),
                () -> assertEquals(1, warnings.get(0).getWarningTypes().size()),
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertEquals(Warning.WARNING_JOURNEY_TO_AIM_MISSING, warnings.get(0).getWarningTypes().get(0))
        );
    }
}
