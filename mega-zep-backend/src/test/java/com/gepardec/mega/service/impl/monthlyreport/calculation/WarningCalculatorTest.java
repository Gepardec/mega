package com.gepardec.mega.service.impl.monthlyreport.calculation;

import com.gepardec.mega.domain.model.monthlyreport.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarningCalculatorTest {

    @InjectMocks
    WarningCalculator warningCalculator;

    @Mock
    ResourceBundle messages;

    @Test
    void determineJourneyWarnings_validateWarningStrings() {

        List<JourneyTimeEntry> projectTimeEntries = List.of(
                JourneyTimeEntry.of(LocalDateTime.now(), LocalDateTime.now().plusHours(2), Task.REISEN, JourneyDirection.BACK),
                JourneyTimeEntry.of(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5), Task.REISEN, JourneyDirection.TO)
        );

        when(messages.getString("warning.JOURNEY_TO_MISSING")).thenReturn("JOURNEY_TO_MISSING");
        when(messages.getString("warning.JOURNEY_BACK_MISSING")).thenReturn("JOURNEY_BACK_MISSING");

        List<JourneyWarning> warnings = warningCalculator.determineJourneyWarnings(projectTimeEntries);

        assertAll(
                () -> assertEquals(1, warnings.size()),
                () -> assertEquals(2, warnings.get(0).getWarnings().size()),
                () -> assertEquals("JOURNEY_TO_MISSING", warnings.get(0).getWarnings().get(0)),
                () -> assertEquals("JOURNEY_BACK_MISSING", warnings.get(0).getWarnings().get(1))
        );
    }

    private List<ProjectTimeEntry> createEntriesMoreThan6Hours() {
        return List.of(
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 7, 9, 0),
                        LocalDateTime.of(2020, 1, 7, 16, 0),
                        Task.BEARBEITEN));
    }

    private List<ProjectTimeEntry> createEntriesMoreThan6HoursWithTravelTime() {
        return List.of(
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 7, 9, 0),
                        LocalDateTime.of(2020, 1, 7, 14, 0),
                        Task.BEARBEITEN),
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 7, 16, 0),
                        LocalDateTime.of(2020, 1, 7, 17, 0),
                        Task.BEARBEITEN));
    }


    private List<ProjectTimeEntry> createEntriesMoreThan10HoursADay() {
        List<ProjectTimeEntry> projectTimes = new ArrayList<>();
        projectTimes.add(
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 7, 7, 0),
                        LocalDateTime.of(2020, 1, 7, 10, 0),
                        Task.BEARBEITEN));
        projectTimes.add(
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 7, 10, 30),
                        LocalDateTime.of(2020, 1, 7, 17, 45),
                        Task.BEARBEITEN));
        projectTimes.add(
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 7, 18, 0),
                        LocalDateTime.of(2020, 1, 7, 22, 0),
                        Task.BEARBEITEN));
        return projectTimes;
    }


}
