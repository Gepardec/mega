package com.gepardec.mega.rest.api;

import com.gepardec.mega.rest.model.ProjectEntryDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/projectentry")
public interface ProjectEntryResource {
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response update(ProjectEntryDto projectEntryDTO);
}
