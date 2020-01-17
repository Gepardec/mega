package com.gepardec.mega.aplication.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Collections;

@ApplicationScoped
public class GoogleVerifierProducer {

    @Inject
    @ConfigProperty(name = "google.frontend.clientId")
    String frontendClientId;

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
                .setAudience(Collections.singletonList(frontendClientId))
                .build();
    }
}
