package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.rest.model.EnterpriseEntryDto;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/enterprise")
public interface EnterpriseResourceAPI {
    @GET
    @Path("/entriesformonthyear/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    EnterpriseEntryDto getEnterpriseEntryForMonthYear(@PathParam("year") Integer year, @PathParam("month") Integer month);

    @PUT
    @Path("/entry/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    boolean updateEnterpriseEntry(@PathParam("year") Integer year, @PathParam("month") Integer month, @RequestBody EnterpriseEntryDto entryDto);
}
