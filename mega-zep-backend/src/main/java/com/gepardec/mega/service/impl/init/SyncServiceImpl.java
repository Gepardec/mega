package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.db.entity.User;
import com.gepardec.mega.db.repository.UserRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.init.SyncService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
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
    EmployeeService employeeService;

    @Inject
    UserRepository userRepository;

    @Override
    public void syncEmployees() {
        final List<Employee> employees = employeeService.getAllActiveEmployees();
        final List<User> users = userRepository.listAll();
        createNonExistentUsers(employees, users);
        updateModifiedUsers(employees, users);
        deactivateDeletedOrInactiveUsers(employees, users);
    }

    private void createNonExistentUsers(final List<Employee> employees, final List<User> users) {
        final List<User> notExistentUsers = filterNotExistingEmployeesAndMapToUser(employees, users);
        if (!notExistentUsers.isEmpty()) {
            notExistentUsers.forEach(userRepository::persist);
        }
    }

    private void updateModifiedUsers(final List<Employee> employees, final List<User> users) {
        final List<User> modifiedUsers = filterModifiedEmployeesAndUpdateUsers(employees, users);
        if (!modifiedUsers.isEmpty()) {
            modifiedUsers.forEach(userRepository::update);
        }
    }

    private void deactivateDeletedOrInactiveUsers(final List<Employee> employees, final List<User> users) {
        final List<User> removedUsers = filterUserNotMappedToEmployeesAndMarkUserDeactivated(employees, users);
        if (!removedUsers.isEmpty()) {
            removedUsers.forEach(userRepository::update);
        }
    }

    private List<User> filterNotExistingEmployeesAndMapToUser(final List<Employee> employees, final List<User> users) {
        final Map<String, User> zepIdToUser = mapZepItToUser(users);
        return employees.stream()
                .filter(zepEmployee -> !zepIdToUser.containsKey(zepEmployee.userId()))
                .map(this::employeeToUser)
                .collect(Collectors.toList());
    }

    private List<User> filterUserNotMappedToEmployeesAndMarkUserDeactivated(final List<Employee> employees, final List<User> users) {
        final Map<String, Employee> zepIdToEmployee = mapZepItToEmployee(employees);
        return users.stream()
                .filter(user -> !zepIdToEmployee.containsKey(user.getZepId()))
                .map(this::markUserDeactivated)
                .collect(Collectors.toList());
    }

    private List<User> filterModifiedEmployeesAndUpdateUsers(final List<Employee> employees, final List<User> users) {
        final Map<String, User> zepIdToUser = mapZepItToUser(users);
        final Map<User, Employee> existingUserToEmployee = employees.stream()
                .filter(zepEmployee -> zepIdToUser.containsKey(zepEmployee.userId()))
                .collect(Collectors.toMap(employee -> zepIdToUser.get(employee.userId()), Function.identity()));
        return existingUserToEmployee.entrySet().stream()
                .filter(entry -> hasEmployeeChanged(entry.getKey(), entry.getValue()))
                .map(entry -> updateUserData(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private boolean hasEmployeeChanged(final User user, final Employee employee) {
        return !user.getEmail().equals(employee.email())
                || !user.getActive();
    }

    private User markUserDeactivated(User user) {
        user.setActive(false);
        return user;
    }

    private User updateUserData(User user, Employee employee) {
        user.setEmail(employee.email());
        user.setActive(true);
        return user;
    }

    private User employeeToUser(final Employee employee) {
        final User user = new User();
        user.setZepId(employee.userId());
        user.setEmail(employee.email());
        user.setActive(true);

        return user;
    }

    private Map<String, User> mapZepItToUser(final List<User> users) {
        return users.stream()
                .collect(Collectors.toMap(User::getZepId, Function.identity()));
    }

    private Map<String, Employee> mapZepItToEmployee(final List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.toMap(Employee::userId, Function.identity()));
    }
}
