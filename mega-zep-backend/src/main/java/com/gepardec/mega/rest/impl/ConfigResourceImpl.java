package com.gepardec.mega.rest;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.application.configuration.OAuthConfig;
import com.gepardec.mega.application.configuration.ZepConfig;
import com.gepardec.mega.rest.model.Config;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Provides configuration for the frontend.
 */
@RequestScoped
public class ConfigResource implements ConfigResourceAPI {

    @Inject
    OAuthConfig oauthConfig;

    @Inject
    ApplicationConfig applicationConfig;

    @Inject
    ZepConfig zepConfig;

    @Override
    public Config get() {
        return Config.builder()
                .excelUrl(applicationConfig.getExcelUrlAsString())
                .budgetCalculationExcelUrl(applicationConfig.getBudgetCalculationExcelUrlAsString())
                .zepOrigin(zepConfig.getUrlForFrontend())
                .clientId(oauthConfig.getClientId())
                .issuer(oauthConfig.getIssuer())
                .scope(oauthConfig.getScope())
                .version(applicationConfig.getVersion())
                .build();
    }
}
