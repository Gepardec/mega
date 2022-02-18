package com.gepardec.mega.rest;

import com.gepardec.mega.domain.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
public interface UserResourceAPI {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    User get();
}
