package com.gepardec.mega.rest.impl;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.rest.api.AuthenticationApi;
import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.zep.service.api.AuthenticationService;
import de.provantis.zep.MitarbeiterType;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Objects;

@ApplicationScoped
public class AuthenticationImpl implements AuthenticationApi {

    @Inject
    Logger log;

    @Inject
    SessionUser sessionUser;

    @Inject
    AuthenticationService authenticationService;

    @Override
    public Response login(GoogleUser user) {
        // TODO: Only provide idToken, nothing more is needed
        Objects.requireNonNull(user, "No GoogleUser object provided for login endpoint");
        final String oldEmail = sessionUser.getEmail();
        final MitarbeiterType mt = authenticationService.login(user.getIdToken());

        if (oldEmail == null) {
            log.info("User '{}' logged in", sessionUser.getEmail());
        } else if (!oldEmail.equals(mt.getEmail())) {
            log.info("Logged user '{}' out and logged user '{}' in", sessionUser.getEmail(), oldEmail);
        } else {
            log.info("User '{}' already logged in", sessionUser.getEmail());
        }

        // TODO: Translate the service result to view result object
        return Response.ok(mt).build();
    }

    @Override
    public Response logout(@Context HttpServletRequest request) {
        if (sessionUser.isLoggedIn()) {
            log.info("User '{}' logged out", sessionUser.getEmail());
            request.getSession().invalidate();
        } else {
            log.info("No user session found");
        }
        return Response.ok().build();
    }
}