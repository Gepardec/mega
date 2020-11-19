package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.domain.model.*;
import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.rest.model.ManagementEntry;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Secured
@RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
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
