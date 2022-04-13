package com.gepardec.mega.rest.impl;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.rest.api.UserResource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@RequestScoped
@RolesAllowed(Role.EMPLOYEE)
@Secured
public class UserResourceImpl implements UserResource {

    @Inject
    UserContext userContext;

    @Override
    public Response get() {
        return Response.ok(userContext.getUser()).build();
    }
}
