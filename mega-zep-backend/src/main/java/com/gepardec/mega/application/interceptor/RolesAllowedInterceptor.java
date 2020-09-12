package com.gepardec.mega.application.interceptor;

import com.gepardec.mega.application.exception.ForbiddenException;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.UserContext;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.Priorities;
import java.util.Objects;
import java.util.stream.Stream;

@Priority(Priorities.AUTHENTICATION)
@RolesAllowed(allowedRoles = Role.USER)
@Interceptor
public class RolesAllowedInterceptor {

    @Inject
    UserContext userContext;

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {
        RolesAllowed rolesAllowedAnnotation = invocationContext.getMethod().getAnnotation(RolesAllowed.class);
        if (rolesAllowedAnnotation == null) {
            rolesAllowedAnnotation = invocationContext.getTarget().getClass().getAnnotation(RolesAllowed.class);
        }

        Objects.requireNonNull(rolesAllowedAnnotation, "Could not resolve Authorizaion annotation. Do you use Stereotype annotations, which are currently not supported?");

        Role[] allowedRoles = rolesAllowedAnnotation.allowedRoles();
        if (Stream.of(allowedRoles).anyMatch(role -> role.equals(userContext.user().role()))) {
            return invocationContext.proceed();
        } else {
            throw new ForbiddenException(String.format("User has insufficient role %s", userContext.user().role()));
        }
    }
}