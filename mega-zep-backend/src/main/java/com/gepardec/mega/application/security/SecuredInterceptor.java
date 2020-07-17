package com.gepardec.mega.application.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

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
    GoogleIdTokenVerifier tokenVerifier;

    @Inject
    SessionUser sessionUser;

    @AroundInvoke
    public Object invoke(final InvocationContext ic) throws Exception {
        if (!sessionUser.isLogged()) {
            throw new UnauthorizedException("Anonymous user tried to access a secured resource");
        }

        if (tokenVerifier.verify(sessionUser.getIdToken()) == null) {
            throw new UnauthorizedException("IdToken was invalid");
        }

        return ic.proceed();
    }
}
