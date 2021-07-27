package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.service.api.employee.EmployeeService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
@Secured
// PL, OM
@RolesAllowed({Role.PROJECT_LEAD, Role.OFFICE_MANAGEMENT})
public class EmployeeResource implements EmployeeResourceAPI {

    @Inject
    EmployeeService employeeService;

    @Override
    public List<Employee> list() {
        return employeeService.getAllActiveEmployees();
    }

    @Override
    public List<String> update(final List<Employee> employees) {
        return employeeService.updateEmployeesReleaseDate(employees);
    }

}
