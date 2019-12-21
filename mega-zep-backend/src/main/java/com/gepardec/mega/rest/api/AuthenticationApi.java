package com.gepardec.mega.rest.api;

import com.gepardec.mega.model.google.GoogleUser;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public interface AuthenticationApi {
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response login(GoogleUser user);

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    Response logout(@Context HttpServletRequest request);
}
