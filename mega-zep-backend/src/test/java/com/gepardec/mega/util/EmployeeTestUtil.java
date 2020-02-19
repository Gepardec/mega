package com.gepardec.mega.util;

import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.service.model.Employee;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTestUtil {

    public static List<Employee> createEmployees(final int count) {
        final List<Employee> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(createEmployee(i));
        }

        return result;
    }

    public static Employee createEmployee(final int userId) {
        final Employee employee = new Employee();
        final String name = "Thomas_" + userId;

        employee.setEmail(name + "@gepardec.com");
        employee.setFirstName(name);
        employee.setSureName(name + "_Nachname");
        employee.setTitle("Ing.");
        employee.setUserId(String.valueOf(userId));
        employee.setSalutation("Herr");
        employee.setWorkDescription("ARCHITEKT");
        employee.setReleaseDate("2020-01-01");
        employee.setRole(Role.USER.roleId);
        employee.setActive(true);

        return employee;
    }

    public static void assertEmployee(final Employee actual, final Employee employee) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(employee.getRole(), actual.getRole(), "role"),
                () -> Assertions.assertEquals(employee.getUserId(), actual.getUserId(), "userId"),
                () -> Assertions.assertEquals(employee.getTitle(), actual.getTitle(), "title"),
                () -> Assertions.assertEquals(employee.getFirstName(), actual.getFirstName(), "firstName"),
                () -> Assertions.assertEquals(employee.getSureName(), actual.getSureName(), "sureName"),
                () -> Assertions.assertEquals(employee.getSalutation(), actual.getSalutation(), "salutation"),
                () -> Assertions.assertEquals(employee.getWorkDescription(), actual.getWorkDescription(), "workDescription"),
                () -> Assertions.assertEquals(employee.getReleaseDate(), actual.getReleaseDate(), "releaseDate"),
                () -> Assertions.assertEquals(employee.isActive(), actual.isActive(), "isActive"));
    }
}
