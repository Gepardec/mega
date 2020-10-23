package com.gepardec.mega.zep;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ZepService {

    Employee getEmployee(String userId);

    List<Employee> getEmployees();

    void updateEmployeesReleaseDate(String userId, String releaseDate);

    List<ProjectEntry> getProjectTimes(Employee employee);

    Map<User, List<String>> getProjectsForUsersAndYear(LocalDate monthYear, List<User> users);

    Map<String, List<User>> getProjectLeadsForProjectsAndYear(LocalDate monthYear, List<User> users);
}
