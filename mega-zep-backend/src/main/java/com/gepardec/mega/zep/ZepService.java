package com.gepardec.mega.zep;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.ProjectTimeEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ZepService {
    Employee getEmployee(String userId);

    List<Employee> getEmployees();

    void updateEmployeesReleaseDate(String userId, String releaseDate);

    List<ProjectTimeEntry> getProjectTimes(Employee employee);
}
