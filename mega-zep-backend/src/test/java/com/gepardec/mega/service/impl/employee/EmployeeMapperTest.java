package com.gepardec.mega.service.impl.employee;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.mapper.EmployeeMapper;
import de.provantis.zep.BeschaeftigungszeitListeType;
import de.provantis.zep.BeschaeftigungszeitType;
import de.provantis.zep.MitarbeiterType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeMapperTest {

    private EmployeeMapper mapper;

    @BeforeEach
    void beforeEach() {
        mapper = new EmployeeMapper();
    }

    @Test
    void map_whenEmployeeIsNull_thenReturnsNull() {
        assertThat(mapper.map(null)).isNull();
    }

    @Test
    void map_whenBeschaeftingungszeitListeIsNull_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isFalse();
    }

    @Test
    void map_whenBeschaeftingungszeitListeIsEmpty_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        employee.setBeschaeftigungszeitListe(new BeschaeftigungszeitListeType());

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isFalse();
    }

    @Test
    void map_whenEmployeeWasEmployedInThePastOnce_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType closedEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(List.of(closedEmployment));
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isFalse();
    }

    @Test
    void map_whenEmployeeWasEmployedInThePastMultipleTimes_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType closedEmploymentOne = createBeschaeftigungszeitType(LocalDate.now().minusDays(10), LocalDate.now()
                .minusDays(8));
        final BeschaeftigungszeitType closedEmploymentTwo = createBeschaeftigungszeitType(LocalDate.now().minusDays(7), LocalDate.now().minusDays(4));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(List.of(closedEmploymentOne, closedEmploymentTwo));
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isFalse();
    }

    @Test
    void map_whenEmployeeWillBeEmployedInTheFutureWithOpenEnd_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType futureActiveEmployment = createBeschaeftigungszeitType(LocalDate.now().plusDays(1), null);
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(List.of(futureActiveEmployment));
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isFalse();
    }

    @Test
    void map_whenEmployeeWillBeEmployedInTheFutureWithFixedEnd_thenEmployeeIsInactive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType futureActiveEmployment = createBeschaeftigungszeitType(LocalDate.now().plusDays(1), LocalDate.now()
                .plusDays(2));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(List.of(futureActiveEmployment));
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isFalse();
    }

    @Test
    void map_whenEmployeeIsEmployedOneDayOnCurrentDay_thenEmployeeIsActive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final LocalDate today = LocalDate.now();
        final BeschaeftigungszeitType futureActiveEmployment = createBeschaeftigungszeitType(today, today);
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(List.of(futureActiveEmployment));
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isTrue();
    }

    @Test
    void map_whenEmployeeIsCurrentlyEmployedWithOpenEnd_thenEmployeeIsActive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType activeEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(10), null);
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(List.of(activeEmployment));
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isTrue();
    }

    @Test
    void map_whenEmployeeIsCurrentlyEmployedWithFixedEndDate_thenEmployeeIsActive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType activeEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(10), LocalDate.now().plusDays(1));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(List.of(activeEmployment));
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isTrue();
    }

    @Test
    void map_whenEmployeeWasEmployedInThePastAndIsCurrentlyEmployed_thenEmployeeIsActive() {
        final MitarbeiterType employee = new MitarbeiterType();
        final BeschaeftigungszeitType closedEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(10), LocalDate.now().minusDays(8));
        final BeschaeftigungszeitType activeEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(7), LocalDate.now().plusDays(1));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(List.of(closedEmployment, activeEmployment));
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        assertThat(actual.isActive()).isTrue();
    }

    @Test
    void map_whenEmployee_thenMappedProperly() {
        final BeschaeftigungszeitType activeEmployment = createBeschaeftigungszeitType(LocalDate.now().minusDays(7), LocalDate.now().plusDays(1));
        final BeschaeftigungszeitListeType employments = createBeschaeftigungszeitListeType(List.of(activeEmployment));
        final MitarbeiterType employee = new MitarbeiterType();
        employee.setEmail("no-reply@gepardec.com");
        employee.setVorname("Max");
        employee.setNachname("Mustermann");
        employee.setTitel("Ing.");
        employee.setUserId("1");
        employee.setAnrede("Herr");
        employee.setPreisgruppe("ARCHITEKT");
        employee.setFreigabedatum("2020-01-01");
        employee.setBeschaeftigungszeitListe(employments);

        final Employee actual = mapper.map(employee);
        assertThat(employee).isNotNull();
        assertThat(actual.getUserId()).isEqualTo(employee.getUserId());
        assertThat(actual.getEmail()).isEqualTo(employee.getEmail());
        assertThat(actual.getFirstname()).isEqualTo(employee.getVorname());
        assertThat(actual.getLastname()).isEqualTo(employee.getNachname());
        assertThat(actual.getTitle()).isEqualTo(employee.getTitel());
        assertThat(actual.getSalutation()).isEqualTo(employee.getAnrede());
        assertThat(actual.getWorkDescription()).isEqualTo(employee.getPreisgruppe());
        assertThat(actual.getReleaseDate()).isEqualTo(employee.getFreigabedatum());
        assertThat(actual.isActive()).isTrue();
    }

    private BeschaeftigungszeitType createBeschaeftigungszeitType(final LocalDate start, final LocalDate end) {
        final BeschaeftigungszeitType beschaeftigung = new BeschaeftigungszeitType();
        beschaeftigung.setStartdatum((start != null) ? DateUtils.formatDate(start) : null);
        beschaeftigung.setEnddatum((end != null) ? DateUtils.formatDate(end) : null);
        return beschaeftigung;
    }

    private BeschaeftigungszeitListeType createBeschaeftigungszeitListeType(List<BeschaeftigungszeitType> employments) {
        final BeschaeftigungszeitListeType beschaeftigungszeitListeType = new BeschaeftigungszeitListeType();
        beschaeftigungszeitListeType.getBeschaeftigungszeit().addAll(employments);
        return beschaeftigungszeitListeType;
    }
}
