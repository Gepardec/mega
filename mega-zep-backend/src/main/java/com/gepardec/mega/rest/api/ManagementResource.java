package com.gepardec.mega.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/management")
public interface ManagementResource {
    @GET
    @Path("/officemanagemententries/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllOfficeManagementEntries(@PathParam("year") Integer year, @PathParam("month") Integer month);

    @GET
    @Path("/projectmanagemententries/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllProjectManagementEntries(@PathParam("year") Integer year, @PathParam("month") Integer month, @QueryParam("all") boolean allProjects);
}
