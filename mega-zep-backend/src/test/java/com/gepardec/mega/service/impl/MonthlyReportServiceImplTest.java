package com.gepardec.mega.service.impl;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.service.comment.CommentService;
import com.gepardec.mega.service.impl.monthlyreport.MonthlyReportServiceImpl;
import com.gepardec.mega.service.impl.monthlyreport.calculation.WarningCalculator;
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
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

@ExtendWith(MockitoExtension.class)
public class MonthlyReportServiceImplTest {

    @Mock
    private ZepService zepService;

    @Mock
    private StepEntryService stepEntryService;

    @Mock
    private CommentService commentService;
    @Mock
    private ResourceBundle resourceBundle;

    @Spy
    private WarningCalculator warningCalculator;

    @InjectMocks
    private MonthlyReportServiceImpl workerService;

    @Test
    void testGetMonthendReportForUser_MitarbeiterNull() {
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(null);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterInvalid() {
        final Employee employee = createEmployeeWithReleaseDate(0, "NULL");
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenNull() {
        final Employee employee = createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any())).thenReturn(null);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }


    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenInvalid() {
        final Employee employee = createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any())).thenReturn(new ArrayList<>());

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_NoWarning() {
        final Employee employee = createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any())).thenReturn(createReadProjektzeitenResponseType(10));
        Mockito.when(stepEntryService.getEmcState(Mockito.any())).thenReturn(State.DONE);
        Mockito.when(commentService.findCommentsForEmployee(Mockito.any())).thenReturn(Collections.emptyList());

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Thomas_0@gepardec.com", monthendReportForUser.getEmployee().email());
        Assertions.assertNotNull(monthendReportForUser.getTimeWarnings());
        Assertions.assertTrue(monthendReportForUser.getTimeWarnings().isEmpty());
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_Warning() {
        final Employee employee = createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any())).thenReturn(createReadProjektzeitenResponseType(18));
        Mockito.when(stepEntryService.getEmcState(Mockito.any())).thenReturn(State.DONE);
        Mockito.when(commentService.findCommentsForEmployee(Mockito.any())).thenReturn(Collections.emptyList());

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Thomas_0@gepardec.com", monthendReportForUser.getEmployee().email());
        Assertions.assertNotNull(monthendReportForUser.getTimeWarnings());
        Assertions.assertFalse(monthendReportForUser.getTimeWarnings().isEmpty());
        Assertions.assertEquals(LocalDate.of(2020, 1, 31), monthendReportForUser.getTimeWarnings().get(0).getDate());
        Assertions.assertEquals(1d, monthendReportForUser.getTimeWarnings().get(0).getExcessWorkTime());
        Assertions.assertEquals(0.5d, monthendReportForUser.getTimeWarnings().get(0).getMissingBreakTime());
    }

    private List<ProjectEntry> createReadProjektzeitenResponseType(int bisHours) {

        return List.of(ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 31, 7, 0),
                LocalDateTime.of(2020, 1, 31, bisHours, 0),
                Task.BEARBEITEN),
                ProjectTimeEntry.of(LocalDateTime.of(2020, 1, 30, 7, 0),
                        LocalDateTime.of(2020, 1, 30, 10, 0),
                        Task.BEARBEITEN));
    }

    private Employee createEmployee(final int userId) {
        return createEmployeeWithReleaseDate(userId, "2020-01-01");
    }

    private Employee createEmployeeWithReleaseDate(final int userId, String releaseDate) {
        final String name = "Thomas_" + userId;

        final Employee employee = Employee.builder()
                .email(name + "@gepardec.com")
                .firstName(name)
                .sureName(name + "_Nachname")
                .title("Ing.")
                .userId(String.valueOf(userId))
                .salutation("Herr")
                .workDescription("ARCHITEKT")
                .releaseDate(releaseDate)
                .role(Role.USER.roleId)
                .active(true)
                .build();

        return employee;
    }
}
