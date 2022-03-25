package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarningType;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.Vehicle;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import com.gepardec.mega.service.helper.WarningCalculator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarningCalculatorTest {

    private static final Integer[] HOLIDAYS = {8, 25, 26};

    private static final Integer[] WEEKEND_DAYS = {5, 6, 12, 13, 19, 20, 26, 27};

    @Mock
    ResourceBundle messages;

    @InjectMocks
    WarningCalculator calculator;

    private ProjectTimeEntry projectTimeEntryFor(final int startHour, final int endHour) {
        return projectTimeEntryFor(2, startHour, 0, 1, endHour, 0, WorkingLocation.MAIN);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int day, final int startHour, final int endHour) {
        return projectTimeEntryFor(day, startHour, 0, day, endHour, 0, WorkingLocation.MAIN);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int day, final int startHour, final int startMinute, final int endHour,
                                                 final int endMinute) {
        return projectTimeEntryFor(day, startHour, startMinute, day, endHour, endMinute, WorkingLocation.MAIN);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int day, final int startHour, final int endHour, WorkingLocation workingLocation) {
        return projectTimeEntryFor(day, startHour, 0, day, endHour, 0, workingLocation);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startDay, final int startHour, final int startMinute, final int endDay, final int endHour,
                                                 final int endMinute, WorkingLocation workingLocation) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, startDay, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, endDay, endHour, endMinute))
                .task(Task.BEARBEITEN)
                .workingLocation(workingLocation)
                .build();
    }

    private JourneyTimeEntry journeyTimeEntryFor(final int day, final int startHour, final int endHour, JourneyDirection journeyDirection,
                                                 WorkingLocation workingLocation, Vehicle vehicle) {
        return journeyTimeEntryFor(day, startHour, 0, day, endHour, 0, journeyDirection, workingLocation, vehicle);
    }

    private JourneyTimeEntry journeyTimeEntryFor(final int startDay, final int startHour, final int startMinute, final int endDay, final int endHour,
                                                 final int endMinute,
                                                 JourneyDirection journeyDirection, WorkingLocation workingLocation, Vehicle vehicle) {
        return JourneyTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 1, startDay, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, endDay, endHour, endMinute))
                .task(Task.REISEN)
                .journeyDirection(journeyDirection)
                .workingLocation(workingLocation)
                .vehicle(vehicle)
                .build();
    }

    @Test
    void whenUnordered_thenOrdered2() {

        final ProjectTimeEntry start = projectTimeEntryFor(6, 10);
        final ProjectTimeEntry end = projectTimeEntryFor(18, 22);

        final List<TimeWarning> result = calculator.determineTimeWarnings(List.of(end, start));

        assertThat(result).isEmpty();
    }

    @Test
    void whenStartedToEarlyAt5AndStoppedToLateAt23_thenWarning() {
        final ProjectTimeEntry start = projectTimeEntryFor(5, 12);
        final ProjectTimeEntry end = projectTimeEntryFor(18, 23);

        final List<TimeWarning> result = calculator.determineTimeWarnings(List.of(start, end));

        assertThat(result).hasSize(1);
    }

    @Test
    void whenWarning_thenWarningTypeTranslatedToMessage() {
        final ProjectTimeEntry start = projectTimeEntryFor(5, 12);
        final ProjectTimeEntry end = projectTimeEntryFor(18, 23);

        calculator.determineTimeWarnings(List.of(start, end));

        verify(messages, times(1)).getString("warning.time.OUTSIDE_CORE_WORKING_TIME");
    }

    @Test
    void whenWarning_thenTranslatedWarning() {
        final ProjectTimeEntry start = projectTimeEntryFor(5, 12);
        final ProjectTimeEntry end = projectTimeEntryFor(18, 23);
        when(messages.getString(eq("warning.time.OUTSIDE_CORE_WORKING_TIME"))).thenReturn("WARNING_STRING");

        final List<TimeWarning> warnings = calculator.determineTimeWarnings(List.of(start, end));

        assertThat(warnings.get(0).getWarnings().get(0)).isEqualTo("WARNING_STRING");
    }

    @Test
    void whenValid_thenNoWarning() {
        final ProjectTimeEntry start = projectTimeEntryFor(6, 10);
        final ProjectTimeEntry end = projectTimeEntryFor(18, 22);

        final List<TimeWarning> result = calculator.determineTimeWarnings(List.of(start, end));

        assertThat(result).isEmpty();
    }

    @Test
    void whenUnordered_thenOrdered3() {
        final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(1, 7, 8, JourneyDirection.TO, WorkingLocation.A, Vehicle.OTHER_INACTIVE);
        final ProjectEntry projectEntryTwo = projectTimeEntryFor(1, 8, 9, WorkingLocation.MAIN);
        final ProjectEntry projectEntryThree = projectTimeEntryFor(1, 9, 10, WorkingLocation.MAIN);
        final JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(1, 10, 11, JourneyDirection.BACK, WorkingLocation.A, Vehicle.OTHER_INACTIVE);

        final List<JourneyWarning> warnings = calculator
                .determineJourneyWarnings(List.of(journeyTimeEntryFour, projectEntryThree, projectEntryTwo, journeyTimeEntryOne));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes()).hasSize(2);
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
        final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(1, 7, 8, JourneyDirection.TO, WorkingLocation.A, Vehicle.OTHER_INACTIVE);
        final ProjectEntry projectEntryTwo = projectTimeEntryFor(1, 8, 9, WorkingLocation.MAIN);
        final ProjectEntry projectEntryThree = projectTimeEntryFor(1, 9, 10, WorkingLocation.MAIN);
        final JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(1, 10, 11, JourneyDirection.BACK, WorkingLocation.A, Vehicle.OTHER_INACTIVE);

        final List<JourneyWarning> warnings = calculator
                .determineJourneyWarnings(List.of(journeyTimeEntryOne, projectEntryTwo, projectEntryThree, journeyTimeEntryFour));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes()).hasSize(1);
        assertThat(warnings.get(0).getWarningTypes().get(0)).isEqualTo(JourneyWarningType.INVALID_WORKING_LOCATION);
    }

    @Test
    void whenTwoProjectTimeEntryWithinTwoJourneys_thenNoWarning() {
        final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(1, 7, 8, JourneyDirection.TO, WorkingLocation.A, Vehicle.OTHER_INACTIVE);
        final ProjectEntry projectEntryTwo = projectTimeEntryFor(1, 8, 9, WorkingLocation.A);
        final JourneyTimeEntry journeyTimeEntryThree = journeyTimeEntryFor(1, 9, 10, JourneyDirection.BACK, WorkingLocation.A, Vehicle.OTHER_INACTIVE);
        final JourneyTimeEntry journeyTimeEntryFour = journeyTimeEntryFor(1, 10, 11, JourneyDirection.TO, WorkingLocation.P, Vehicle.OTHER_INACTIVE);
        final ProjectEntry projectEntryFive = projectTimeEntryFor(1, 11, 12, WorkingLocation.P);
        final JourneyTimeEntry journeyTimeEntrySix = journeyTimeEntryFor(1, 12, 13, JourneyDirection.BACK, WorkingLocation.A, Vehicle.OTHER_INACTIVE);

        final List<JourneyWarning> warnings = calculator
                .determineJourneyWarnings(
                        List.of(journeyTimeEntryOne, projectEntryTwo, journeyTimeEntryThree, journeyTimeEntryFour, projectEntryFive,
                                journeyTimeEntrySix));

        assertThat(warnings).isEmpty();
    }

    @Test
    void whenUnordered_thenOrdered4() {
        final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(1, 7, 12);
        final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(1, 12, 15, 16, 15);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 12, 30, 16, 30);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(secondDaytimeEntryTwo, secondDaytimeEntryOne, firstDaytimeEntryTwo, firstDaytimeEntryOne));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getDate()).isEqualTo(firstDaytimeEntryOne.getDate());
    }

    @Test
    void when2DaysAndFirstDayWith15MinBreakTime_thenWarning() {
        final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(1, 7, 12);
        final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(1, 12, 15, 16, 15);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 12, 30, 16, 30);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDaytimeEntryOne, firstDaytimeEntryTwo, secondDaytimeEntryOne, secondDaytimeEntryTwo));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getDate()).isEqualTo(firstDaytimeEntryOne.getDate());
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.25);
    }

    @Test
    void when2DaysAndSecondDayWith15MinBreakTime_thenWarning() {
        final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(2, 12, 30, 16, 30);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(3, 7, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(3, 12, 15, 16, 15);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDaytimeEntryOne, firstDaytimeEntryTwo, secondDaytimeEntryOne, secondDaytimeEntryTwo));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getDate()).isEqualTo(secondDaytimeEntryOne.getDate());
        assertThat(warnings.get(0).getMissingBreakTime()).isEqualTo(0.25);
    }

    @Test
    void when2DaysAndAllDaysWith15MinBreakTime_thenTwoWarnings() {
        final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(1, 7, 12);
        final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(1, 12, 15, 16, 15);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 12, 15, 16, 15);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDaytimeEntryOne, firstDaytimeEntryTwo, secondDaytimeEntryOne, secondDaytimeEntryTwo));

        assertThat(warnings).hasSize(2);
    }

    @Test
    void whenUnordered_thenOrdered() {
        final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(7, 16, 22);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(8, 8, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(8, 17, 22);
        final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(9, 9, 12);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(thirdDaytimeEntry, secondDaytimeEntryTwo, secondDaytimeEntryOne, firstDaytimeEntry));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getDate()).isEqualTo(secondDaytimeEntryOne.getDate());
    }

    @Test
    void when2DaysAndBothWith30MinBreakTime_thenNoWarning() {
        final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(2, 12, 30, 16, 30);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(3, 7, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(3, 12, 30, 16, 30);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDaytimeEntryOne, firstDaytimeEntryTwo, secondDaytimeEntryOne, secondDaytimeEntryTwo));

        assertThat(warnings).isEmpty();
    }

    @Test
    void when3DaysAndSecondDayWith10HourRestTime_thenWarning() {
        final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(7, 16, 22);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(8, 8, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(8, 17, 22);
        final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(9, 9, 12);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDaytimeEntry, secondDaytimeEntryOne, secondDaytimeEntryTwo, thirdDaytimeEntry));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getDate()).isEqualTo(secondDaytimeEntryOne.getDate());
        assertThat(warnings.get(0).getMissingRestTime()).isEqualTo(1);
    }

    @Test
    void when3DaysAndThirdDayWith10HourRestTime_thenWarning() {
        final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(7, 16, 22);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(8, 9, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(8, 17, 22);
        final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(9, 8, 12);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDaytimeEntry, secondDaytimeEntryOne, secondDaytimeEntryTwo, thirdDaytimeEntry));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getDate()).isEqualTo(thirdDaytimeEntry.getDate());
        assertThat(warnings.get(0).getMissingRestTime()).isEqualTo(1);
    }

    @Test
    void when3DaysAndAllDaysWith10HourRestTime_thenTwoWarnings() {
        final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(7, 16, 22);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(8, 8, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(8, 17, 22);
        final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(9, 8, 12);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDaytimeEntry, secondDaytimeEntryOne, secondDaytimeEntryTwo, thirdDaytimeEntry));

        assertThat(warnings).hasSize(2);
    }

    @Test
    void when3DaysAndAndAllWith11HourRestTime_thenNoWarning() {
        final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(7, 16, 22);
        final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(8, 9, 12);
        final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(8, 17, 22);
        final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(9, 9, 12);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDaytimeEntry, secondDaytimeEntryOne, secondDaytimeEntryTwo, thirdDaytimeEntry));

        assertThat(warnings).isEmpty();
    }

    @Test
    void whenUnordered_thenOrdered5() {
        final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(1, 7, 12);
        final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(1, 13, 19);
        final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(2, 13, 16);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(secondDayTimeEntryTwo, secondDayTimeEntryOne, firstDayTimeEntryTwo, firstDayTimeEntryOne));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getDate()).isEqualTo(firstDayTimeEntryOne.getDate());
    }

    @Test
    void when2DaysAndFirstDayWith11Hours_thenWarning() {
        final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(1, 7, 12);
        final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(1, 13, 19);
        final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(2, 13, 16);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDayTimeEntryOne, firstDayTimeEntryTwo, secondDayTimeEntryOne, secondDayTimeEntryTwo));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getExcessWorkTime()).isEqualTo(1);
    }

    @Test
    void when2DaysAndSecondDayWith11Hours_thenWarning() {
        final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(2, 13, 16);
        final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(3, 7, 12);
        final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(3, 13, 19);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDayTimeEntryOne, firstDayTimeEntryTwo, secondDayTimeEntryOne, secondDayTimeEntryTwo));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).getExcessWorkTime()).isEqualTo(1);
    }

    @Test
    void when2DaysAndAllDaysWith11Hours_thenTwoWarnings() {
        final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(1, 7, 12);
        final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(1, 13, 19);
        final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(2, 13, 19);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDayTimeEntryOne, firstDayTimeEntryTwo, secondDayTimeEntryOne, secondDayTimeEntryTwo));

        assertThat(warnings).hasSize(2);
    }

    @Test
    void when2DaysAndAllDaysWith10Hours_thenNoWarning() {
        final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
        final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(2, 13, 16);
        final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(3, 7, 12);
        final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(3, 13, 16);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(firstDayTimeEntryOne, firstDayTimeEntryTwo, secondDayTimeEntryOne, secondDayTimeEntryTwo));

        assertThat(warnings).isEmpty();
    }

    @Test
    void whenJourneuOverXDays_thenNoWarning() {
        final JourneyTimeEntry journeyTimeEntryOne = journeyTimeEntryFor(7, 7, 12, JourneyDirection.TO, WorkingLocation.A, Vehicle.OTHER_INACTIVE);
        final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(8, 13, 16, WorkingLocation.MAIN);
        final JourneyTimeEntry thirdTimeEntryOne = journeyTimeEntryFor(9, 7, 12, JourneyDirection.BACK, WorkingLocation.A, Vehicle.OTHER_INACTIVE);

        final List<TimeWarning> warnings = calculator
                .determineTimeWarnings(List.of(journeyTimeEntryOne, secondDayTimeEntryTwo, thirdTimeEntryOne));

        assertThat(warnings).isEmpty();
    }

    @Test
    void whenEntryIsOnHoliday_thenReturnsWarning() {
        final ProjectTimeEntry entryOne = projectTimeEntryFor(1, 8, 0);

        final List<TimeWarning> result = calculator.determineTimeWarnings(List.of(entryOne));

        assertThat(result)
                .isNotEmpty();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.get(0).getDate())
                .isEqualTo(entryOne.getDate());
    }

    @Test
    void whenEntryIsOnWeekend_thenReturnsWarning() {
        final ProjectTimeEntry entryOne = projectTimeEntryFor(4, 8, 0);

        final List<TimeWarning> result = calculator.determineTimeWarnings(List.of(entryOne));

        assertThat(result)
                .isNotEmpty();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.get(0).getDate())
                .isEqualTo(entryOne.getDate());
    }

    @ParameterizedTest
    @DisplayName("Tests all holidays for december 2020")
    @MethodSource("holidaysStream")
    void whenEntryDayIsHoliday_thenReturnsWarning(int day) {
        final ProjectTimeEntry entryOne = getProjectTimeEntry(day);

        final List<TimeWarning> result = calculator.determineTimeWarnings(List.of(entryOne));

        assertThat(result)
                .isNotEmpty();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.get(0).getDate())
                .isEqualTo(entryOne.getDate());
    }

    @ParameterizedTest
    @DisplayName("Tests all weekend days for december 2020")
    @MethodSource("weekendDaysStream")
    void whenEntryDayIsOnWeekend_thenReturnsWarning(int day) {
        final ProjectTimeEntry entryOne = getProjectTimeEntry(day);

        final List<TimeWarning> result = calculator.determineTimeWarnings(List.of(entryOne));

        assertThat(result)
                .isNotEmpty();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.get(0).getDate())
                .isEqualTo(entryOne.getDate());
    }

    static Stream<Integer> holidaysStream() {
        return Stream.of(HOLIDAYS);
    }

    static Stream<Integer> weekendDaysStream() {
        return Stream.of(WEEKEND_DAYS);
    }

    private ProjectTimeEntry getProjectTimeEntry(int day) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2020, 12, day, 8, 0))
                .toTime(LocalDateTime.of(2020, 12, day, 12, 0))
                .task(Task.BEARBEITEN)
                .workingLocation(WorkingLocation.MAIN)
                .build();
    }
}
