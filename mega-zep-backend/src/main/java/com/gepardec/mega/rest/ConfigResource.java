package com.gepardec.mega.rest;

import com.gepardec.mega.aplication.configuration.GoogleConfig;
import com.gepardec.mega.rest.model.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Provides configuration for the frontend.
 */
@Path("/config")
public class ConfigResource {

    @Inject
    GoogleConfig googleConfig;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return Response.ok(new Config(googleConfig.getFrontendClientId(), googleConfig.getIssuer(), googleConfig.getScope())).build();
    }
}
