package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.rest.model.ProjectCommentDto;
import com.gepardec.mega.service.api.project.ProjectCommentService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@Secured
public class ProjectCommentResource implements ProjectCommentResourceAPI {

    @Inject
    ProjectCommentService projectCommentService;

    @Override
    public ProjectCommentDto get(
            String currentMonthYear,
            String projectName
    ) {
        return projectCommentService.findForProjectNameWithCurrentYearMonth(projectName, currentMonthYear);
    }

    @Override
    public ProjectCommentDto create(ProjectCommentDto newProjectCommentDto) {
        return projectCommentService.create(newProjectCommentDto);
    }

    @Override
    public boolean update(ProjectCommentDto projectCommentDto) {
        return projectCommentService.update(projectCommentDto.getId(), projectCommentDto.getComment());
    }
}
