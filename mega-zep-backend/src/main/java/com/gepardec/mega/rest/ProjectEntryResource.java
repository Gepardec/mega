package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.rest.model.ProjectEntryDTO;
import com.gepardec.mega.service.api.projectentry.ProjectEntryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@Secured
public class ProjectEntryResource implements ProjectEntryResourceAPI {

    @Inject
    ProjectEntryService projectEntryService;

    @Override
    public boolean update(final ProjectEntryDTO projectEntryDTO) {
        return projectEntryService.update(projectEntryDTO);
    }
}
