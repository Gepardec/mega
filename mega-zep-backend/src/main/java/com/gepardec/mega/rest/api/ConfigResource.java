package com.gepardec.mega.rest;

import com.gepardec.mega.rest.model.Config;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/config")
public interface ConfigResourceAPI {
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Config get();
}
