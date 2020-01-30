package com.gepardec.mega;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.monthlyreport.warning.WarningConfig;
import com.gepardec.mega.zep.service.impl.WorkerServiceImpl;
import com.gepardec.mega.zep.soap.ZepSoapProvider;
import de.provantis.zep.MitarbeiterType;
import de.provantis.zep.ZepSoapPortType;
import io.quarkus.test.Mock;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Mock
public class WorkerServiceMock extends WorkerServiceImpl {

    WorkerServiceImpl delegate;

    public WorkerServiceMock() {

    }

    @Override
    public MitarbeiterType getEmployee(String userId) {
        return delegate.getEmployee(userId);
    }

    @Override
    public List<MitarbeiterType> getAllActiveEmployees() {return delegate.getAllActiveEmployees();}

    @Override
    public List<String> updateEmployeesReleaseDate(Map<String, String> emailReleaseDates) {return delegate.updateEmployeesReleaseDate(emailReleaseDates);}

    @Override
    public MonthlyReport getMonthendReportForUser(String userId) {return delegate.getMonthendReportForUser(userId);}

    @Override
    public void updateEmployeeReleaseDate(String id, String releaseDate) {delegate.updateEmployeeReleaseDate(id, releaseDate);}

    public void setDelegate(WorkerServiceImpl delegate) {
        this.delegate = delegate;
    }
}
