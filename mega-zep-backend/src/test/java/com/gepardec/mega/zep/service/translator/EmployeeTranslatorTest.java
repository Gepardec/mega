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
        Assertions.assertFalse(employee.isActive());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeEmpty() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType());

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertFalse(employee.isActive());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterInactive() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), LocalDate.now().minusDays(15))));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertFalse(employee.isActive());
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
        Assertions.assertFalse(employee.isActive());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterInactiveEndeDateYesterday() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), LocalDate.now().minusDays(1))));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertFalse(employee.isActive());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterActive() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30),null)));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.isActive());
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
        Assertions.assertTrue(employee.isActive());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterActiveEndeDateToday() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), LocalDate.now())));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.isActive());
    }

    @Test
    void testToEmployeeBeschaeftingungszeitListeMitarbeiterActiveEndeDateTomorrow() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(30), LocalDate.now().plusDays(1))));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertTrue(employee.isActive());
    }

    @Test
    void testToEmployee() {
        final MitarbeiterType mitarbeiterType = ZepTestUtil.createMitarbeiterType(0);
        mitarbeiterType.setBeschaeftigungszeitListe(ZepTestUtil.createBeschaeftigungszeitListeType(ZepTestUtil.createBeschaeftigungszeitType(LocalDate.now().minusDays(300), null)));

        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiterType);
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("0", employee.getUserId());
        Assertions.assertEquals("Thomas_0@gepardec.com", employee.getEmail());
        Assertions.assertEquals("Ing.", employee.getTitle());
        Assertions.assertEquals("Thomas_0", employee.getFirstName());
        Assertions.assertEquals("Thomas_0_Nachname", employee.getSureName());
        Assertions.assertEquals("Herr", employee.getSalutation());
        Assertions.assertEquals("2020-01-01", employee.getReleaseDate());
        Assertions.assertEquals("ARCHITEKT", employee.getWorkDescription());
        Assertions.assertEquals(Role.USER.roleId, employee.getRole());
        Assertions.assertTrue(employee.isActive());
    }
}
