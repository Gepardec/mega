package com.gepardec.mega.rest.api;

import com.gepardec.mega.rest.model.EmployeeStepDto;
import com.gepardec.mega.rest.model.ProjectStepDto;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/stepentry")
public interface StepEntryResource {
    @PUT
    @Path("/close")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response close(@NotNull(message = "{stepEntryResource.parameter.notNull}") EmployeeStepDto employeeStepDto);

    @PUT
    @Path("/closeforoffice")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response closeForOffice(@NotNull(message = "{stepEntryResource.parameter.notNull}") EmployeeStepDto employeeStepDto);

    @PUT
    @Path("/closeforproject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response close(@NotNull(message = "{stepEntryResource.parameter.notNull}") ProjectStepDto projectStepDto);
}
