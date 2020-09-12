package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Secured
@RequestScoped
@Path("/user")
public class UserResource {

    @Inject
    UserContext userContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User get() {
        return userContext.user();
    }
}
