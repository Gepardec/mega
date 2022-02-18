package com.gepardec.mega.rest;

import com.gepardec.mega.rest.model.EmployeeStep;
import com.gepardec.mega.rest.model.ProjectStep;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/stepentry")
public interface StepEntryResourceAPI {
    @PUT
    @Path("/close")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    boolean close(@NotNull(message = "{stepEntryResource.parameter.notNull}") EmployeeStep employeeStep);

    @PUT
    @Path("/closeforoffice")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    boolean closeForOffice(@NotNull(message = "{stepEntryResource.parameter.notNull}") EmployeeStep employeeStep);

    @PUT
    @Path("/closeforproject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    boolean close(@NotNull(message = "{stepEntryResource.parameter.notNull}") ProjectStep projectStep);
}
