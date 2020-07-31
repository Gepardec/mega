package com.gepardec.mega.application.security;

import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@RequestScoped
public class UserContext {

    @Inject
    HttpServletRequest request;

    @Inject
    GoogleIdTokenVerifier googleIdTokenVerifier;

    @Inject
    UserService userService;

    private User user;

    @PostConstruct
    public void doPostConstruct() {
        try {
            final GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(request.getHeader("X-Authorization"));
            user = userService.getUser(googleIdToken.getPayload().getEmail(),
                    Optional.ofNullable(googleIdToken.getPayload().get("picture")).orElse(StringUtils.EMPTY).toString());
        } catch (GeneralSecurityException | IOException e) {
            user = null;
        }
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public User getUser() {
        return user;
    }
}
