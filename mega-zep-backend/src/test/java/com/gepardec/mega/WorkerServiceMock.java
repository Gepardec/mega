package com.gepardec.mega;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.rest.model.Employee;
import com.gepardec.mega.zep.service.impl.WorkerServiceImpl;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

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
    public List<String> updateEmployeesReleaseDate(List<Employee> employees) {return delegate.updateEmployeesReleaseDate(employees);}

    @Override
    public MonthlyReport getMonthendReportForUser(String userId) {return delegate.getMonthendReportForUser(userId);}

    @Override
    public void updateEmployeeReleaseDate(String userId, String releaseDate) {delegate.updateEmployeeReleaseDate(userId, releaseDate);}

    public void setDelegate(WorkerServiceImpl delegate) {
        this.delegate = delegate;
    }
}
