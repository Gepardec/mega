package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.monthendreport.MonthendReport;
import com.gepardec.mega.security.Role;
import de.provantis.zep.MitarbeiterType;

import java.util.List;

public interface WorkerService {
    MitarbeiterType getEmployee (GoogleUser user);

    List<MitarbeiterType> getAllEmployees ();

    List<MitarbeiterType> getEmployeeByType(Role... roles);

    Integer updateEmployee(MitarbeiterType employee);

    Integer updateEmployees(List<MitarbeiterType> employees);

    MonthendReport getMonthendReportForUser(GoogleUser user);
}