package com.gepardec.mega.rest;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.application.security.RolesAllowed;
import com.gepardec.mega.application.security.Secured;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.domain.model.employee.Employee;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Secured
@Path("/employees")
public class EmployeeResource {

    @Inject
    EmployeeService employeeService;

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployees() {
        return Response.ok(employeeService.getAllActiveEmployees()).build();
    }

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setEmployees(@NotEmpty(message = "{workerResource.employees.notEmpty}") final List<Employee> employees) {
        return Response.ok().entity(employeeService.updateEmployeesReleaseDate(employees)).build();
    }
}
