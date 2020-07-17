package com.gepardec.mega;

import com.gepardec.mega.domain.model.MonthlyReport;
import com.gepardec.mega.service.api.WorkerService;
import com.gepardec.mega.service.impl.WorkerServiceImpl;
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
