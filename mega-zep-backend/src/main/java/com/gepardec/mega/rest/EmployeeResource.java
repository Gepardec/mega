package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.service.api.employee.EmployeeService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
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
