package com.gepardec.mega.aplication.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class GoogleConfig {

    @Inject
    @ConfigProperty(name = "google.frontend.clientId")
    String frontendClientId;

    @Inject
    @ConfigProperty(name = "google.oauth.issuer")
    String issuer;

    @Inject
    @ConfigProperty(name = "google.oauth.scope")
    String scope;

    public String getFrontendClientId() {
        return frontendClientId;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getScope() {
        return scope;
    }
}
