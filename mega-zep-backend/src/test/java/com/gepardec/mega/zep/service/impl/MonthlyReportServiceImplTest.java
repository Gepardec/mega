package com.gepardec.mega.zep.service.impl;


import com.gepardec.mega.domain.model.employee.Employee;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.service.impl.monthlyreport.MonthlyReportServiceImpl;
import com.gepardec.mega.util.EmployeeTestUtil;
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
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class MonthlyReportServiceImplTest {

    @Mock
    private ZepService zepService;

    @InjectMocks
    private MonthlyReportServiceImpl workerService;

    @Test
    void testGetMonthendReportForUser_MitarbeiterNull() {
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(null);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterInvalid() {
        final Employee employee = EmployeeTestUtil.createEmployee(0, "NULL");
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenNull() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any())).thenReturn(null);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }


    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenInvalid() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any())).thenReturn(new ArrayList<>());

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_NoWarning() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any())).thenReturn(createReadProjektzeitenResponseType(10));

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Thomas_0@gepardec.com", monthendReportForUser.getEmployee().email());
        Assertions.assertNotNull(monthendReportForUser.getTimeWarnings());
        Assertions.assertTrue(monthendReportForUser.getTimeWarnings().isEmpty());
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_Warning() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepService.getProjectTimes(Mockito.any())).thenReturn(createReadProjektzeitenResponseType(18));

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Thomas_0@gepardec.com", monthendReportForUser.getEmployee().email());
        Assertions.assertNotNull(monthendReportForUser.getTimeWarnings());
        Assertions.assertFalse(monthendReportForUser.getTimeWarnings().isEmpty());
        Assertions.assertEquals(LocalDate.of(2020, 1, 31), monthendReportForUser.getTimeWarnings().get(0).getDate());
        Assertions.assertEquals(1d, monthendReportForUser.getTimeWarnings().get(0).getExcessWorkTime());
        Assertions.assertEquals(0.5d, monthendReportForUser.getTimeWarnings().get(0).getMissingBreakTime());
    }

    private List<ProjectTimeEntry> createReadProjektzeitenResponseType(int bisHours) {

        return Arrays.asList(new ProjectTimeEntry(LocalDateTime.of(2020, 1, 31, 7 ,0),
                        LocalDateTime.of(2020, 1, 31, bisHours, 0),
                        Task.BEARBEITEN),
                new ProjectTimeEntry(LocalDateTime.of(2020, 1, 30, 7 ,0),
                        LocalDateTime.of(2020, 1, 30, 10, 0),
                        Task.BEARBEITEN));
    }
}
