package com.gepardec.mega.rest;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.application.configuration.OAuthConfig;
import com.gepardec.mega.application.configuration.ZepConfig;
import com.gepardec.mega.rest.model.Config;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Provides configuration for the frontend.
 */
@Path("/config")
public class ConfigResource {

    @Inject
    OAuthConfig oauthConfig;

    @Inject
    ApplicationConfig applicationConfig;

    @Inject
    ZepConfig zepConfig;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Config get() {
        return Config.newBuilder()
                .excelUrl(applicationConfig.getExcelUrlAsString())
                .zepUrl(zepConfig.getUrlAsString())
                .clientId(oauthConfig.getClientId())
                .issuer(oauthConfig.getIssuer())
                .scope(oauthConfig.getScope())
                .version(applicationConfig.getVersion())
                .build();
    }
}
