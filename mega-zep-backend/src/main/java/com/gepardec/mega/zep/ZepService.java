package com.gepardec.mega.zep;

import com.gepardec.mega.domain.model.employee.Employee;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;

import java.util.List;

public interface ZepService {

    Employee getEmployee(String userId);

    List<Employee> getEmployees();

    void updateEmployeesReleaseDate(String userId, String releaseDate);

    List<ProjectTimeEntry> getProjectTimes(Employee employee);
}
