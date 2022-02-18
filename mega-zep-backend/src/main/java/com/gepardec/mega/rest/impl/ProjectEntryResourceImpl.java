package com.gepardec.mega.rest.impl;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.rest.api.ProjectEntryResource;
import com.gepardec.mega.rest.model.ProjectEntryDto;
import com.gepardec.mega.service.api.ProjectEntryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@Secured
public class ProjectEntryResourceImpl implements ProjectEntryResource {

    @Inject
    ProjectEntryService projectEntryService;

    @Override
    public boolean update(final ProjectEntryDto projectEntryDTO) {
        return projectEntryService.update(projectEntryDTO);
    }
}
