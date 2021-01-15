package com.gepardec.mega.application.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URL;

@ApplicationScoped
public class ZepConfig {

    @Inject
    @ConfigProperty(name = "mega.zep.admin-token")
    String authorizationToken;

    @Inject
    @ConfigProperty(name = "mega.zep.origin")
    URL origin;

    @Inject
    @ConfigProperty(name = "mega.zep.soap-path")
    String soapPath;

    public String getUrlAsString() {
        return String.format("%s%s", origin.toString(), soapPath);
    }

    public String getUrlForFrontend(){
        return origin.toString();
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public URL getOrigin() {
        return origin;
    }
}
