package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/employees")
public interface EmployeeResourceAPI {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Employee> list();

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<String> update(@NotEmpty(message = "{workerResource.employees.notEmpty}") List<Employee> employees);
}
