package com.gepardec.mega;

import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import com.gepardec.mega.service.impl.monthlyreport.MonthlyReportServiceImpl;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Mock
public class MonthlyReportServiceMock extends MonthlyReportServiceImpl {

    MonthlyReportService delegate;

    public MonthlyReportServiceMock() {

    }

    @Override
    public MonthlyReport getMonthendReportForUser(String userId) {
        return delegate.getMonthendReportForUser(userId);
    }

    public void setDelegate(MonthlyReportService delegate) {
        this.delegate = delegate;
    }
}
