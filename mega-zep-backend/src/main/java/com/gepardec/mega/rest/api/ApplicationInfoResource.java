package com.gepardec.mega.rest.api;

import com.gepardec.mega.rest.model.ApplicationInfoDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/info")
public interface ApplicationInfoResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ApplicationInfoDto get();
}
