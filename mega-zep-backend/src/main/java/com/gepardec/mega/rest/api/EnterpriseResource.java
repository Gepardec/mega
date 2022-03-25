package com.gepardec.mega.rest.api;

import com.gepardec.mega.rest.model.EnterpriseEntryDto;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/enterprise")
public interface EnterpriseResource {
    @GET
    @Path("/entriesformonthyear/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getEnterpriseEntryForMonthYear(@PathParam("year") Integer year, @PathParam("month") Integer month);

    @PUT
    @Path("/entry/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateEnterpriseEntry(@PathParam("year") Integer year, @PathParam("month") Integer month, @RequestBody EnterpriseEntryDto entryDto);
}
