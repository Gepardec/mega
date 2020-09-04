package com.gepardec.mega.application.producer;

import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.domain.model.User;
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
            final GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(request.getHeader("X-Authorization"));
            return userService.getUser(googleIdToken.getPayload().getEmail(),
                    Optional.ofNullable(googleIdToken.getPayload().get("picture")).orElse(StringUtils.EMPTY).toString());
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }
}
