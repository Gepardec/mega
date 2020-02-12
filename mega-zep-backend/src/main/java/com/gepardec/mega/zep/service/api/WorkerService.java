package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.rest.model.Employee;
import de.provantis.zep.MitarbeiterType;

import java.util.List;

public interface WorkerService {
    MitarbeiterType getEmployee(final String userId);

    List<MitarbeiterType> getAllActiveEmployees();

    void updateEmployeeReleaseDate(final String id, final String releaseDate);

    List<String> updateEmployeesReleaseDate(List<Employee> employees);

    MonthlyReport getMonthendReportForUser(final String userId);
}