package com.gepardec.mega.service.api;

import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.domain.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee getEmployee(final String userId);

    List<Employee> getAllActiveEmployees();

    void updateEmployeeReleaseDate(final String userId, final String releaseDate);

    List<String> updateEmployeesReleaseDate(List<Employee> employees);

    List<User> getAll();
}
