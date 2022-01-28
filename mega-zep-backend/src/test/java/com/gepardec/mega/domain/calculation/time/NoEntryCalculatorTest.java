package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;
import de.provantis.zep.FehlzeitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NoEntryCalculatorTest {

    private NoEntryCalculator noEntryCalculator;

    @BeforeEach
    void init() {
        noEntryCalculator = new NoEntryCalculator();
    }

    @Test
    void calculate_whenNoProjectEntriesAndNoAbsenceEntries_thenEmptyEntryListWarning() {
        TimeWarning expectedTimeWarning = new TimeWarning();
        expectedTimeWarning.getWarningTypes().add(TimeWarningType.EMPTY_ENTRY_LIST);
        List<TimeWarning> expectedTimeWarningsList = new ArrayList<>();
        expectedTimeWarningsList.add(expectedTimeWarning);

        List<TimeWarning> result = noEntryCalculator.calculate(new ArrayList<>(), new ArrayList<>());

        assertThat(result)
                .isEqualTo(expectedTimeWarningsList);
    }

    @Test
    void calculate_whenMissingEntry_thenCorrectWarningWithCorrectDate() {
        TimeWarning expectedTimeWarning = new TimeWarning();
        expectedTimeWarning.getWarningTypes().add(TimeWarningType.EMPTY_ENTRY_LIST);
        expectedTimeWarning.setDate(LocalDate.of(2021, 2, 26));
        List<TimeWarning> expectedTimeWarningsList = new ArrayList<>();
        expectedTimeWarningsList.add(expectedTimeWarning);

        List<TimeWarning> result = noEntryCalculator.calculate(createProjectEntryList(1), new ArrayList<>());

        assertThat(result.get(0).getDate())
                .isEqualTo(expectedTimeWarningsList.get(0).getDate());
    }

    @Test
    void calculate_whenAllEntries_thenNoWarning() {
        List<TimeWarning> result = noEntryCalculator.calculate(createProjectEntryList(0), new ArrayList<>());

        assertThat(result.isEmpty())
                .isTrue();
    }

    @Test
    void calculate_whenAllEntriesAndVacationDayOnWorkingDay_thenNoWarning() {
        List<TimeWarning> result = noEntryCalculator.calculate(createProjectEntryList(2), createAbsenceListFromUBType());

        assertThat(result.isEmpty())
                .isTrue();
    }

    @Test
    void calculate_whenAllEntriesAndCompensatoryDayOnWorkingDay_thenNoWarning() {
        List<TimeWarning> result = noEntryCalculator.calculate(createProjectEntryList(2), createAbsenceListFromFAType());

        assertThat(result.isEmpty())
                .isTrue();
    }

    @Test
    void calculate_whenAllEntriesInMonthWithHolidayOnFirstNov_thenNoWarning() {
        List<TimeWarning> result = noEntryCalculator.calculate(createProjectEntryListForNovember(), new ArrayList<>());

        assertThat(result).isEmpty();
    }

    private List<FehlzeitType> createAbsenceListFromUBType() {
        List<FehlzeitType> fehlZeit = new ArrayList<>();
        FehlzeitType fehlzeitType = new FehlzeitType();
        String startDate = LocalDate.of(2021, 2, 25).toString();
        String endDate = LocalDate.of(2021, 2, 26).toString();
        fehlzeitType.setStartdatum(startDate);
        fehlzeitType.setEnddatum(endDate);
        fehlzeitType.setFehlgrund("UB");
        fehlZeit.add(fehlzeitType);

        return fehlZeit;
    }

    private List<FehlzeitType> createAbsenceListFromFAType() {
        List<FehlzeitType> fehlZeit = new ArrayList<>();
        FehlzeitType fehlzeitType = new FehlzeitType();
        String startDate = LocalDate.of(2021, 2, 25).toString();
        String endDate = LocalDate.of(2021, 2, 26).toString();
        fehlzeitType.setStartdatum(startDate);
        fehlzeitType.setEnddatum(endDate);
        fehlzeitType.setFehlgrund("FA");
        fehlZeit.add(fehlzeitType);

        return fehlZeit;
    }

    private List<ProjectEntry> createProjectEntryList(int amountOfMissingEntries) {
        List<ProjectEntry> entries = new ArrayList<>();

        for(int i = 1; i <= (26-amountOfMissingEntries); i++) {
            ProjectEntry projectEntry = ProjectTimeEntry.builder().fromTime(LocalDateTime.of(LocalDate.of(2021, 2, i), LocalTime.of(8, 0))).toTime(LocalDateTime.of(LocalDate.of(2021, 2, i), LocalTime.of(12, 0))).build();
            entries.add(projectEntry);
        }

        return entries;
    }

    private List<ProjectEntry> createProjectEntryListForNovember() {
        List<ProjectEntry> entries = new ArrayList<>();

        for(int i = 2; i <= (30); i++) {
            ProjectEntry projectEntry = ProjectTimeEntry.builder().fromTime(LocalDateTime.of(LocalDate.of(2021, 11, i), LocalTime.of(8, 0))).toTime(LocalDateTime.of(LocalDate.of(2021, 11, i), LocalTime.of(12, 0))).build();
            entries.add(projectEntry);
        }

        return entries;
    }
}
