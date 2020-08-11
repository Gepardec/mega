package com.gepardec.mega;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.service.api.employee.EmployeeService;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
@Mock
public class EmployeeServiceMock implements EmployeeService {

    EmployeeService delegate;

    @Override
    public Employee getEmployee(String userId) {
        return delegate.getEmployee(userId);
    }

    @Override
    public List<Employee> getAllActiveEmployees() {
        return delegate.getAllActiveEmployees();
    }

    @Override
    public List<String> updateEmployeesReleaseDate(List<Employee> employees) {
        return delegate.updateEmployeesReleaseDate(employees);
    }

    @Override
    public void updateEmployeeReleaseDate(String userId, String releaseDate) {
        delegate.updateEmployeeReleaseDate(userId, releaseDate);
    }

    public void setDelegate(EmployeeService delegate) {
        this.delegate = delegate;
    }
}
