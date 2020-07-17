package com.gepardec.mega.zep.service.impl;


import com.gepardec.mega.domain.MonthlyReport;
import com.gepardec.mega.domain.WorkingLocation;
import com.gepardec.mega.service.impl.WorkerServiceImpl;
import com.gepardec.mega.service.monthlyreport.WarningConfig;
import com.gepardec.mega.service.api.EmployeeService;
import com.gepardec.mega.domain.Employee;
import com.gepardec.mega.util.EmployeeTestUtil;
import com.gepardec.mega.zep.ZepSoapProvider;
import de.provantis.zep.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Arrays;


@ExtendWith(MockitoExtension.class)
public class WorkerServiceImplTest {

    @Mock
    private Logger logger;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ZepSoapProvider zepSoapProvider;

    @Mock
    private ZepSoapPortType zepSoapPortType;

    @Mock
    private WarningConfig warningConfig;

    @InjectMocks
    private WorkerServiceImpl workerService;


    @Test
    void testGetMonthendReportForUser_MitarbeiterNull() {
        Mockito.when(employeeService.getEmployee(Mockito.any())).thenReturn(null);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterInvalid() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        employee.setReleaseDate("NULL");
        Mockito.when(employeeService.getEmployee(Mockito.any())).thenReturn(employee);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenNull() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        Mockito.when(employeeService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepSoapPortType.readProjektzeiten(Mockito.any())).thenReturn(null);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }


    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenInvalid() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        Mockito.when(employeeService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepSoapPortType.readProjektzeiten(Mockito.any())).thenReturn(new ReadProjektzeitenResponseType());

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_NoWarning() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        Mockito.when(employeeService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepSoapPortType.readProjektzeiten(Mockito.any())).thenReturn(createReadProjektzeitenResponseType("10:00"));

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Thomas_0@gepardec.com", monthendReportForUser.getEmployee().getEmail());
        Assertions.assertNotNull(monthendReportForUser.getTimeWarnings());
        Assertions.assertTrue(monthendReportForUser.getTimeWarnings().isEmpty());
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_Warning() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        Mockito.when(employeeService.getEmployee(Mockito.any())).thenReturn(employee);
        Mockito.when(zepSoapPortType.readProjektzeiten(Mockito.any())).thenReturn(createReadProjektzeitenResponseType("18:00"));

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Thomas_0@gepardec.com", monthendReportForUser.getEmployee().getEmail());
        Assertions.assertNotNull(monthendReportForUser.getTimeWarnings());
        Assertions.assertFalse(monthendReportForUser.getTimeWarnings().isEmpty());
        Assertions.assertEquals(LocalDate.of(2020, 1, 31), monthendReportForUser.getTimeWarnings().get(0).getDate());
        Assertions.assertEquals(1d, monthendReportForUser.getTimeWarnings().get(0).getExcessWorkTime());
        Assertions.assertEquals(0.5d, monthendReportForUser.getTimeWarnings().get(0).getMissingBreakTime());
    }

    private ReadProjektzeitenResponseType createReadProjektzeitenResponseType(String bis) {
        final ProjektzeitType projektzeitType1 = new ProjektzeitType();
        projektzeitType1.setDatum("2020-01-31");
        projektzeitType1.setVon("07:00");
        projektzeitType1.setBis(bis);
        projektzeitType1.setTaetigkeit("BEARBEITEN");
        projektzeitType1.setOrt(WorkingLocation.DEFAULT_WORKING_LOCATION.getWorkingLocationCode());

        final ProjektzeitType projektzeitType2 = new ProjektzeitType();
        projektzeitType2.setDatum("2020-01-30");
        projektzeitType2.setVon("07:00");
        projektzeitType2.setBis("10:00");
        projektzeitType2.setTaetigkeit("BEARBEITEN");
        projektzeitType2.setOrt(WorkingLocation.DEFAULT_WORKING_LOCATION.getWorkingLocationCode());

        final ProjektzeitenListeType projektzeitenListeType = new ProjektzeitenListeType();
        projektzeitenListeType.getProjektzeiten().addAll(Arrays.asList(projektzeitType1, projektzeitType2));

        final ReadProjektzeitenResponseType readProjektzeitenResponseType = new ReadProjektzeitenResponseType();
        readProjektzeitenResponseType.setProjektzeitListe(projektzeitenListeType);

        return readProjektzeitenResponseType;
    }
}
