package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.zep.ZepServiceImpl;
import com.gepardec.mega.util.ZepTestUtil;
import com.gepardec.mega.zep.ZepServiceException;
import com.gepardec.mega.zep.ZepSoapProvider;
import de.provantis.zep.ReadMitarbeiterResponseType;
import de.provantis.zep.ZepSoapPortType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ZepServiceImplTest {

    @Mock
    private Logger logger;

    @Mock
    private ZepSoapPortType zepSoapPortType;

    @Mock
    private ZepSoapProvider zepSoapProvider;

    private ZepServiceImpl beanUnderTest;

    @BeforeEach
    void setUp() {
        beanUnderTest = new ZepServiceImpl(logger, zepSoapPortType, zepSoapProvider);
    }

    @Test
    void testGetEmployee() {
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(ZepTestUtil.createReadMitarbeiterResponseType(
                ZepTestUtil.createMitarbeiterType(0)
        ));

        final Employee employee = beanUnderTest.getEmployee("0");
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("0", employee.userId());

        Mockito.verify(zepSoapPortType).readMitarbeiter(Mockito.argThat(
                argument -> argument.getReadMitarbeiterSearchCriteria() != null && argument.getReadMitarbeiterSearchCriteria().getUserId().equals("0")
        ));
    }

    @Test
    void testGetEmployeesMitarbeiterZepResponseNull() {
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(null);

        final List<Employee> employee = beanUnderTest.getEmployees();
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.isEmpty());
    }

    @Test
    void testGetEmployeesMitarbeiterZepResponseMitarbeiterListeNull() {
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(new ReadMitarbeiterResponseType());

        final List<Employee> employee = beanUnderTest.getEmployees();
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.isEmpty());
    }

    @Test
    void testGetEmployeesMitarbeiterZepResponseMitarbeiterListeEmpty() {
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(new ReadMitarbeiterResponseType());

        final List<Employee> employee = beanUnderTest.getEmployees();
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.isEmpty());
    }

    @Test
    void testGetEmployees() {
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(ZepTestUtil.createReadMitarbeiterResponseType(
                ZepTestUtil.createMitarbeiterType(0),
                ZepTestUtil.createMitarbeiterType(1),
                ZepTestUtil.createMitarbeiterType(2)
        ));

        final List<Employee> employee = beanUnderTest.getEmployees();
        Assertions.assertNotNull(employee);
        Assertions.assertFalse(employee.isEmpty());
        Assertions.assertEquals(3, employee.size());

        Mockito.verify(zepSoapPortType).readMitarbeiter(Mockito.argThat(
                argument -> argument.getReadMitarbeiterSearchCriteria() == null
        ));
    }

    @Test
    void testUpdateEmployeesReleaseDateException() {
        Mockito.when(zepSoapPortType.updateMitarbeiter(Mockito.any())).thenReturn(ZepTestUtil.createUpaUpdateMitarbeiterResponseType(ZepTestUtil.createResponseHeaderType("1337")));

        final ZepServiceException zepServiceException = Assertions.assertThrows(ZepServiceException.class, () -> beanUnderTest.updateEmployeesReleaseDate("0", "2020-01-01"));
        Assertions.assertEquals("updateEmployeeReleaseDate failed with code: 1337", zepServiceException.getMessage());

        Mockito.verify(zepSoapPortType).updateMitarbeiter(Mockito.argThat(
                argument -> argument.getMitarbeiter().getUserId().equals("0") && argument.getMitarbeiter().getFreigabedatum().equals("2020-01-01")
        ));
    }

    @Test
    void testUpdateEmployeesReleaseDateResponseNull() {
        Mockito.when(zepSoapPortType.updateMitarbeiter(Mockito.any())).thenReturn(null);

        beanUnderTest.updateEmployeesReleaseDate("0", "2020-01-01");

        Mockito.verify(zepSoapPortType).updateMitarbeiter(Mockito.argThat(
                argument -> argument.getMitarbeiter().getUserId().equals("0") && argument.getMitarbeiter().getFreigabedatum().equals("2020-01-01")
        ));
    }

    @Test
    void testUpdateEmployeesReleaseDateResponseHeaderNull() {
        Mockito.when(zepSoapPortType.updateMitarbeiter(Mockito.any())).thenReturn(ZepTestUtil.createUpaUpdateMitarbeiterResponseType(null));

        beanUnderTest.updateEmployeesReleaseDate("0", "2020-01-01");

        Mockito.verify(zepSoapPortType).updateMitarbeiter(Mockito.argThat(
                argument -> argument.getMitarbeiter().getUserId().equals("0") && argument.getMitarbeiter().getFreigabedatum().equals("2020-01-01")
        ));
    }

    @Test
    void testUpdateEmployeesReleaseDate() {
        Mockito.when(zepSoapPortType.updateMitarbeiter(Mockito.any())).thenReturn(ZepTestUtil.createUpaUpdateMitarbeiterResponseType(ZepTestUtil.createResponseHeaderType("0")));

        beanUnderTest.updateEmployeesReleaseDate("0", "2020-01-01");

        Mockito.verify(zepSoapPortType).updateMitarbeiter(Mockito.argThat(
                argument -> argument.getMitarbeiter().getUserId().equals("0") && argument.getMitarbeiter().getFreigabedatum().equals("2020-01-01")
        ));
    }
}
