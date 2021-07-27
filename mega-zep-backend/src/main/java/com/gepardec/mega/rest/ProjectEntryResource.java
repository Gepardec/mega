package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.rest.model.ProjectEntryDTO;
import com.gepardec.mega.service.api.projectentry.ProjectEntryService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class ProjectEntryResource implements ProjectEntryResourceAPI {

    @Inject
    ProjectEntryService projectEntryService;

    @Override
    public boolean update(final ProjectEntryDTO projectEntryDTO) {
        return projectEntryService.update(projectEntryDTO);
    }
}
