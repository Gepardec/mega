package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarningType;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.Vehicle;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvalidWorkingLocationInJourneyCalculatorTest {

    private InvalidWorkingLocationInJourneyCalculator calculator;

    @BeforeEach
    void init() {
        calculator = new InvalidWorkingLocationInJourneyCalculator();
    }

    private ProjectTimeEntry projectTimeEntryFor(int startHour, int endHour, WorkingLocation workingLocation) {
        return projectTimeEntryFor(startHour, 0, endHour, 0, workingLocation);
    }

    private ProjectTimeEntry projectTimeEntryFor(int startHour, int startMinute, int endHour, int endMinute, WorkingLocation workingLocation) {

        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.BEARBEITEN)
                .workingLocation(workingLocation)
                .build();
    }

    private JourneyTimeEntry journeyTimeEntryFor(int startHour, int endHour, JourneyDirection direction, WorkingLocation workingLocation) {
        return journeyTimeEntryFor(startHour, 0, endHour, 0, direction, workingLocation);
    }

    private JourneyTimeEntry journeyTimeEntryFor(int startHour, int startMinute, int endHour, int endMinute, JourneyDirection direction, WorkingLocation workingLocation) {
        return JourneyTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(workingLocation)
                .journeyDirection(direction)
                .vehicle(Vehicle.OTHER_INACTIVE)
                .build();
    }

    @Test
    void whenOneProjectTimeEntryWithinJourneyWithWorkingLocationMain_thenWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.P);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree));

        assertEquals(1, warnings.size());
        assertEquals(1, warnings.get(0).getWarningTypes().size());
        assertEquals(JourneyWarningType.INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
    }

    @Test
    void whenOneProjectTimeEntryWithinJourneyWithDifferentWorkingLocation_thenWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.CH);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree));

        assertEquals(1, warnings.size());
        assertEquals(1, warnings.get(0).getWarningTypes().size());
        assertEquals(JourneyWarningType.INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
    }

    @Test
    void whenTwoProjectTimeEntryWithinJourneyWithWorkingLocationMain_thenOneWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.MAIN);
        ProjectEntry projectEntryThree = projectTimeEntryFor(9, 10, WorkingLocation.MAIN);
        JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

        assertEquals(1, warnings.size());
        assertEquals(1, warnings.get(0).getWarningTypes().size());
        assertEquals(JourneyWarningType.INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
    }

    @Test
    void whenTwoProjectTimeEntryWithinJourneyWithOneInvalidWorkingLocation_thenOneWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
        ProjectEntry projectEntryThree = projectTimeEntryFor(9, 10, WorkingLocation.MAIN);
        JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

        assertEquals(1, warnings.size());
        assertEquals(1, warnings.get(0).getWarningTypes().size());
        assertEquals(JourneyWarningType.INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
    }

    @Test
    void whenTwoProjectTimeEntryWithinTwoJourneysWithOneInvalidWorkingLocation_thenOneWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.P);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.TO, WorkingLocation.P);
        ProjectEntry projectEntryFive = projectTimeEntryFor(11, 12, WorkingLocation.P);
        JourneyTimeEntry journeyTimeEntrySix = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree, journeyTimeEntryFour, projectEntryFive,
                        journeyTimeEntrySix));

        assertEquals(1, warnings.size());
        assertEquals(1, warnings.get(0).getWarningTypes().size());
        assertEquals(JourneyWarningType.INVALID_WORKING_LOCATION, warnings.get(0).getWarningTypes().get(0));
    }

    @Test
    void whenProjectTimeEntryWithWorkingLocationMAINAfterJourneyBackWithWorkingLocationA_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(8, 10, JourneyDirection.TO, WorkingLocation.A);
        ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(10, 11, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);
        ProjectTimeEntry projectTimeEntryFour = projectTimeEntryFor(15, 16, WorkingLocation.MAIN);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree, projectTimeEntryFour));

        assertEquals(0, warnings.size());
    }

    @Test
    void whenProjectTimeEntryWithWorkingLocationAAfterJourneyBackWithWorkingLocationA_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(8, 10, JourneyDirection.TO, WorkingLocation.A);
        ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(10, 11, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);
        ProjectTimeEntry projectTimeEntryFour = projectTimeEntryFor(15, 16, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree, projectTimeEntryFour));

        assertEquals(0, warnings.size());
    }

    @Test
    void whenOneProjectTimeEntryWithinJourney_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree));

        assertTrue(warnings.isEmpty());
    }

    @Test
    void whenTwoProjectTimeEntryWithinJourney_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
        ProjectEntry projectEntryThree = projectTimeEntryFor(9, 10, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

        assertTrue(warnings.isEmpty());
    }

    @Test
    void whenTwoProjectTimeEntryWithinTwoJourneys_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.TO, WorkingLocation.P);
        ProjectEntry projectEntryFive = projectTimeEntryFor(11, 12, WorkingLocation.P);
        JourneyTimeEntry journeyTimeEntrySix = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree, journeyTimeEntryFour, projectEntryFive,
                        journeyTimeEntrySix));

        assertTrue(warnings.isEmpty());
    }
}
