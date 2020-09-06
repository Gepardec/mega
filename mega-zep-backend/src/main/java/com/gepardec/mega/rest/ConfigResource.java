package com.gepardec.mega.rest;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.application.configuration.OAuthConfig;
import com.gepardec.mega.rest.model.Config;

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
    OAuthConfig oauthConfig;

    @Inject
    ApplicationConfig applicationConfig;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        final Config config = Config.newBuilder()
                .clientId(oauthConfig.getClientId())
                .issuer(oauthConfig.getIssuer())
                .scope(oauthConfig.getScope())
                .version(applicationConfig.getVersion())
                .build();
        return Response.ok(config).build();
    }
}
