package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.service.api.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@ApplicationScoped
public class UserContextProducer {

    static final String X_AUTHORIZATION_HEADER = "X-Authorization";
    @Inject
    HttpServletRequest request;

    @Inject
    GoogleIdTokenVerifier googleIdTokenVerifier;

    @Inject
    UserService userService;

    @Produces
    @RequestScoped
    UserContext createUserContext() {
        final User user = verifyAndLoadUser();
        return UserContext.builder()
                .user(user)
                .loggedIn(user != null)
                .build();
    }

    private User verifyAndLoadUser() {
        try {
            final String authorizationHeader = authorizationHeaderOrFail();
            final GoogleIdToken googleIdToken = Optional.ofNullable(googleIdTokenVerifier.verify(authorizationHeader))
                    .orElseThrow(() -> new UnauthorizedException("IdToken verifier returned null idToken"));
            return userService.getUser(googleIdToken.getPayload().getEmail(),
                    Optional.ofNullable(googleIdToken.getPayload().get("picture")).orElse(StringUtils.EMPTY).toString());
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }

    private String authorizationHeaderOrFail() {
        return Optional.ofNullable(request.getHeader(X_AUTHORIZATION_HEADER))
                .orElseThrow(() -> new UnauthorizedException("No authorization header '" + X_AUTHORIZATION_HEADER + "' was provided"));
    }
}
