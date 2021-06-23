package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.rest.model.ProjectEntryDTO;
import com.gepardec.mega.service.api.projectentry.ProjectEntryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Secured
@RequestScoped
@Path("/projectentry")
public class ProjectEntryResource {

    @Inject
    ProjectEntryService projectEntryService;

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean update(final ProjectEntryDTO projectEntryDTO) {
        return projectEntryService.update(projectEntryDTO);
    }
}
