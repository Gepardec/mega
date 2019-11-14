package com.gepardec.mega.rest.impl;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.rest.api.AuthenticationApi;
import com.gepardec.mega.zep.service.api.AuthenticationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class AuthenticationImpl implements AuthenticationApi {

    @Inject
    AuthenticationService authenticationService;

    @Override
    public Response login (final HttpServletRequest request, final HttpServletResponse response) {
        return Response.ok().build();
    }

    @Override
    public Response login (GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        return authenticationService.login(user, request);
    }

    @Override
    public Response logoutPreFlight (final HttpServletRequest request, final HttpServletResponse response) {
        return Response.ok().build();
    }

    @Override
    public Response logout (@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return authenticationService.logout(request);
    }
}
