package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.security.Role;
import de.provantis.zep.MitarbeiterType;

import java.util.List;

public interface WorkerService {
    MitarbeiterType getEmployee (GoogleUser user);

    List<MitarbeiterType> getAllEmployees ();

    List<MitarbeiterType> getEmployeesByRoles(Role... roles);

    Integer updateEmployee(MitarbeiterType employee);

    Integer updateEmployees(List<MitarbeiterType> employees);

    MonthlyReport getMonthendReportForUser(GoogleUser user);
}