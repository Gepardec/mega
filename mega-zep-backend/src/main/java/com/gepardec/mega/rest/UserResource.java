package com.gepardec.mega.rest;

import com.gepardec.mega.domain.User;
import com.gepardec.mega.application.security.SessionUser;
import com.gepardec.mega.service.api.UserService;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Handles the User actions.
 */
@RequestScoped
@Path("/user")
public class UserResource {

    @Inject
    Logger log;

    @Inject
    SessionUser sessionUser;

    @Inject
    UserService userService;

    @Context
    HttpServletRequest request;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response login(@NotEmpty(message = "{authenticationResource.google.idToken.notEmpty}") String idToken) {
        log.info("idToken: {}", idToken);
        final String oldEmail = sessionUser.getEmail();
        final User user = userService.login(idToken);

        if (oldEmail == null) {
            log.info("User '{}' logged in", sessionUser.getEmail());
        } else if (!oldEmail.equals(user.getEmail())) {
            log.info("Logged user '{}' out and logged user '{}' in", sessionUser.getEmail(), oldEmail);
        } else {
            log.info("User '{}' already logged in", sessionUser.getEmail());
        }

        return Response.ok(user).build();
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout() {
        if (sessionUser.isLogged()) {
            log.info("User '{}' logged out", sessionUser.getEmail());
            request.getSession().invalidate();
        } else {
            log.info("No user session found");
        }
        return Response.ok().build();
    }
}