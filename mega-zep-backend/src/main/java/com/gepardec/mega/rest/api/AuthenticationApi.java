package com.gepardec.mega.rest.api;

import com.gepardec.mega.model.google.GoogleUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public interface AuthenticationApi {

    @OPTIONS
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    Response login (@Context HttpServletRequest request, @Context HttpServletResponse response);

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response login (GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @OPTIONS
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    Response logoutPreFlight (@Context HttpServletRequest request, @Context HttpServletResponse response);

    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    Response logout (@Context HttpServletRequest request, @Context HttpServletResponse response);
}
