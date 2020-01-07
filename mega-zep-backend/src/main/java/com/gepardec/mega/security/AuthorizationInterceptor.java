package com.gepardec.mega.security;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Authorization(allowedRoles = Role.USER)
@Interceptor
public class AuthorizationInterceptor {
    @Inject
    SessionUser sessionUser;

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {

        Authorization authorizationAnnotation = invocationContext.getMethod().getAnnotation(Authorization.class);
        if (authorizationAnnotation == null) {
            authorizationAnnotation = invocationContext.getTarget().getClass().getAnnotation(Authorization.class);
        }

        Objects.requireNonNull(authorizationAnnotation, "Could not resolve Authorization annotation. Do you use Stereotype annotations, which are currently not supported?");

        Role[] allowedRoles = authorizationAnnotation.allowedRoles();
        List<Role> userRoles = Role.getCoherentRolesByValue(sessionUser.getRole());
        if (sessionUser.isLoggedIn() && Stream.of(allowedRoles)
                .anyMatch(role -> userRoles.contains(role))) {
            return invocationContext.proceed();
        } else {
            throw new SecurityException(String.format("user has insufficient role %s", sessionUser.getRole()));
        }
    }
}