package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.SecurityContext;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@ApplicationScoped
public class SecurityContextProducer {

    static final String X_AUTHORIZATION_HEADER = "X-Authorization";

    @Inject
    HttpServletRequest request;

    @Inject
    GoogleIdTokenVerifier googleIdTokenVerifier;

    @Produces
    @RequestScoped
    SecurityContext createSecurityContext() {
        final GoogleIdToken googleIdToken = verifyAndGoogleIdToken();
        if (googleIdToken != null) {
            return SecurityContext.builder()
                    .email(googleIdToken.getPayload().getEmail())
                    .build();
        } else {
            return SecurityContext.builder().build();
        }
    }

    private GoogleIdToken verifyAndGoogleIdToken() {
        try {
            final String authorizationHeader = authorizationHeaderOrFail();
            return Optional.ofNullable(googleIdTokenVerifier.verify(authorizationHeader))
                    .orElseThrow(() -> new UnauthorizedException("IdToken verifier returned null idToken"));
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }

    private String authorizationHeaderOrFail() {
        return Optional.ofNullable(request.getHeader(X_AUTHORIZATION_HEADER))
                .orElseThrow(() -> new UnauthorizedException("No authorization header '" + X_AUTHORIZATION_HEADER + "' was provided"));
    }

}
