package com.gepardec.mega.rest.api;

import com.gepardec.mega.rest.model.ProjectCommentDto;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/projectcomments")
public interface ProjectCommentResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response get(
            @QueryParam("date") @NotNull(message = "{projectCommentResource.date.notNull}") String currentMonthYear,
            @QueryParam("projectName") @NotNull(message = "{projectCommentResource.projectName.notNull}") String projectName
    );

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response create(@NotNull(message = "{projectCommentResource.projectCommentEntry.notNull}") ProjectCommentDto newProjectCommentDto);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response update(@NotNull(message = "{projectCommentResource.projectCommentEntry.notNull}") ProjectCommentDto projectCommentDto);
}
