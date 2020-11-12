package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.db.entity.User;
import com.gepardec.mega.db.repository.UserRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.init.SyncService;
import com.gepardec.mega.service.api.project.ProjectService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 10/3/2020
 */
@Dependent
@Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
public class SyncServiceImpl implements SyncService {

    @Inject
    Logger log;

    @Inject
    EmployeeService employeeService;

    @Inject
    ProjectService projectService;

    @Inject
    UserRepository userRepository;

    @Inject
    ApplicationConfig applicationConfig;

    @Inject
    SyncServiceMapper mapper;

    @Override
    public void syncEmployees() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Started user sync: {}", Instant.ofEpochMilli(stopWatch.getStartTime()));

        final List<Project> projects = projectService.getProjectsForMonthYear(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
        log.info("Loaded projects: {}", projects.size());

        final List<Employee> employees = employeeService.getAllActiveEmployees();
        log.info("Loaded employees: {}", employees.size());

        final List<User> users = userRepository.listAll();
        log.info("Existing users: {}", users.size());

        createNonExistentUsers(employees, users, projects);
        updateModifiedUsers(employees, users, projects);
        deactivateDeletedOrInactiveUsers(employees, users);

        log.info("User sync took: {}ms", stopWatch.getTime());
        log.info("Finished user sync: {}", Instant.ofEpochMilli(stopWatch.getStartTime() + stopWatch.getTime()));
    }

    private void createNonExistentUsers(final List<Employee> employees, final List<User> users, final List<Project> projects) {
        final List<User> notExistentUsers = filterNotExistingEmployeesAndMapToUser(employees, users, projects);
        if (!notExistentUsers.isEmpty()) {
            notExistentUsers.forEach(userRepository::persist);
        }
        log.info("Created users: {}", notExistentUsers.size());
    }

    private void updateModifiedUsers(final List<Employee> employees, final List<User> users, final List<Project> project) {
        final List<User> modifiedUsers = filterModifiedEmployeesAndUpdateUsers(employees, users, project);
        if (!modifiedUsers.isEmpty()) {
            modifiedUsers.forEach(userRepository::update);
        }
        log.info("Updated users: {}", modifiedUsers.size());
    }

    private void deactivateDeletedOrInactiveUsers(final List<Employee> employees, final List<User> users) {
        final List<User> removedUsers = filterUserNotMappedToEmployeesAndMarkUserDeactivated(employees, users);
        if (!removedUsers.isEmpty()) {
            removedUsers.forEach(userRepository::update);
        }
        log.info("Deleted users: {}", removedUsers.size());
    }

    private List<User> filterNotExistingEmployeesAndMapToUser(final List<Employee> employees, final List<User> users, final List<Project> projects) {
        final Map<String, User> zepIdToUser = mapZepIdToUser(users);
        final Locale defaultLocale = applicationConfig.getDefaultLocale();
        return employees.stream()
                .filter(zepEmployee -> !zepIdToUser.containsKey(zepEmployee.userId()))
                .map(employee -> mapper.mapEmployeeToUser(employee, projects, defaultLocale))
                .collect(Collectors.toList());
    }

    private List<User> filterUserNotMappedToEmployeesAndMarkUserDeactivated(final List<Employee> employees, final List<User> users) {
        final Map<String, Employee> zepIdToEmployee = mapZepIdToEmployee(employees);
        return users.stream()
                .filter(user -> !zepIdToEmployee.containsKey(user.getZepId()))
                .map(this::markUserDeactivated)
                .collect(Collectors.toList());
    }

    private List<User> filterModifiedEmployeesAndUpdateUsers(final List<Employee> employees, final List<User> users, final List<Project> projects) {
        final Map<String, User> zepIdToUser = mapZepIdToUser(users);
        final Map<User, Employee> existingUserToEmployee = employees.stream()
                .filter(zepEmployee -> zepIdToUser.containsKey(zepEmployee.userId()))
                .collect(Collectors.toMap(employee -> zepIdToUser.get(employee.userId()), Function.identity()));
        final Locale defaultLocale = applicationConfig.getDefaultLocale();
        return existingUserToEmployee.entrySet().stream()
                .map(entry -> mapper.mapEmployeeToUser(entry.getKey(), entry.getValue(), projects, defaultLocale))
                .collect(Collectors.toList());
    }

    private User markUserDeactivated(User user) {
        user.setActive(false);
        return user;
    }

    private Map<String, User> mapZepIdToUser(final List<User> users) {
        return users.stream()
                .collect(Collectors.toMap(User::getZepId, Function.identity()));
    }

    private Map<String, Employee> mapZepIdToEmployee(final List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.toMap(Employee::userId, Function.identity()));
    }
}
