package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@ExtendWith(MockitoExtension.class)
class MonthlyReportServiceImplTest {

    @Mock
    private ZepService zepService;

    @Mock
    private ResourceBundle resourceBundle;

    @Spy
    private WarningCalculator warningCalculator;

    @Mock
    private CommentService commentService;

    @Mock
    private StepEntryService stepEntryService;

    @InjectMocks
    private MonthlyReportServiceImpl workerService;

    @Test
    void testGetMonthendReportForUser_MitarbeiterNull() {
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(null);

        Assertions.assertNotNull(workerService.getMonthendReportForUser("0"));
    }

    // @Test FIXME
    void testGetMonthendReportForUser_MitarbeiterValid() {
        final Employee employee = createEmployeeWithReleaseDate(0, "NULL");
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);

        Assertions.assertNotNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid() {
        final Employee employee = createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());

        Assertions.assertNotNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_NoWarning() {
        final Employee employee = createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any(), Mockito.any())).thenReturn(createReadProjektzeitenResponseType(10));

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Max_0@gepardec.com", monthendReportForUser.employee().email());
        Assertions.assertNotNull(monthendReportForUser.timeWarnings());
        Assertions.assertTrue(monthendReportForUser.timeWarnings().isEmpty());
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_Warning() {
        final Employee employee = createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any(), Mockito.any())).thenReturn(createReadProjektzeitenResponseType(18));

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Max_0@gepardec.com", monthendReportForUser.employee().email());
        Assertions.assertNotNull(monthendReportForUser.timeWarnings());
        Assertions.assertFalse(monthendReportForUser.timeWarnings().isEmpty());
        Assertions.assertEquals(LocalDate.of(2020, 1, 31), monthendReportForUser.timeWarnings().get(0).getDate());
        Assertions.assertEquals(1d, monthendReportForUser.timeWarnings().get(0).getExcessWorkTime());
        Assertions.assertEquals(0.5d, monthendReportForUser.timeWarnings().get(0).getMissingBreakTime());
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
