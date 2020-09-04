package com.gepardec.mega.application.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@ApplicationScoped
public class OAuthConfig {

    @Inject
    @ConfigProperty(name = "oauth.clientId")
    String clientId;

    @Inject
    @ConfigProperty(name = "oauth.issuer")
    String issuer;

    @Inject
    @ConfigProperty(name = "oauth.scope")
    String scope;

    public String getClientId() {
        return clientId;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getScope() {
        return scope;
    }
}
