package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import com.gepardec.mega.service.helper.WarningCalculator;
import com.gepardec.mega.service.impl.MonthlyReportServiceImpl;
import com.gepardec.mega.zep.ZepService;
import de.provantis.zep.FehlzeitType;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
class MonthlyReportServiceImplTest {

    @InjectMock
    ZepService zepService;

    @InjectMock
    WarningCalculator warningCalculator;

    @Inject
    MonthlyReportServiceImpl monthlyReportService;

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid() {
        final Employee employee = createEmployeeWithReleaseDate(0, "NULL");
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);

        assertThat(monthlyReportService.getMonthendReportForUser("0"))
                .isNotNull();
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(warningCalculator.determineNoTimeEntries(Mockito.anyList(), Mockito.anyList())).thenReturn(new ArrayList<>());

        assertThat(monthlyReportService.getMonthendReportForUser("0"))
                .isNotNull();
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_NoWarning() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(ArgumentMatchers.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(10));
        when(warningCalculator.determineNoTimeEntries(Mockito.anyList(), Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertThat(monthendReportForUser)
                .isNotNull();
        assertThat(monthendReportForUser.getEmployee().getEmail())
                .isEqualTo("Max_0@gepardec.com");
        assertThat(monthendReportForUser.getTimeWarnings())
                .isNotNull();
        assertThat(monthendReportForUser.getTimeWarnings().isEmpty())
                .isTrue();
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_Warning() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(createTimeWarningList());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertThat(monthendReportForUser)
                .isNotNull();
        assertThat(monthendReportForUser.getEmployee().getEmail())
                .isEqualTo("Max_0@gepardec.com");
        assertThat(monthendReportForUser.getTimeWarnings())
                .isNotNull();
        assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()).isEmpty())
                .isFalse();
        assertThat(monthendReportForUser.getTimeWarnings().get(0).getDate())
                .isEqualTo(LocalDate.of(2020, 1, 31));
        assertThat(monthendReportForUser.getTimeWarnings().get(0).getExcessWorkTime())
                .isEqualTo(1d);
        assertThat(monthendReportForUser.getTimeWarnings().get(0).getMissingBreakTime())
                .isEqualTo(0.5d);
    }

    @Test
    void getMonthendReportForUser_isUserIsValidAndHasNursingAbsenceDays_thenReturnsReportWithCorrectNursingDays() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        List<FehlzeitType> absenceList = new ArrayList<>();
        FehlzeitType nursingDay = new FehlzeitType();
        nursingDay.setFehlgrund("PU");
        nursingDay.setGenehmigt(true);
        nursingDay.setEnddatum(LocalDate.of(2020, 2, 29).toString());
        nursingDay.setStartdatum(LocalDate.of(2020, 2, 27).toString());
        absenceList.add(nursingDay);
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(absenceList);
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertThat(monthendReportForUser)
                .isNotNull();
        assertThat(monthendReportForUser.getEmployee().getEmail())
                .isEqualTo("Max_0@gepardec.com");
        assertThat(monthendReportForUser.getTimeWarnings())
                .isNotNull();
        assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()).isEmpty())
                .isTrue();
        assertThat(monthendReportForUser.getNursingDays()).isEqualTo(2);
    }

    private List<TimeWarning> createTimeWarningList() {
        TimeWarning timewarning = new TimeWarning();
        timewarning.setDate(LocalDate.of(2020, 1, 31));
        timewarning.getWarningTypes().add(TimeWarningType.OUTSIDE_CORE_WORKING_TIME);
        timewarning.setExcessWorkTime(1d);
        timewarning.setMissingBreakTime(0.5d);
        List<TimeWarning> timeWarningList = new ArrayList<>();
        timeWarningList.add(timewarning);

        return timeWarningList;
    }

    private List<ProjectEntry> createReadProjektzeitenResponseType(int bisHours) {

        return List.of(
                ProjectTimeEntry.builder()
                        .fromTime(LocalDateTime.of(2020, 1, 31, 7, 0))
                        .toTime(LocalDateTime.of(2020, 1, 31, bisHours, 0))
                        .task(Task.BEARBEITEN)
                        .workingLocation(WorkingLocation.MAIN).build(),
                ProjectTimeEntry.builder()
                        .fromTime(LocalDateTime.of(2020, 1, 30, 7, 0))
                        .toTime(LocalDateTime.of(2020, 1, 30, 10, 0))
                        .task(Task.BEARBEITEN)
                        .workingLocation(WorkingLocation.MAIN).build()
        );
    }

    private Employee createEmployee(final int userId) {
        return createEmployeeWithReleaseDate(userId, "2020-01-01");
    }

    private Employee createEmployeeWithReleaseDate(final int userId, String releaseDate) {
        final String name = "Max_" + userId;

        final Employee employee = Employee.builder()
                .email(name + "@gepardec.com")
                .firstname(name)
                .lastname(name + "_Nachname")
                .title("Ing.")
                .userId(String.valueOf(userId))
                .salutation("Herr")
                .workDescription("ARCHITEKT")
                .releaseDate(releaseDate)
                .active(true)
                .build();

        return employee;
    }
}
