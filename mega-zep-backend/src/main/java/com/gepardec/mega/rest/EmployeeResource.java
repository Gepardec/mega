package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.service.api.employee.EmployeeService;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Secured
// PL, OM
@RolesAllowed({Role.PROJECT_LEAD, Role.OFFICE_MANAGEMENT})
@Path("/employees")
public class EmployeeResource {

    @Inject
    EmployeeService employeeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> list() {
        return employeeService.getAllActiveEmployees();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<String> update(@NotEmpty(message = "{workerResource.employees.notEmpty}") final List<Employee> employees) {
        return employeeService.updateEmployeesReleaseDate(employees);
    }

}
