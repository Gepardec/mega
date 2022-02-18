package com.gepardec.mega.rest;

import com.gepardec.mega.domain.model.Employee;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
