package com.gepardec.mega.application.security;

import com.gepardec.mega.application.configuration.OAuthConfig;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class GoogleVerifierProducer {

    @Inject
    OAuthConfig oauthConfig;

    @Produces
    @Dependent
    public HttpTransport createHttpTransport() {
        return new NetHttpTransport();
    }

    @Produces
    @Dependent
    public JsonFactory createJsonFactory() {
        return new JacksonFactory();
    }

    @Produces
    @Dependent
    public GoogleIdTokenVerifier createGoogleTokenVerifier(final HttpTransport httpTransport, final JsonFactory jsonFactory) {
        return new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(List.of(oauthConfig.getClientId()))
                .setIssuer(oauthConfig.getIssuer())
                .build();
    }
}
