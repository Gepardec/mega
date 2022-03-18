package com.gepardec.mega.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/worker")
public interface WorkerResource {
    @GET
    @Path("/monthendreports")
    @Produces(MediaType.APPLICATION_JSON)
    Response monthlyReport();

    @GET
    @Path("/monthendreports/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    Response monthlyReport(@PathParam("year") Integer year, @PathParam("month") Integer month);
}
