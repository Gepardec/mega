package com.gepardec.mega.service.api;

import com.gepardec.mega.domain.model.MonthlyReport;

public interface WorkerService {

    MonthlyReport getMonthendReportForUser(final String userId);
}