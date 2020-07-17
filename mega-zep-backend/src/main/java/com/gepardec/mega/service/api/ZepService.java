package com.gepardec.mega.service.api;

import com.gepardec.mega.domain.Employee;

import java.util.List;

public interface ZepService {
    Employee getEmployee(String userId);

    List<Employee> getEmployees();

    void updateEmployeesReleaseDate(String userId, String releaseDate);
}
