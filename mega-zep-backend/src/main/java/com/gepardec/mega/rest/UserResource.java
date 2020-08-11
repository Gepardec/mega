package com.gepardec.mega.rest;

import com.gepardec.mega.application.security.Secured;
import com.gepardec.mega.application.security.UserContext;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Secured
@RequestScoped
@Path("/user")
public class UserResource {

    @Inject
    UserContext userContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        return Response.ok(userContext.getUser()).build();
    }
}
