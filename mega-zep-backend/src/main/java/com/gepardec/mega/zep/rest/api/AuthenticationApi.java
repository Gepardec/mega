package com.gepardec.mega.zep.rest.api;

import com.gepardec.mega.model.google.GoogleUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public interface AuthenticationApi {
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response login (GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response);

    @POST
    @Path("/logout")
    Response logout(@Context HttpServletRequest request, @Context HttpServletResponse response);
}
