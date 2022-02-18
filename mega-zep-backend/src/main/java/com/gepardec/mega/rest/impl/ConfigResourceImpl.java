package com.gepardec.mega.rest.impl;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.application.configuration.OAuthConfig;
import com.gepardec.mega.application.configuration.ZepConfig;
import com.gepardec.mega.rest.api.ConfigResource;
import com.gepardec.mega.rest.model.ConfigDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * Provides configuration for the frontend.
 */
@RequestScoped
public class ConfigResourceImpl implements ConfigResource {

    @Inject
    OAuthConfig oauthConfig;

    @Inject
    ApplicationConfig applicationConfig;

    @Inject
    ZepConfig zepConfig;

    @Override
    public Response get() {
        final ConfigDto configDto = ConfigDto.builder()
                .excelUrl(applicationConfig.getExcelUrlAsString())
                .budgetCalculationExcelUrl(applicationConfig.getBudgetCalculationExcelUrlAsString())
                .zepOrigin(zepConfig.getUrlForFrontend())
                .clientId(oauthConfig.getClientId())
                .issuer(oauthConfig.getIssuer())
                .scope(oauthConfig.getScope())
                .version(applicationConfig.getVersion())
                .build();

        return Response.ok(configDto).build();
    }
}
