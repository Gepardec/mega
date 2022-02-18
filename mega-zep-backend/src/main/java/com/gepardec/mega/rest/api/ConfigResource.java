package com.gepardec.mega.rest.api;

import com.gepardec.mega.rest.model.ConfigDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/config")
public interface ConfigResource {
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ConfigDto get();
}
