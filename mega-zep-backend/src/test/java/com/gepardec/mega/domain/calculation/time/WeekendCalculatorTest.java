package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class WeekendCalculatorTest {
    private final WeekendCalculator calculator = new WeekendCalculator();

    private static final Integer[] BUSINESS_DAYS = {1, 2, 3, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 27, 28, 29, 30, 31};

    private static final Integer[] WEEKEND_DAYS = {4, 5, 11, 12, 18, 19, 25, 26};

    @ParameterizedTest
    @DisplayName("Tests if no warnings are created for business days")
    @MethodSource("streamOfBusinessDays")
    void calculate_whenEntryIsABusinessDay_thenReturnsEmptyWarningList(int day) {
        ProjectEntry entry = createEntry(day);

        List<TimeWarning> result = calculator.calculate(List.of(entry));

        assertThat(result)
                .isEmpty();
    }

    @ParameterizedTest
    @DisplayName("tests if correct warnings are created for weekend days")
    @MethodSource("streamOfWeekendDays")
    void calculate_whenEntryIsAWeekendDay_thenReturnsCorrectWarning(int day) {
        ProjectEntry entry = createEntry(day);

        List<TimeWarning> result = calculator.calculate(List.of(entry));

        assertThat(result)
                .isNotEmpty();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.get(0).getDate())
                .isEqualTo(LocalDate.of(2021, 12, day));
    }

    static Stream<Integer> streamOfBusinessDays() {
        return Stream.of(BUSINESS_DAYS);
    }

    static Stream<Integer> streamOfWeekendDays() {
        return Stream.of(WEEKEND_DAYS);
    }

    private ProjectTimeEntry createEntry(int day) {
        return ProjectTimeEntry.builder()
                .fromTime(LocalDateTime.of(2021, 12, day, 8, 0))
                .toTime(LocalDateTime.of(2021, 12, day, 16, 30))
                .task(Task.BEARBEITEN)
                .workingLocation(WorkingLocation.MAIN)
                .build();
    }
}
