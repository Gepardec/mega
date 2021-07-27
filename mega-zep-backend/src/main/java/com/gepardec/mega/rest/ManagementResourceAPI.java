package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.rest.model.ManagementEntry;
import com.gepardec.mega.rest.model.ProjectManagementEntry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Secured
@RolesAllowed(value = {Role.PROJECT_LEAD, Role.OFFICE_MANAGEMENT})
@Path("/management")
public interface ManagementResourceAPI {
    @GET
    @Path("/officemanagemententries/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    List<ManagementEntry> getAllOfficeManagementEntries(@PathParam("year") Integer year, @PathParam("month") Integer month);

    @GET
    @Path("/projectmanagemententries/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    List<ProjectManagementEntry> getAllProjectManagementEntries(@PathParam("year") Integer year, @PathParam("month") Integer month);
}
