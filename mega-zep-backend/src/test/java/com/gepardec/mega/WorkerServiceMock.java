package com.gepardec.mega;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.zep.service.impl.WorkerServiceImpl;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Mock
public class WorkerServiceMock extends WorkerServiceImpl {

    WorkerServiceImpl delegate;

    @Override
    public MitarbeiterType getEmployee(String userId) {
        return delegate.getEmployee(userId);
    }

    @Override
    public List<MitarbeiterType> getAllActiveEmployees() {return delegate.getAllActiveEmployees();}

    @Override
    public List<String> updateEmployeesReleaseDate(Map<String, String> emailReleaseDates) {return delegate.updateEmployeesReleaseDate(emailReleaseDates);}

    @Override
    public MonthlyReport getMonthendReportForUser(String eMail) {return delegate.getMonthendReportForUser(eMail);}

    @Override
    public void updateEmployeeReleaseDate(String id, String releaseDate) {delegate.updateEmployeeReleaseDate(id, releaseDate);}

    public void setDelegate(WorkerServiceImpl delegate) {
        this.delegate = delegate;
    }
}
