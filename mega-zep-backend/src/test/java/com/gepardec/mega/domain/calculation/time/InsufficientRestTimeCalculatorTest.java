package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.Vehicle;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InsufficientRestTimeCalculatorTest {

    private final InsufficientRestCalculator calculator = new InsufficientRestCalculator();

    @Test
    void calculate_whenDataListEmpty_thenNoWarningsCreated() {
        assertThat(calculator.calculate(Collections.emptyList())).isEmpty();
    }

    private ProjectTimeEntry projectTimeEntryFor(int day, int startHour, int startMinute, int endHour, int endMinute) {
        return projectTimeEntryFor(day, startHour, startMinute, day, endHour, endMinute);
    }

    private ProjectTimeEntry projectTimeEntryFor(int startDay, int startHour, int startMinute, int endDay, int endHour, int endMinute) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, startDay, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, endDay, endHour, endMinute))
                .task(Task.BEARBEITEN)
                .workingLocation(WorkingLocation.MAIN)
                .build();
    }

    private JourneyTimeEntry journeyTimeEntryFor(int startDay, int startHour, int startMinute, int endDay, int endHour, int endMinute, Vehicle vehicle) {
        return JourneyTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, startDay, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, endDay, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(WorkingLocation.MAIN)
                .journeyDirection(JourneyDirection.TO)
                .vehicle(vehicle)
                .build();
    }

    @Test
    void whenDataListEmpty_thenNoWarningsCreated() {
        assertThat(calculator.calculate(Collections.emptyList())).isEmpty();
    }

    @Test
    void whenWarning_thenOnlyMissingRestTimeSet() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(2, 8, 0, 11, 0);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertThat(warnings).hasSize(1);
        TimeWarning warning = warnings.get(0);
        assertThat(warning.getDate()).isNotNull();
        assertThat(warning.getMissingRestTime()).isNotNull();
        assertThat(warning.getExcessWorkTime()).isNull();
        assertThat(warning.getMissingBreakTime()).isNull();
    }

    @Test
    void whenUnordered_thenOrdered() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(2, 8, 0, 11, 0);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryTwo, timeEntryOne));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingRestTime()).isEqualTo(1);
    }

    @Test
    void when10HoursRestTime_thenWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(2, 8, 0, 11, 0);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingRestTime()).isEqualTo(1);
    }

    @Test
    void whenOneJourneyEntryAnd10HoursRestTime_thenWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
        JourneyTimeEntry timeEntryTwo = journeyTimeEntryFor(1, 22, 0, 2, 1, 0, Vehicle.CAR_INACTIVE);
        ProjectTimeEntry timeEntryThree = projectTimeEntryFor(2, 8, 0, 11, 0);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingRestTime()).isEqualTo(1);
    }

    @Test
    void whenOneJourneyEntryActiveAnd10HoursRestTime_thenWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
        JourneyTimeEntry timeEntryTwo = journeyTimeEntryFor(1, 22, 0, 2, 1, 0, Vehicle.CAR_ACTIVE);
        ProjectTimeEntry timeEntryThree = projectTimeEntryFor(2, 8, 0, 11, 0);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getMissingRestTime()).isEqualTo(1);
    }

    @Test
    void whenJourneyEntryActiveAnd10HoursRestTime_thenWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
        JourneyTimeEntry timeEntryTwo = journeyTimeEntryFor(2, 8, 0, 2, 9, 0, Vehicle.CAR_ACTIVE);


        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertThat(warnings).isEmpty();
    }

    @Test
    void when11HoursRestTime_thenNoWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
        ProjectTimeEntry timeEntryTwo = projectTimeEntryFor(2, 9, 0, 11, 0);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo));

        assertThat(warnings).isEmpty();
    }

    @Test
    void whenOneJourneyEntryAnd11HoursRestTime_thenNoWarning() {
        ProjectTimeEntry timeEntryOne = projectTimeEntryFor(1, 16, 0, 22, 0);
        JourneyTimeEntry timeEntryTwo = journeyTimeEntryFor(1, 22, 0, 2, 1, 0, Vehicle.CAR_INACTIVE);
        ProjectTimeEntry timeEntryThree = projectTimeEntryFor(2, 9, 0, 11, 0);

        List<TimeWarning> warnings = calculator.calculate(List.of(timeEntryOne, timeEntryTwo, timeEntryThree));

        assertThat(warnings).isEmpty();
    }
}
