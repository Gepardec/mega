package com.gepardec.mega.application.security;

import com.gepardec.mega.application.security.exception.UnauthorizedException;

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
    public Object invoke(final InvocationContext ic) throws Exception {
        if (!userContext.loggedIn()) {
            throw new UnauthorizedException("IdToken was invalid");
        }
        return ic.proceed();
    }
}
