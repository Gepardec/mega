package com.gepardec.mega.service.api;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;

import java.time.LocalDate;

public interface MonthlyReportService {

    MonthlyReport getMonthendReportForUser(final String userId);

    MonthlyReport getMonthendReportForUser(final String userId, LocalDate date);

    boolean setOpenAndUnassignedStepEntriesDone(Employee employee, Long stepId);
}
