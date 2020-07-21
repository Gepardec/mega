package com.gepardec.mega.zep.service.translator;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.domain.Employee;
import com.gepardec.mega.service.employee.EmployeeTranslator;
import com.gepardec.mega.util.ZepTestUtil;
import de.provantis.zep.MitarbeiterType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class EmployeeTranslatorTest {


    @Test
    void testToEmployeeBeschaeftingungszeitListeNull() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertFalse(employee.active());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeEmpty() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType());

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertFalse(employee.active());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterInactive() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), LocalDate.now().minusDays(15))));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertFalse(employee.active());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterInactiveMultiTimes() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(
                ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(90), LocalDate.now().minusDays(75)),
                ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(60), LocalDate.now().minusDays(45)),
                ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), LocalDate.now().minusDays(15))
        ));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertFalse(employee.active());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterInactiveEndeDateYesterday() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), LocalDate.now().minusDays(1))));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertFalse(employee.active());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterActive() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30),null)));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.active());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterActiveMultiTimes() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(
                ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(90), LocalDate.now().minusDays(75)),
                ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(60), LocalDate.now().minusDays(45)),
                ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), null)
        ));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.active());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterActiveEndeDateToday() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), LocalDate.now())));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.active());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterActiveEndeDateTomorrow() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), LocalDate.now().plusDays(1))));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.active());
    }

    @Test
    void testToEmployee() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(300), null)));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("0", employee.userId());
        Assertions.assertEquals("Thomas_0@gepardec.com", employee.email());
        Assertions.assertEquals("Ing.", employee.title());
        Assertions.assertEquals("Thomas_0", employee.firstName());
        Assertions.assertEquals("Thomas_0_Nachname", employee.sureName());
        Assertions.assertEquals("Herr", employee.salutation());
        Assertions.assertEquals("2020-01-01", employee.releaseDate());
        Assertions.assertEquals("ARCHITEKT", employee.workDescription());
        Assertions.assertEquals(Role.USER.roleId, employee.role());
        Assertions.assertTrue(employee.active());
    }
}
