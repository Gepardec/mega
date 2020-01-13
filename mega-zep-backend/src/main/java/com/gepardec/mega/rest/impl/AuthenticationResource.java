package com.gepardec.mega.rest.impl;

import com.gepardec.mega.rest.Employee;
import com.gepardec.mega.rest.EmployeeTranslator;
import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.zep.service.api.AuthenticationService;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
public class AuthenticationResource {

    @Inject
    Logger log;

    @Inject
    SessionUser sessionUser;

    @Inject
    AuthenticationService authenticationService;

    @Context
    HttpServletRequest request;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@NotNull(message = "{authenticationResource.google.idToken.notNull}") String idToken) {
        final Employee employee = EmployeeTranslator.toEmployee(authenticationService.login(idToken));
        final String oldEmail = sessionUser.getEmail();

        if (oldEmail == null) {
            log.info("User '{}' logged in", sessionUser.getEmail());
        } else if (!oldEmail.equals(employee.getEMail())) {
            log.info("Logged user '{}' out and logged user '{}' in", sessionUser.getEmail(), oldEmail);
        } else {
            log.info("User '{}' already logged in", sessionUser.getEmail());
        }

        return Response.ok(employee).build();
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