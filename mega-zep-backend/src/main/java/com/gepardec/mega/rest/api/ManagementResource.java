package com.gepardec.mega.rest.api;

import com.gepardec.mega.rest.model.ManagementEntryDto;
import com.gepardec.mega.rest.model.ProjectManagementEntryDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/management")
public interface ManagementResource {
    @GET
    @Path("/officemanagemententries/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    List<ManagementEntryDto> getAllOfficeManagementEntries(@PathParam("year") Integer year, @PathParam("month") Integer month);

    @GET
    @Path("/projectmanagemententries/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    List<ProjectManagementEntryDto> getAllProjectManagementEntries(@PathParam("year") Integer year, @PathParam("month") Integer month, @QueryParam("all") boolean allProjects);
}
