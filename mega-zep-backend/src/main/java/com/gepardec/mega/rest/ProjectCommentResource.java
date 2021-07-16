package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.rest.model.ProjectCommentDto;
import com.gepardec.mega.service.api.project.ProjectCommentService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Secured
@RequestScoped
@Path("/projectcomments")
public class ProjectCommentResource {

    @Inject
    ProjectCommentService projectCommentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectCommentDto getProjectComment(
            @QueryParam("date") @NotNull(message = "{projectCommentResource.date.notNull}") String currentMonthYear,
            @QueryParam("projectName") @NotNull(message = "{projectCommentResource.projectName.notNull}") String projectName
    ) {
        return projectCommentService.findProjectCommentForProjectNameWithCurrentYearMonth(projectName, currentMonthYear);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectCommentDto createProjectComment(@NotNull(message = "{projectCommentResource.projectCommentEntry.notNull}") ProjectCommentDto newProjectCommentDto) {
        return projectCommentService.createProjectComment(newProjectCommentDto);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateProjectComment(@NotNull(message = "{projectCommentResource.projectCommentEntry.notNull}") ProjectCommentDto projectCommentDto) {
        return projectCommentService.updateProjectComment(projectCommentDto.getId(), projectCommentDto.getComment());
    }
}
