package com.gepardec.mega.security;

import com.gepardec.mega.annotations.Authorization;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Authorization
@Interceptor
public class AuthorizationInterceptor {

    @Inject
    Logger logger;

    @Inject
    SessionUser sessionUser;

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {

        final Authorization authorizationAnnotation = invocationContext.getMethod().getAnnotation(Authorization.class);

        if(authorizationAnnotation != null){
            int[] allowedRoles = authorizationAnnotation.allowedRoles();
            for(int allowedRole : allowedRoles){
                if(allowedRole == sessionUser.getRole()){
                    return invocationContext.proceed();
                }
            }

            logInsufficientPermission(invocationContext);

            final HttpServletResponse httpServletResponse = getHttpServletResponse(invocationContext);
            if(httpServletResponse != null){
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

            return null;
        } else {
            return invocationContext.proceed();
        }
    }

    private HttpServletResponse getHttpServletResponse(InvocationContext ic){
        return Arrays.stream(ic.getParameters()).filter(HttpServletResponse.class::isInstance).map(HttpServletResponse.class::cast).findFirst().orElse(null);
    }

    private void logInsufficientPermission(InvocationContext invocationContext){
        final String methodName = invocationContext.getMethod().getDeclaringClass().getSimpleName() +
                "." + invocationContext.getMethod().getName();
        logger.warn("User {} has insufficient permissions to call {}", sessionUser.getName(), methodName);
    }
}
