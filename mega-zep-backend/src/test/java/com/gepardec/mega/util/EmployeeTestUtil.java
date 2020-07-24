package com.gepardec.mega.util;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.domain.model.employee.Employee;
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
        return EmployeeTestUtil.createEmployee(userId, "2020-01-01");
    }

    public static Employee createEmployee(final int userId, final String releaseDate) {
        return EmployeeTestUtil.createEmployee(userId, releaseDate, true);
    }

    public static Employee createEmployee(final int userId, final String releaseDate, final boolean active) {
        final String name = "Thomas_" + userId;

        final Employee employee = Employee.builder()
                .email(name + "@gepardec.com")
                .firstName(name)
                .sureName(name + "_Nachname")
                .title("Ing.")
                .userId(String.valueOf(userId))
                .salutation("Herr")
                .workDescription("ARCHITEKT")
                .releaseDate(releaseDate)
                .role(Role.USER.roleId)
                .active(active)
                .build();

        return employee;
    }

    public static void assertEmployee(final Employee actual, final Employee employee) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(employee.role(), actual.role(), "role"),
                () -> Assertions.assertEquals(employee.userId(), actual.userId(), "userId"),
                () -> Assertions.assertEquals(employee.title(), actual.title(), "title"),
                () -> Assertions.assertEquals(employee.firstName(), actual.firstName(), "firstName"),
                () -> Assertions.assertEquals(employee.sureName(), actual.sureName(), "sureName"),
                () -> Assertions.assertEquals(employee.salutation(), actual.salutation(), "salutation"),
                () -> Assertions.assertEquals(employee.workDescription(), actual.workDescription(), "workDescription"),
                () -> Assertions.assertEquals(employee.releaseDate(), actual.releaseDate(), "releaseDate"),
                () -> Assertions.assertEquals(employee.active(), actual.active(), "isActive"));
    }
}
