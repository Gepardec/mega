package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.monthlyreport.MonthlyReport;

public interface WorkerService {

    MonthlyReport getMonthendReportForUser(final String userId);
}