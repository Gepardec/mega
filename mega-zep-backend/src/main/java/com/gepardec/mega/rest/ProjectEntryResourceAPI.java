package com.gepardec.mega.rest;

import com.gepardec.mega.rest.model.ProjectEntryDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/projectentry")
public interface ProjectEntryResourceAPI {
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    boolean update(ProjectEntryDTO projectEntryDTO);
}
