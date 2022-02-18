package com.gepardec.mega.rest.impl;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.rest.api.UserResource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@RolesAllowed(Role.EMPLOYEE)
@Secured
public class UserResourceImpl implements UserResource {

    @Inject
    UserContext userContext;

    @Override
    public User get() {
        return userContext.user();
    }
}
