package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.rest.model.ProjectCommentDto;

import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/projectcomments")
public interface ProjectCommentResourceAPI {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ProjectCommentDto get(
            @QueryParam("date") @NotNull(message = "{projectCommentResource.date.notNull}") String currentMonthYear,
            @QueryParam("projectName") @NotNull(message = "{projectCommentResource.projectName.notNull}") String projectName
    );

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    ProjectCommentDto create(@NotNull(message = "{projectCommentResource.projectCommentEntry.notNull}") ProjectCommentDto newProjectCommentDto);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    boolean update(@NotNull(message = "{projectCommentResource.projectCommentEntry.notNull}") ProjectCommentDto projectCommentDto);
}
