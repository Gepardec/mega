package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import de.provantis.zep.MitarbeiterType;

import java.util.List;
import java.util.Map;

public interface WorkerService {
    MitarbeiterType getEmployee(final String eMail);

    List<MitarbeiterType> getAllActiveEmployees();

    void updateEmployeeReleaseDate(final String id, final String releaseDate);

    List<String> updateEmployeesReleaseDate(final Map<String, String> pairs);

    MonthlyReport getMonthendReportForUser(final String eMail);
}