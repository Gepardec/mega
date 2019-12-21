package com.gepardec.mega.security;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Objects;
import java.util.stream.Stream;

@RolesAllowed(allowedRoles = Role.USER)
@Interceptor
public class RolesAllowedInterceptor {
    @Inject
    SessionUser sessionUser;

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {

        RolesAllowed rolesAllowedAnnotation = invocationContext.getMethod().getAnnotation(RolesAllowed.class);
        if (rolesAllowedAnnotation == null) {
            rolesAllowedAnnotation = invocationContext.getTarget().getClass().getAnnotation(RolesAllowed.class);
        }

        Objects.requireNonNull(rolesAllowedAnnotation, "Could not resolve Authorizaion annotation. Do you use Stereotype annotations, which are currently not supported?");

        Role[] allowedRoles = rolesAllowedAnnotation.allowedRoles();
        if (sessionUser.isLoggedIn() && Stream.of(allowedRoles).anyMatch(role -> role.equals(sessionUser.getRole()))) {
            return invocationContext.proceed();
        } else {
            throw new PermissionDeniedException(String.format("User has insufficient role %s", sessionUser.getRole()));
        }
    }
}