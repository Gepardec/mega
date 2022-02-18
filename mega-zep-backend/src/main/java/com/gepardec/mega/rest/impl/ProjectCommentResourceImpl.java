package com.gepardec.mega.rest.impl;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.rest.api.ProjectCommentResource;
import com.gepardec.mega.rest.model.ProjectCommentDto;
import com.gepardec.mega.service.api.ProjectCommentService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@RequestScoped
@Secured
public class ProjectCommentResourceImpl implements ProjectCommentResource {

    @Inject
    ProjectCommentService projectCommentService;

    @Override
    public Response get(
            String currentMonthYear,
            String projectName
    ) {
        return Response.ok(projectCommentService.findForProjectNameWithCurrentYearMonth(projectName, currentMonthYear)).build();
    }

    @Override
    public Response create(ProjectCommentDto newProjectCommentDto) {
        return Response.ok(projectCommentService.create(newProjectCommentDto)).build();
    }

    @Override
    public Response update(ProjectCommentDto projectCommentDto) {
        return Response.ok(projectCommentService.update(projectCommentDto.getId(), projectCommentDto.getComment())).build();
    }
}
