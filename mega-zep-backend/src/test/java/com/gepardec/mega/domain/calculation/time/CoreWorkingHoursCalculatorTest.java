package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;
import com.gepardec.mega.domain.model.monthlyreport.Vehicle;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoreWorkingHoursCalculatorTest {

    private CoreWorkingHoursCalculator calculator;

    @BeforeEach
    void init() {
        calculator = new CoreWorkingHoursCalculator();
    }

    private ProjectTimeEntry projectTimeEntryFor(int startHour, int endHour) {
        return projectTimeEntryFor(startHour, 0, endHour, 0);
    }

    private ProjectTimeEntry projectTimeEntryFor(int startHour, int startMinute, int endHour, int endMinute) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.BEARBEITEN)
                .workingLocation(WorkingLocation.MAIN).build();
    }

    private JourneyTimeEntry journeyTimeEntryFor(int startHour, int endHour, Vehicle vehicle) {
        return journeyTimeEntryFor(startHour, 0, endHour, 0, vehicle);
    }

    private JourneyTimeEntry journeyTimeEntryFor(int startHour, int startMinute, int endHour, int endMinute, Vehicle vehicle) {
        return JourneyTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(WorkingLocation.MAIN)
                .journeyDirection(JourneyDirection.TO)
                .vehicle(vehicle)
                .build();
    }

    @Test
    void whenUnordered_thenNoWarning() {
        JourneyTimeEntry travelBefore = journeyTimeEntryFor(1, 6, Vehicle.OTHER_INACTIVE);
        ProjectTimeEntry start = projectTimeEntryFor(6, 12);
        ProjectTimeEntry end = projectTimeEntryFor(13, 22);
        JourneyTimeEntry travelAfter = journeyTimeEntryFor(22, 23, Vehicle.OTHER_INACTIVE);

        List<TimeWarning> result = calculator.calculate(List.of(travelAfter, end, start, travelBefore));

        assertTrue(result.isEmpty());
    }

    @Test
    void whenWarning_thenTimeWarningTypeSet() {
        ProjectTimeEntry start = projectTimeEntryFor(5, 12);
        ProjectTimeEntry end = projectTimeEntryFor(13, 16);

        List<TimeWarning> result = calculator.calculate(List.of(start, end));

        assertFalse(result.isEmpty(), "Warnings should have been detected");
        assertEquals(1, result.get(0).getWarningTypes().size(), "One WarningType should have been set");
        assertEquals(TimeWarningType.OUTSIDE_CORE_WORKING_TIME, result.get(0).getWarningTypes().get(0));
    }

    @Test
    void whenStartedToEarlyAt5_thenWarning() {
        ProjectTimeEntry start = projectTimeEntryFor(5, 12);
        ProjectTimeEntry end = projectTimeEntryFor(13, 16);

        List<TimeWarning> result = calculator.calculate(List.of(start, end));

        assertEquals(1, result.size());
    }

    @Test
    void whenStartedToEarlyAndBothActiveTravel_thenWarning() {
        JourneyTimeEntry start = journeyTimeEntryFor(5, 12, Vehicle.CAR_ACTIVE);
        JourneyTimeEntry end = journeyTimeEntryFor(13, 16, Vehicle.CAR_ACTIVE);

        List<TimeWarning> result = calculator.calculate(List.of(start, end));

        assertEquals(1, result.size());
    }

    @Test
    void whenStartedToEarlyAt5AndStoppedToLateAt23_thenWarning() {
        ProjectTimeEntry start = projectTimeEntryFor(5, 12);
        ProjectTimeEntry end = projectTimeEntryFor(18, 23);

        List<TimeWarning> result = calculator.calculate(List.of(start, end));

        assertEquals(1, result.size());
    }

    @Test
    void whenInactiveTravelerOnJourneyAndStartedToEarly_thenWarning() {
        JourneyTimeEntry start = journeyTimeEntryFor(3, 4, Vehicle.OTHER_INACTIVE);
        ProjectTimeEntry end = projectTimeEntryFor(5, 8);

        List<TimeWarning> result = calculator.calculate(List.of(start, end));

        assertEquals(1, result.size());
    }

    @Test
    void whenActiveTravelerOnJourneyAndStartedToEarly_thenWarning() {
        JourneyTimeEntry start = journeyTimeEntryFor(3, 6, Vehicle.CAR_ACTIVE);

        List<TimeWarning> result = calculator.calculate(List.of(start));

        assertEquals(1, result.size());
    }

    @Test
    void whenDataListEmpty_thenNoWarnings() {
        assertTrue(calculator.calculate(List.of()).isEmpty());
    }

    @Test
    void whenStoppedToLateAt23_thenWarning() {
        ProjectTimeEntry start = projectTimeEntryFor(6, 12);
        ProjectTimeEntry end = projectTimeEntryFor(18, 23);

        List<TimeWarning> result = calculator.calculate(List.of(start, end));

        assertEquals(1, result.size());
    }

    @Test
    void whenValid_thenNoWarning() {
        JourneyTimeEntry travelInactiveBefore = journeyTimeEntryFor(1, 6, Vehicle.OTHER_INACTIVE);
        JourneyTimeEntry travelActiveBefore = journeyTimeEntryFor(6, 10, Vehicle.CAR_ACTIVE);
        ProjectTimeEntry start = projectTimeEntryFor(10, 12);
        ProjectTimeEntry end = projectTimeEntryFor(13, 16);
        JourneyTimeEntry travelActiveAfter = journeyTimeEntryFor(17, 22, Vehicle.CAR_ACTIVE);
        JourneyTimeEntry travelInactiveAfter = journeyTimeEntryFor(22, 23, Vehicle.OTHER_INACTIVE);

        List<TimeWarning> result = calculator
                .calculate(List.of(travelInactiveBefore, travelActiveBefore, start, end, travelActiveAfter, travelInactiveAfter));

        assertTrue(result.isEmpty());
    }

    @Test
    void whenOnlyInactiveTravelerOnJourney_thenNoWarning() {
        JourneyTimeEntry start = journeyTimeEntryFor(3, 10, Vehicle.OTHER_INACTIVE);
        JourneyTimeEntry end = journeyTimeEntryFor(10, 23, Vehicle.OTHER_INACTIVE);

        List<TimeWarning> result = calculator.calculate(List.of(start, end));

        assertTrue(result.isEmpty());
    }
}
