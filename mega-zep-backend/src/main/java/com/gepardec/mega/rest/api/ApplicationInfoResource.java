package com.gepardec.mega.rest;

import com.gepardec.mega.rest.model.ApplicationInfo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/info")
public interface ApplicationInfoResourceAPI {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ApplicationInfo get();
}
