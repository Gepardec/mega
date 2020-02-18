package com.gepardec.mega.zep.service;

import com.gepardec.mega.service.model.Employee;

import java.util.List;

public interface ZepService {
    Employee getEmployee(String userId);

    List<Employee> getActiveEmployees();

    void updateEmployeesReleaseDate(String userId, String releaseDate);
}
