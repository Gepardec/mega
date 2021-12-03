package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncServiceMapperTest {

    private static final Locale DEFAULT_FRENCH_LOCALE = Locale.FRENCH;

    @Mock
    private Logger log;

    @Mock
    private NotificationConfig notificationConfig;

    @InjectMocks
    private SyncServiceMapper mapper;

    private Employee employeeForLanguage(final String language) {
        return employeeFor("1", "no-reply@gepardec.com", language);
    }

    private Employee employeeForEmail(final String email) {
        return employeeFor("1", email, DEFAULT_FRENCH_LOCALE.getLanguage());
    }

    private Employee employeeForUserId(final String userId) {
        return employeeFor(userId, "no-reply@gepardec.com", DEFAULT_FRENCH_LOCALE.getLanguage());
    }

    private Employee employeeFor(final String userId, final String email, final String language) {
        return Employee.builder()
                .userId(userId)
                .email(email)
                .firstname("Max")
                .lastname("Mustermann")
                .language(language)
                .releaseDate("NULL")
                .active(true)
                .build();
    }

    private Project projectForLeadUserId(final String userId) {
        return Project.builder()
                .projectId("1")
                .employees(List.of())
                .leads(List.of(userId))
                .categories(List.of())
                .startDate(LocalDate.now())
                .build();
    }

    @Test
    void whenCalled_thenActiveSetToFalse() {
        final User user = new User();
        user.setActive(true);

        final User actual = mapper.mapToDeactivatedUser(user);

        assertThat(actual.getActive()).isFalse();
    }

    @Test
    void whenZepIdIsDifferent_thenZepIsNotUpdated() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final User user = new User();
        user.setZepId("2");
        final Employee employee = employeeForUserId("1");

        final User actual = mapper.mapEmployeeToUser(user, employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertThat(actual.getZepId()).isEqualTo("2");
    }

    @Test
    void whenEmployeeDataDiffers_thenUserIsUpdated() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final User user = new User();
        user.setZepId("2");
        user.setRoles(Set.of(Role.EMPLOYEE, Role.OFFICE_MANAGEMENT, Role.PROJECT_LEAD));
        user.setFirstname("Max");
        user.setLastname("Mustermann");
        user.setActive(false);

        final Employee employee = Employee.builder()
                .userId("2")
                .email("no-reply@gepardec.com")
                .firstname("Max")
                .lastname("Mustermann")
                .language("de")
                .active(true)
                .build();
        final Project project = projectForLeadUserId("2");

        final User actual = mapper.mapEmployeeToUser(user, employee, List.of(project), DEFAULT_FRENCH_LOCALE);

        assertAll(
                () -> assertThat(actual.getZepId()).isEqualTo("2"),
                () -> assertThat(actual.getEmail()).isEqualTo("no-reply@gepardec.com"),
                () -> assertThat(actual.getFirstname()).isEqualTo("Max"),
                () -> assertThat(actual.getLastname()).isEqualTo("Mustermann"),
                () -> assertThat(actual.getLocale()).isEqualTo(Locale.GERMAN),
                () -> assertThat(actual.getReleaseDate()).isNull(),
                () -> assertThat(actual.getActive()).isTrue(),
                () -> assertThat(actual.getRoles()).hasSize(2),
                () -> assertThat(actual.getRoles()).contains(Role.EMPLOYEE),
                () -> assertThat(actual.getRoles()).contains(Role.PROJECT_LEAD));
    }

    @Test
    void whenCalled_thenUserHasRoleEmployee() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForUserId("1");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertThat(actual.getRoles()).contains(Role.EMPLOYEE);
    }

    @Test
    void whenEmployee_thenUser() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = Employee.builder()
                .userId("1")
                .email("no-reply@gepardec.com")
                .firstname("Max")
                .lastname("Mustermann")
                .language("de")
                .releaseDate("2020-11-12")
                .active(true)
                .build();
        final Project project = projectForLeadUserId("1");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(project), DEFAULT_FRENCH_LOCALE);

        assertAll(
                () -> assertThat(actual.getZepId()).isEqualTo("1"),
                () -> assertThat(actual.getEmail()).isEqualTo("no-reply@gepardec.com"),
                () -> assertThat(actual.getFirstname()).isEqualTo("Max"),
                () -> assertThat(actual.getLastname()).isEqualTo("Mustermann"),
                () -> assertThat(actual.getLocale()).isEqualTo(Locale.GERMAN),
                () -> assertThat(actual.getReleaseDate()).isEqualTo(LocalDate.of(2020, 11, 12)),
                () -> assertThat(actual.getActive()).isTrue(),
                () -> assertThat(actual.getRoles()).hasSize(2),
                () -> assertThat(actual.getRoles()).contains(Role.EMPLOYEE),
                () -> assertThat(actual.getRoles()).contains(Role.PROJECT_LEAD));
    }

    @Test
    void whenNoProjects_thenUserHasNotRoleProjectLead() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());

        final Employee employee = employeeForUserId("1");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertThat(actual.getRoles()).doesNotContain(Role.PROJECT_LEAD);
    }

    @Test
    void whenProjectsAndNoEmployeeIsLead_thenNoUserHasRoleProjectLead() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());

        final Employee employee = employeeForUserId("1");
        final Project project = projectForLeadUserId("2");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(project), DEFAULT_FRENCH_LOCALE);

        assertThat(actual.getRoles()).doesNotContain(Role.PROJECT_LEAD);
    }

    @Test
    void whenProjectsAndEmployeeIsLead_thenNoUserHasRoleProjectLead() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());

        final Employee employee = employeeForUserId("1");
        final Project project = projectForLeadUserId("1");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(project), DEFAULT_FRENCH_LOCALE);

        assertThat(actual.getRoles()).contains(Role.PROJECT_LEAD);
    }

    @Test
    void whenNoOmEmails_thenNoUserHasRoleOfficeManagement() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForEmail("no-reply@gepardec.com");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertThat(actual.getRoles()).doesNotContain(Role.OFFICE_MANAGEMENT);
    }

    @Test
    void whenOmEmailsAndNoEmployeeIsOm_thenNoUserHasRoleOfficeManagement() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of("max.mustermann81@gmail.com"));
        final Employee employee = employeeForEmail("no-reply@gepardec.com");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertThat(actual.getRoles()).doesNotContain(Role.OFFICE_MANAGEMENT);
    }

    @Test
    void whenOmEmailsAndEmployeeIsOm_thenUserHasRoleOfficeManagement() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of("no-reply@gepardec.com"));
        final Employee employee = employeeForEmail("no-reply@gepardec.com");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertThat(actual.getRoles()).contains(Role.OFFICE_MANAGEMENT);
    }

    @Test
    void whenLanguageIsNull_thenUserHasDefaultLocale() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForLanguage(null);

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertThat(DEFAULT_FRENCH_LOCALE).isEqualTo(actual.getLocale());
    }

    @Test
    void whenLanguageIsInvalid_thenUserHasDefaultLocale() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForLanguage("xx");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertThat(DEFAULT_FRENCH_LOCALE).isEqualTo(actual.getLocale());
    }

    @Test
    void whenLanguageIsInvalid_thenLogsWarning() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForLanguage("xx");

        mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        verify(log, times(1)).warn(anyString());
    }

    @Test
    void whenLanguage_thenUserHasLocale() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForLanguage("de");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertThat(Locale.GERMAN).isEqualTo(actual.getLocale());
    }
}
