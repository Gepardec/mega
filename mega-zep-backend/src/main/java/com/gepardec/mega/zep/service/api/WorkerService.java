package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import de.provantis.zep.MitarbeiterType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface WorkerService {
    MitarbeiterType getEmployee(final String eMail);

    List<MitarbeiterType> getAllActiveEmployees();

    Integer updateEmployeeReleaseDate(final String eMail, final String releaseDate);

    Integer updateEmployeesReleaseDate(final List<Pair<String, String>> pairs);

    MonthlyReport getMonthendReportForUser(final String eMail);
}