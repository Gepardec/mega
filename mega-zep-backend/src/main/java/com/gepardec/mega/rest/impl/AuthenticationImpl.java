package com.gepardec.mega.rest.impl;

import com.gepardec.mega.rest.Employee;
import com.gepardec.mega.rest.EmployeeTranslator;
import com.gepardec.mega.rest.api.AuthenticationApi;
import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.zep.service.api.AuthenticationService;
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
    public Response login(String idToken) {
        Objects.requireNonNull(idToken, "No GoogleUser object provided for login endpoint");
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

    @Override
    public Response logout(@Context HttpServletRequest request) {
        if (sessionUser.isLogged()) {
            log.info("User '{}' logged out", sessionUser.getEmail());
            request.getSession().invalidate();
        } else {
            log.info("No user session found");
        }
        return Response.ok().build();
    }
}