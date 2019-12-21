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
        // TODO: Only provide email and idToken, nothing more is needed
        Objects.requireNonNull(user, "No GoogleUser object provided for login endpoint");
        final MitarbeiterType mt = authenticationService.login(user.getEmail(), user.getIdToken());
        // TODO: Translate the service result to view result object
        return Response.ok(mt).build();
    }

    @Override
    public Response logout(@Context HttpServletRequest request) {
        if (sessionUser.isLoggedIn()) {
            log.info("User '{}' logged out", sessionUser.getEmail());
            request.getSession().invalidate();
        }
        return Response.ok().build();
    }
}
