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
    @ConfigProperty(name = "mega.zep.url")
    URL url;

    public String getUrlAsString() {
        return url.toString();
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public URL getUrl() {
        return url;
    }
}
