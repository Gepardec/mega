package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.db.entity.User;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        return employeeFor("1", "thomas.herzog@gepardec.com", language);
    }

    private Employee employeeForEmail(final String email) {
        return employeeFor("1", email, DEFAULT_FRENCH_LOCALE.getLanguage());
    }

    private Employee employeeForUserId(final String userId) {
        return employeeFor(userId, "thomas.herzog@gepardec.com", DEFAULT_FRENCH_LOCALE.getLanguage());
    }

    private Employee employeeFor(final String userId, final String email, final String language) {
        return Employee.builder()
                .userId(userId)
                .email(email)
                .firstname("Thomas")
                .lastname("Herzog")
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

        assertFalse(actual.getActive());
    }

    @Test
    void whenZepIdIsDifferent_thenZepIsNotUpdated() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final User user = new User();
        user.setZepId("2");
        final Employee employee = employeeForUserId("1");

        final User actual = mapper.mapEmployeeToUser(user, employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertEquals("2", actual.getZepId());
    }

    @Test
    void whenEmployeeDataDiffers_thenUserIsUpdated() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final User user = new User();
        user.setZepId("2");
        user.setRoles(Set.of(Role.EMPLOYEE, Role.OFFICE_MANAGEMENT, Role.PROJECT_LEAD));
        user.setFirstname("Werner");
        user.setLastname("Bruckmüller");
        user.setActive(false);

        final Employee employee = Employee.builder()
                .userId("2")
                .email("thomas.herzog@gepardec.com")
                .firstname("Thomas")
                .lastname("Herzog")
                .language("de")
                .active(true)
                .build();
        final Project project = projectForLeadUserId("2");

        final User actual = mapper.mapEmployeeToUser(user, employee, List.of(project), DEFAULT_FRENCH_LOCALE);

        assertAll(
                () -> assertEquals("2", actual.getZepId()),
                () -> assertEquals("thomas.herzog@gepardec.com", actual.getEmail()),
                () -> assertEquals("Thomas", actual.getFirstname()),
                () -> assertEquals("Herzog", actual.getLastname()),
                () -> assertEquals(Locale.GERMAN, actual.getLocale()),
                () -> assertNull(actual.getReleaseDate()),
                () -> assertTrue(actual.getActive()),
                () -> assertEquals(2, actual.getRoles().size()),
                () -> assertTrue(actual.getRoles().contains(Role.EMPLOYEE)),
                () -> assertTrue(actual.getRoles().contains(Role.PROJECT_LEAD)));
    }

    @Test
    void whenCalled_thenUserHasRoleEmployee() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForUserId("1");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertTrue(actual.getRoles().contains(Role.EMPLOYEE));
    }

    @Test
    void whenEmployee_thenUser() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = Employee.builder()
                .userId("1")
                .email("thomas.herzog@gepardec.com")
                .firstname("Thomas")
                .lastname("Herzog")
                .language("de")
                .releaseDate("2020-11-12")
                .active(true)
                .build();
        final Project project = projectForLeadUserId("1");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(project), DEFAULT_FRENCH_LOCALE);

        assertAll(
                () -> assertEquals("1", actual.getZepId()),
                () -> assertEquals("thomas.herzog@gepardec.com", actual.getEmail()),
                () -> assertEquals("Thomas", actual.getFirstname()),
                () -> assertEquals("Herzog", actual.getLastname()),
                () -> assertEquals(Locale.GERMAN, actual.getLocale()),
                () -> assertEquals(LocalDate.of(2020, 11, 12), actual.getReleaseDate()),
                () -> assertTrue(actual.getActive()),
                () -> assertEquals(2, actual.getRoles().size()),
                () -> assertTrue(actual.getRoles().contains(Role.EMPLOYEE)),
                () -> assertTrue(actual.getRoles().contains(Role.PROJECT_LEAD)));
    }

    @Test
    void whenNoProjects_thenUserHasNotRoleProjectLead() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());

        final Employee employee = employeeForUserId("1");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertFalse(actual.getRoles().contains(Role.PROJECT_LEAD));
    }

    @Test
    void whenProjectsAndNoEmployeeIsLead_thenNoUserHasRoleProjectLead() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());

        final Employee employee = employeeForUserId("1");
        final Project project = projectForLeadUserId("2");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(project), DEFAULT_FRENCH_LOCALE);

        assertFalse(actual.getRoles().contains(Role.PROJECT_LEAD));
    }

    @Test
    void whenProjectsAndEmployeeIsLead_thenNoUserHasRoleProjectLead() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());

        final Employee employee = employeeForUserId("1");
        final Project project = projectForLeadUserId("1");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(project), DEFAULT_FRENCH_LOCALE);

        assertTrue(actual.getRoles().contains(Role.PROJECT_LEAD));
    }

    @Test
    void whenNoOmEmails_thenNoUserHasRoleOfficeManagement() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForEmail("thomas.herzog@gepardec.com");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertFalse(actual.getRoles().contains(Role.OFFICE_MANAGEMENT));
    }

    @Test
    void whenOmEmailsAndNoEmployeeIsOm_thenNoUserHasRoleOfficeManagement() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of("herzog.thomas81@gmail.com"));
        final Employee employee = employeeForEmail("thomas.herzog@gepardec.com");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertFalse(actual.getRoles().contains(Role.OFFICE_MANAGEMENT));
    }

    @Test
    void whenOmEmailsAndEmployeeIsOm_thenUserHasRoleOfficeManagement() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of("thomas.herzog@gepardec.com"));
        final Employee employee = employeeForEmail("thomas.herzog@gepardec.com");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertTrue(actual.getRoles().contains(Role.OFFICE_MANAGEMENT));
    }

    @Test
    void whenLanguageIsNull_thenUserHasDefaultLocale() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForLanguage(null);

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertEquals(actual.getLocale(), DEFAULT_FRENCH_LOCALE);
    }

    @Test
    void whenLanguageIsInvalid_thenUserHasDefaultLocale() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForLanguage("xx");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertEquals(actual.getLocale(), DEFAULT_FRENCH_LOCALE);
    }

    @Test
    void whenLanguageIsInvalid_thenLogsWarning() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForLanguage("xx");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        verify(log, times(1)).warn(anyString());
    }

    @Test
    void whenLanguage_thenUserHasLocale() {
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        final Employee employee = employeeForLanguage("de");

        final User actual = mapper.mapEmployeeToNewUser(employee, List.of(), DEFAULT_FRENCH_LOCALE);

        assertEquals(actual.getLocale(), Locale.GERMAN);
    }
}
