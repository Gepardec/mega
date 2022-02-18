package com.gepardec.mega.rest.api;

import com.gepardec.mega.domain.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
public interface UserResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    User get();
}
