package com.gepardec.mega;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.zep.service.api.WorkerService;
import com.gepardec.mega.zep.service.impl.WorkerServiceImpl;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Mock
public class WorkerServiceMock extends WorkerServiceImpl {

    WorkerService delegate;

    public WorkerServiceMock() {

    }

    @Override
    public MonthlyReport getMonthendReportForUser(String userId) {
        return delegate.getMonthendReportForUser(userId);
    }

    public void setDelegate(WorkerService delegate) {
        this.delegate = delegate;
    }
}
