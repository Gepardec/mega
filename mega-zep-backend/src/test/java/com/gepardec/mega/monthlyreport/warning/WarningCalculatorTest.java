package com.gepardec.mega.monthlyreport.warning;

import com.gepardec.mega.aplication.utils.DateUtils;
import com.gepardec.mega.monthlyreport.ProjectTimeManager;
import com.gepardec.mega.monthlyreport.Task;
import com.gepardec.mega.monthlyreport.WorkingLocation;
import com.gepardec.mega.monthlyreport.journey.JourneyDirection;
import de.provantis.zep.ProjektzeitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WarningCalculatorTest {

    @Mock
    private WarningConfig warningConfig;


    @InjectMocks
    private WarningCalculator warningCalculator;


    @Test
    void determineTimeWarnings_oneEntryMoreThan6Hours_warning() {
        ProjectTimeManager projectTimeManager = new ProjectTimeManager(createEntrieyMoreThan6Hours());

        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(projectTimeManager);
        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 01, 07), warnings.get(0).getDate()),
                () -> assertNull(warnings.get(0).getMissingRestTime()),
                () -> assertEquals(0.5, warnings.get(0).getMissingBreakTime()),
                () -> assertNull(warnings.get(0).getExcessWorkTime()));
    }


    @Test
    void determineTimeWarnings_moreThan10HoursADay_Warning() {
        ProjectTimeManager projectTimeManager = new ProjectTimeManager(createEntriesMoreThan10HoursADay());
        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(projectTimeManager);

        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 01, 07), warnings.get(0).getDate()),
                () -> assertNull(warnings.get(0).getMissingRestTime()),
                () -> assertNull(warnings.get(0).getMissingBreakTime()),
                () -> assertEquals(4.25, warnings.get(0).getExcessWorkTime()));
    }


    @Test
    void determineTimeWarnings_lessThan11HoursRest_Warning() {
        ProjectTimeManager projectTimeManager = new ProjectTimeManager(createEntriesLessThan11HoursRest());
        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(projectTimeManager);

        assertAll(() -> assertEquals(1, warnings.size()),
                () -> assertEquals(LocalDate.of(2020, 1, 8), warnings.get(0).getDate()),
                () -> assertEquals(3.0, warnings.get(0).getMissingRestTime()),
                () -> assertNull(warnings.get(0).getMissingBreakTime()),
                () -> assertNull(warnings.get(0).getExcessWorkTime()));
    }

    @Test
    void determineTimeWarnings_lessThan11HoursRestAndMoreThan10HoursADayAndThan6Hours_Warnings() {
        ArrayList<ProjektzeitType> projectTimes = new ArrayList<>();
        projectTimes.addAll(createEntriesLessThan11HoursRest());
        projectTimes.addAll(createEntrieyMoreThan6Hours());


        ProjectTimeManager projectTimeManager = new ProjectTimeManager(projectTimes);

        List<TimeWarning> warnings = warningCalculator.determineTimeWarnings(projectTimeManager);
        assertAll(() -> assertEquals(2, warnings.size()),
                //first day
                () -> assertEquals(LocalDate.of(2020, 1, 7), warnings.get(0).getDate()),
                () -> assertNull(warnings.get(0).getMissingRestTime()),
                () -> assertEquals(0.5, warnings.get(0).getMissingBreakTime()),
                () -> assertEquals(3.0, warnings.get(0).getExcessWorkTime()),
                //seconda day
                () -> assertEquals(LocalDate.of(2020, 1, 8), warnings.get(1).getDate()),
                () -> assertEquals(3.0, warnings.get(1).getMissingRestTime()),
                () -> assertNull(warnings.get(1).getMissingBreakTime()),
                () -> assertNull(warnings.get(1).getExcessWorkTime()));
    }

    private static List<ProjektzeitType> createEntrieyMoreThan6Hours() {
        return Arrays.asList(ofTask(LocalDateTime.of(2020, 01, 07, 9, 00),
                LocalDateTime.of(2020, 01, 07, 16, 00),
                Task.BEARBEITEN,
                WorkingLocation.DEFAULT_WORKING_LOCATION));
    }

//    @ParameterizedTest
//    @CsvSource(value = {
//            "0; RG; '';''; Vollständig; 30.09.2019; laufend; 16 Tage; ''; ; ; OÖ; ; ; ; F32: Depressive Episode; depr. Episoden"
//    }, delimiter = ';')
//    void geldleistungen_RG_vorhanden_LGKK_T173(ArgumentsAccessor argumentsAccessor) {
//        DashboardPage dashboardPage = DashboardPage.load(seleniumSetup, 5007267259L, "3443060989",
//                LocalDate.of(2019, 10, 15).atStartOfDay(), "geldleistungen");
//        testWithArguments(argumentsAccessor, 1, invokeNoTimeout(seleniumSetup, dashboardPage.getGeldleistungen()::getGeldleistungen));
//    }

    private static List<ProjektzeitType> createEntriesMoreThan10HoursADay() {
        List<ProjektzeitType> projectTimes = new ArrayList<>();
        projectTimes.add(
                ofTask(LocalDateTime.of(2020, 01, 07, 7, 00),
                        LocalDateTime.of(2020, 01, 07, 10, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        projectTimes.add(
                ofTask(LocalDateTime.of(2020, 01, 07, 10, 30),
                        LocalDateTime.of(2020, 01, 07, 17, 45),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        projectTimes.add(
                ofTask(LocalDateTime.of(2020, 01, 07, 18, 00),
                        LocalDateTime.of(2020, 01, 07, 22, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        return projectTimes;
    }

    private static List<ProjektzeitType> createEntriesLessThan11HoursRest() {
        List<ProjektzeitType> projectTimes = new ArrayList<>();
        projectTimes.add(
                ofTask(LocalDateTime.of(2020, 01, 07, 16, 00),
                        LocalDateTime.of(2020, 01, 07, 22, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        projectTimes.add(
                ofTask(LocalDateTime.of(2020, 1, 8, 6, 00),
                        LocalDateTime.of(2020, 1, 8, 8, 00),
                        Task.BEARBEITEN,
                        WorkingLocation.DEFAULT_WORKING_LOCATION));
        return projectTimes;
    }


    private static ProjektzeitType ofTask(LocalDateTime from, LocalDateTime to, Task task, WorkingLocation workingLocation) {
        ProjektzeitType projektzeitType = new ProjektzeitType();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        projektzeitType.setDatum(DateUtils.formatDate(from.toLocalDate()));
        projektzeitType.setVon(from.toLocalTime().format(timeFormatter));
        projektzeitType.setBis(to.toLocalTime().format(timeFormatter));
        projektzeitType.setOrt(workingLocation.getWorkingLocationCode());
        projektzeitType.setTaetigkeit(task.name().toUpperCase());
        return projektzeitType;
    }

    private static ProjektzeitType ofJourney(LocalDateTime from, LocalDateTime to, JourneyDirection journeyDirection, Task task, WorkingLocation workingLocation) {
        ProjektzeitType projektzeitType = ofTask(from, to, task, workingLocation);
        projektzeitType.setReiseRichtung(journeyDirection.name());
        return projektzeitType;
    }


}
