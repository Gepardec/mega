package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.Vehicle;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InsufficientBreakTimeCalculatorTest {

    private InsufficientBreakCalculator calculator;

    @BeforeEach
    void beforeEach() {
        calculator = new InsufficientBreakCalculator();
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startHour, final int endHour) {
        return projectTimeEntryFor(startHour, 0, endHour, 0);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.BEARBEITEN)
                .workingLocation(WorkingLocation.MAIN)
                .build();
    }

    private JourneyTimeEntry journeyTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute) {
        return JourneyTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(WorkingLocation.MAIN)
                .journeyDirection(JourneyDirection.TO)
                .vehicle(Vehicle.OTHER_INACTIVE)
                .build();
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
    void when6Hours_thenNoWarning() {
        final ProjectTimeEntry timeEntry = projectTimeEntryFor(7, 13);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntry));

        assertThat(warnings).isEmpty();
    }

    @Test
    void when3And6HoursAnd30MinBreakTime_thenNoWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 30, 16, 0);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertThat(warnings).isEmpty();
    }

    @Test
    void when3And6HoursAnd1HBreakTimeActiveJourney_thenNoWarning() {
        final JourneyTimeEntry journeyEntryOne = journeyTimeEntryFor(7, 10, Vehicle.CAR_ACTIVE);
        final JourneyTimeEntry journeyEntryTwo = journeyTimeEntryFor(11, 17, Vehicle.CAR_ACTIVE);

        final List<TimeWarning> warnings = calculator.calculate(List.of(journeyEntryOne,journeyEntryTwo));

        assertThat(warnings).isEmpty();
    }

    @Test
    void when3And6HoursAndNoBreakTimeActiveJourney_thenWarning() {
        final JourneyTimeEntry journeyEntryOne = journeyTimeEntryFor(7, 10, Vehicle.CAR_ACTIVE);
        final JourneyTimeEntry journeyEntryTwo = journeyTimeEntryFor(10, 16, Vehicle.CAR_ACTIVE);

        final List<TimeWarning> warnings = calculator.calculate(List.of(journeyEntryOne,journeyEntryTwo));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.5);
    }


    @Test
    void when3EntriesAndTwo30MinutesBreak_thenNoWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 15, 12, 15);
        final ProjectTimeEntry timeEntryThree = projectTimeEntryFor(12, 30, 15, 30);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertThat(warnings).isEmpty();
    }

    @Test
    void when4EntriesAnd30MinBreakAndWithOneJourneyTimeEntry_thenWarningButIgnoredJourneyTimeEntry() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 30, 17, 15);
        final JourneyTimeEntry timeEntryThree = journeyTimeEntryFor(17, 15, 19, 30);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertThat(warnings).isEmpty();
    }

    @Test
    void whenDataListEmpty_thenNoWarningsCreated() {
        assertThat(calculator.calculate(List.of())).isEmpty();
    }

    @Test
    void whenWarning_thenOnlyMissingBreakTimeSet() {
        final ProjectTimeEntry timeEntry = projectTimeEntryFor(7, 14);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntry));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.5);

        assertThat(warnings).hasSize(1);
        final TimeWarning warning = warnings.get(0);
        assertThat(warning.getDate()).isNotNull();
        assertThat(warning.getMissingBreakTime()).isNotNull();
        assertThat(warning.getMissingRestTime()).isNull();
        assertThat(warning.getExcessWorkTime()).isNull();
    }

    @Test
    void whenUnordered_thenOrdered() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 16);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryTwo, timeEntryOne));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.5);
    }

    @Test
    void when7Hours_thenWarning() {
        final ProjectTimeEntry timeEntry = projectTimeEntryFor(7, 14);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntry));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.5);
    }

    @Test
    void when3And6HoursAndNoBreakTime_thenWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 16);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.5);
    }

    @Test
    void when3And6HoursAnd15MinBreakTime_thenWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 15, 16, 15);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.25);
    }

    @Test
    void when3EntriesAndTwo20MinutesBreak_thenWarning() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 10, 12, 10);
        final ProjectTimeEntry timeEntryThree = projectTimeEntryFor(12, 20, 15, 20);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.16);
    }

    @Test
    void when4EntriesAnd15MinBreakAndWithOneJourneyTimeEntry_thenWarningButIgnoredJourneyTimeEntry() {
        final ProjectTimeEntry timeEntryOne = projectTimeEntryFor(7, 10);
        final ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(10, 15, 16, 45);
        final JourneyTimeEntry timeEntryThree = journeyTimeEntryFor(16, 45, 19, 15);

        final List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.25);
    }
}
