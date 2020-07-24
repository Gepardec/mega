package com.gepardec.mega.service.api.monthlyreport;

import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;

public interface MonthlyReportService {

    MonthlyReport getMonthendReportForUser(final String userId);
}