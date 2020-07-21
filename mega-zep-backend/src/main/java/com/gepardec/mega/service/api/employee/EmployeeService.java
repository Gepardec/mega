package com.gepardec.mega.service.api.employee;

import com.gepardec.mega.domain.model.employee.Employee;

import java.util.List;

public interface EmployeeService {

    Employee getEmployee(final String userId);

    List<Employee> getAllActiveEmployees();

    void updateEmployeeReleaseDate(final String userId, final String releaseDate);

    List<String> updateEmployeesReleaseDate(List<Employee> employees);
}
