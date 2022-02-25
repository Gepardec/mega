package com.gepardec.mega.service.mapper;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.Role;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalQueries;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

@ApplicationScoped
public class SyncServiceMapper {

    @Inject
    Logger log;

    @Inject
    NotificationConfig notificationConfig;

    public User mapToDeactivatedUser(User user) {
        user.setActive(false);
        return user;
    }

    public User mapEmployeeToNewUser(final Employee employee, List<Project> projects, Locale defaultLocale) {
        final User user = new User();
        user.setZepId(employee.getUserId());
        return mapEmployeeToUser(user, employee, projects, defaultLocale);
    }

    public User mapEmployeeToUser(User user, Employee employee, List<Project> projects, Locale defaultLocale) {
        user.setEmail(employee.getEmail());
        user.setFirstname(employee.getFirstname());
        user.setLastname(employee.getLastname());
        user.setActive(employee.isActive());
        user.setReleaseDate(parseReleaseDate(employee));
        user.setRoles(determineRoles(employee, projects));
        setUserLocaleFromEmployeeLanguage(user, employee, defaultLocale);

        return user;
    }

    private LocalDate parseReleaseDate(Employee employee) {
        if (StringUtils.isNotBlank(employee.getReleaseDate()) && !employee.getReleaseDate().equals("NULL")) {
            try {
                return DateTimeFormatter.ISO_LOCAL_DATE.parse(employee.getReleaseDate(), TemporalQueries.localDate());
            } catch (DateTimeParseException exception) {
                log.warn("could not parse date {} for employee {}", employee.getReleaseDate(), employee.getUserId());
            }
        }
        return null;
    }

    private Set<Role> determineRoles(final Employee employee, final List<Project> projects) {
        final boolean projectLead = projects.stream()
                .anyMatch(project -> project.leads().contains(employee.getUserId()));
        final boolean omEmployee = notificationConfig.getOmMailAddresses().stream().anyMatch(omEmail -> omEmail.equals(employee.getEmail()));

        final Set<Role> roles = new HashSet<>();
        // Everyone if employee
        roles.add(Role.EMPLOYEE);
        if (projectLead) {
            roles.add(Role.PROJECT_LEAD);
        }
        if (omEmployee) {
            roles.add(Role.OFFICE_MANAGEMENT);
        }

        return roles;
    }

    private void setUserLocaleFromEmployeeLanguage(final User user, final Employee employee, final Locale defaultLocale) {
        if (employee.getLanguage() == null) {
            user.setLocale(defaultLocale);
        } else {
            try {
                if (Stream.of(Locale.getISOLanguages()).noneMatch(language -> language.equals(employee.getLanguage()))) {
                    throw new IllegalArgumentException("Language is not a valid iso language");
                }
                user.setLocale(Locale.forLanguageTag(employee.getLanguage()));
            } catch (Exception e) {
                log.warn("Employee '" + employee.getEmail()
                        + "' has an invalid language '" + employee.getLanguage()
                        + "' set, therefore set default Locale '" + defaultLocale.toString() + "'");
                user.setLocale(defaultLocale);
            }
        }
    }
}
