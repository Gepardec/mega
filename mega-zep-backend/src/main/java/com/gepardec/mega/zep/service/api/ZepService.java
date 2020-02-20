package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.service.model.Employee;

import java.util.List;

public interface ZepService {
    Employee getEmployee(String userId);

    List<Employee> getEmployees();

    void updateEmployeesReleaseDate(String userId, String releaseDate);
}
