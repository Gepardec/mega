package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes()).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes().get(0)).isEqualTo(JourneyWarningType.INVALID_WORKING_LOCATION);
    }

    @Test
    void whenOneProjectTimeEntryWithinJourneyWithDifferentWorkingLocation_thenWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.CH);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes()).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes().get(0)).isEqualTo(JourneyWarningType.INVALID_WORKING_LOCATION);
    }

    /*
        Comment disabled because:
                       journeys grouped in days     ->      now grouped in months
                       this allows journeys over multiple days
                       this prevents to group multiple warnings on one day
                        -without implementing 'complex' logic
         */
    @Disabled
    @Test
    void whenTwoProjectTimeEntryWithinJourneyWithWorkingLocationMain_thenOneWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.MAIN);
        ProjectEntry projectEntryThree = projectTimeEntryFor(9, 10, WorkingLocation.MAIN);
        JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes()).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes().get(0)).isEqualTo(JourneyWarningType.INVALID_WORKING_LOCATION);
    }

    @Test
    void whenTwoProjectTimeEntryWithinJourneyWithOneInvalidWorkingLocation_thenOneWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
        ProjectEntry projectEntryThree = projectTimeEntryFor(9, 10, WorkingLocation.MAIN);
        JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes()).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes().get(0)).isEqualTo(JourneyWarningType.INVALID_WORKING_LOCATION);
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

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes()).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes().get(0)).isEqualTo(JourneyWarningType.INVALID_WORKING_LOCATION);
    }

    @Test
    void whenProjectTimeEntryWithWorkingLocationMAINAfterJourneyBackWithWorkingLocationA_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(8, 10, JourneyDirection.TO, WorkingLocation.A);
        ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(10, 11, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);
        ProjectTimeEntry projectTimeEntryFour = projectTimeEntryFor(15, 16, WorkingLocation.MAIN);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree, projectTimeEntryFour));

        assertThat(warnings).isEmpty();
    }

    @Test
    void whenProjectTimeEntryWithWorkingLocationAAfterJourneyBackWithWorkingLocationA_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(8, 10, JourneyDirection.TO, WorkingLocation.A);
        ProjectTimeEntry projectTimeEntryTwo = projectTimeEntryFor(10, 11, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);
        ProjectTimeEntry projectTimeEntryFour = projectTimeEntryFor(15, 16, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectTimeEntryTwo, journeyTimeEntryThree, projectTimeEntryFour));

        assertThat(warnings).hasSize(1);
    }

    @Test
    void whenOneProjectTimeEntryWithinJourney_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(9, 10, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator.calculate(List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree));

        assertThat(warnings).isEmpty();
    }

    @Test
    void whenTwoProjectTimeEntryWithinJourney_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 8, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(8, 9, WorkingLocation.A);
        ProjectEntry projectEntryThree = projectTimeEntryFor(9, 10, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(10, 11, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

        assertThat(warnings).isEmpty();
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

        assertThat(warnings).isEmpty();
    }

    @Test
    void whenProjectEntryAndJourneyEntryHaveSameStartHourSort_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 7, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryOne = projectTimeEntryFor(7, 8, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(11, 12, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryTwo = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(journeyTimeEntryOne, projectEntryOne, projectEntryTwo, journeyTimeEntryTwo));
        assertThat(warnings).isEmpty();
    }

    @Test
    void whenProjectEntryAndJourneyEntryHaveSameStartHourSortReversed_thenNoWarning() {
        JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 7, JourneyDirection.TO, WorkingLocation.A);
        ProjectEntry projectEntryOne = projectTimeEntryFor(7, 8, WorkingLocation.A);
        ProjectEntry projectEntryTwo = projectTimeEntryFor(11, 12, WorkingLocation.A);
        JourneyTimeEntry journeyTimeEntryTwo = journeyTimeEntryFor(12, 13, JourneyDirection.BACK, WorkingLocation.A);

        List<JourneyWarning> warnings = calculator
                .calculate(List.of(projectEntryOne, journeyTimeEntryOne, journeyTimeEntryTwo, projectEntryTwo));
        assertThat(warnings).isEmpty();
    }
}
