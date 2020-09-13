package com.gepardec.mega.service.impl.employee;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.utils.DateUtils;
import de.provantis.zep.BeschaeftigungszeitListeType;
import de.provantis.zep.BeschaeftigungszeitType;
import de.provantis.zep.MitarbeiterType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class EmployeeMapperTest {

    private EmployeeMapper mapper;

    @BeforeEach
    void beforeEach() {
        mapper = new EmployeeMapper();
    }

    @Test
    void map_whenEmployeeIsNull_thenReturnsNull() {
        Assertions.assertNull(mapper.map(null));
    }

    @Test
    void map_whenBeschaeftingungszeitListeIsNull_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();

        final Employee actual = mapper.map(employee);
        Assertions.assertFalse(actual.active());
    }

    @Test
    void map_whenBeschaeftingungszeitListeIsEmpty_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        employee.setBeschaeftigungszeitListe(new BeschaeftigungszeitListeType());

        final Employee actual = mapper.map(employee);
        Assertions.assertFalse(actual.active());
    }

    @Test
    void map_whenEmployeeWasEmployedInThePastOnce_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType closedEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(closedEmployment);
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        Assertions.assertFalse(actual.active());
    }

    @Test
    void map_whenEmployeeWasEmployedInThePastMultipleTimes_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType closedEmploymentOne = createBeschaeftigungszeitType(LocalDate.now().minusDays(10), LocalDate.now()
                .minusDays(8));
        final BeschaeftigungszeitType closedEmploymentTwo = createBeschaeftigungszeitType(LocalDate.now().minusDays(7), LocalDate.now().minusDays(4));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(closedEmploymentOne, closedEmploymentTwo);
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        Assertions.assertFalse(actual.active());
    }

    @Test
    void map_whenEmployeeWillBeEmployedInTheFutureWithOpenEnd_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType futureActiveEmployment = createBeschaeftigungszeitType(LocalDate.now().plusDays(1), null);
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(futureActiveEmployment);
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        Assertions.assertFalse(actual.active());
    }

    @Test
    void map_whenEmployeeWillBeEmployedInTheFutureWithFixedEnd_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType futureActiveEmployment = createBeschaeftigungszeitType(LocalDate.now().plusDays(1), LocalDate.now()
                .plusDays(2));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(futureActiveEmployment);
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        Assertions.assertFalse(actual.active());
    }

    @Test
    void map_whenEmployeeIsEmployedOneDayOnCurrentDay_thenEmployeeIsActive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final LocalDate today = LocalDate.now();
        final BeschaeftigungszeitType futureActiveEmployment = createBeschaeftigungszeitType(today, today);
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(futureActiveEmployment);
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        Assertions.assertTrue(actual.active());
    }

    @Test
    void map_whenEmployeeIsCurrentlyEmployedWithOpenEnd_thenEmployeeIsActive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType activeEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(10), null);
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(activeEmployment);
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        Assertions.assertTrue(actual.active());
    }

    @Test
    void map_whenEmployeeIsCurrentlyEmployedWithFixedEndDate_thenEmployeeIsActive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType activeEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(10), LocalDate.now().plusDays(1));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(activeEmployment);
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        Assertions.assertTrue(actual.active());
    }

    @Test
    void map_whenEmployeeWasEmployedInThePastAndIsCurrentlyEmployed_thenEmployeeIsActive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType closedEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(10), LocalDate.now().minusDays(8));
        final BeschaeftigungszeitType activeEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(7), LocalDate.now().plusDays(1));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(closedEmployment, activeEmployment);
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        Assertions.assertTrue(actual.active());
    }

    @Test
    void map_whenEmployee_thenMappedProperly() {
        final BeschaeftigungszeitType activeEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(7), LocalDate.now().plusDays(1));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(activeEmployment);
        final MitarbeiterType employee = new MitarbeiterType();
        employee.setEmail("thomas.herzog@gepardec.com");
        employee.setVorname("Thomas");
        employee.setNachname("Herzog");
        employee.setTitel("Ing.");
        employee.setUserId("1");
        employee.setAnrede("Herr");
        employee.setPreisgruppe("ARCHITEKT");
        employee.setFreigabedatum("2020-01-01");
        employee.setRechte(Role.USER.roleId);
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        Assertions.assertNotNull(employee);
        Assertions.assertEquals(employee.getUserId(), actual.userId());
        Assertions.assertEquals(employee.getEmail(), actual.email());
        Assertions.assertEquals(employee.getVorname(), actual.firstName());
        Assertions.assertEquals(employee.getNachname(), actual.sureName());
        Assertions.assertEquals(employee.getTitel(), actual.title());
        Assertions.assertEquals(employee.getAnrede(), actual.salutation());
        Assertions.assertEquals(employee.getPreisgruppe(), actual.workDescription());
        Assertions.assertEquals(employee.getFreigabedatum(), actual.releaseDate());
        Assertions.assertEquals(employee.getRechte(), actual.role());
        Assertions.assertTrue(actual.active());
    }

    private BeschaeftigungszeitType createBeschaeftigungszeitType(final LocalDate start, final LocalDate end) {
        final BeschaeftigungszeitType beschaeftigung = new BeschaeftigungszeitType();
        beschaeftigung.setStartdatum((start != null) ? DateUtils.formatDate(start) : null);
        beschaeftigung.setEnddatum((end != null) ? DateUtils.formatDate(end) : null);
        return beschaeftigung;
    }

    private BeschaeftigungszeitListeType createBeschaeftigungszeitListeType(BeschaeftigungszeitType... employments) {
        final BeschaeftigungszeitListeType beschaeftigungszeitListeType = new BeschaeftigungszeitListeType();
        beschaeftigungszeitListeType.getBeschaeftigungszeit().addAll(List.of(employments));
        return beschaeftigungszeitListeType;
    }
}
