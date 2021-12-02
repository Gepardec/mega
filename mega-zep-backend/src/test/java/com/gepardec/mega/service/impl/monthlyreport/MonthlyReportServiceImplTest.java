package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.zep.ZepService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonthlyReportServiceImplTest {

    @Mock
    private ZepService zepService;

    @Mock
    private ResourceBundle resourceBundle;

    @Mock
    private WarningCalculator warningCalculator;

    @Mock
    private CommentService commentService;

    @Mock
    private StepEntryService stepEntryService;

    @Mock
    ResourceBundle messages;

    //@Mock
    //private WarningCalculator warningCalculator;

    @InjectMocks
    private MonthlyReportServiceImpl workerService;

    @Test
    void testGetMonthendReportForUser_MitarbeiterNull() {
        when(zepService.getEmployee(Mockito.any())).thenReturn(null);
        when(warningCalculator.determineNoTimeEntries(anyList(), anyList())).thenReturn(new ArrayList<TimeWarning>());

        Assertions.assertNotNull(workerService.getMonthendReportForUser("0"));
    }

    // @Test FIXME
    void testGetMonthendReportForUser_MitarbeiterValid() {
        final Employee employee = createEmployeeWithReleaseDate(0, "NULL");
        when(zepService.getEmployee(Mockito.any())).thenReturn(employee);

        Assertions.assertNotNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());
        when(warningCalculator.determineNoTimeEntries(anyList(), anyList())).thenReturn(new ArrayList<TimeWarning>());

        Assertions.assertNotNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_NoWarning() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(), Mockito.any())).thenReturn(createReadProjektzeitenResponseType(10));
        when(warningCalculator.determineNoTimeEntries(anyList(), anyList())).thenReturn(new ArrayList<TimeWarning>());

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Max_0@gepardec.com", monthendReportForUser.employee().email());
        Assertions.assertNotNull(monthendReportForUser.timeWarnings());
        Assertions.assertTrue(monthendReportForUser.timeWarnings().isEmpty());
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_Warning() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(), Mockito.any())).thenReturn(createReadProjektzeitenResponseType(18));
        when(zepService.getAbsenceForEmployee(any(Employee.class), any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(warningCalculator.determineTimeWarnings(anyList())).thenReturn(createTimeWarningList());

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Max_0@gepardec.com", monthendReportForUser.employee().email());
        Assertions.assertNotNull(monthendReportForUser.timeWarnings());
        Assertions.assertFalse(Objects.requireNonNull(monthendReportForUser.timeWarnings()).isEmpty());
        Assertions.assertEquals(LocalDate.of(2020, 1, 31), monthendReportForUser.timeWarnings().get(0).getDate());
        Assertions.assertEquals(1d, monthendReportForUser.timeWarnings().get(0).getExcessWorkTime());
        Assertions.assertEquals(0.5d, monthendReportForUser.timeWarnings().get(0).getMissingBreakTime());
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
