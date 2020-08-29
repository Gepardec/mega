package com.gepardec.mega.application.security;

import com.gepardec.mega.application.exception.UnauthorizedException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.Priorities;

@Priority(Priorities.AUTHENTICATION)
@Interceptor
@Secured
public class SecuredInterceptor {

    @Inject
    UserContext userContext;

    @AroundInvoke
    public Object invoke(final InvocationContext invocationContext) throws Exception {
        if (!userContext.isLoggedIn()) {
            throw new UnauthorizedException("IdToken was invalid");
        }
        return invocationContext.proceed();
    }
}
