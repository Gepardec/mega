package connector.security;

import connector.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Authorization
@Interceptor
public class AuthorizationInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    @Inject
    SessionUser sessionUser;

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {

        Authorization authorizationAnnotation = invocationContext.getMethod().getAnnotation(Authorization.class);

        if(authorizationAnnotation != null){
            int[] allowedRoles = authorizationAnnotation.allowedRoles();
            for(int allowedRole : allowedRoles){
                if(allowedRole == sessionUser.getRole()){
                    return invocationContext.proceed();
                }
            }

            logInsufficientPermission(invocationContext);

            HttpServletResponse httpServletResponse = getHttpServletResponse(invocationContext);
            if(httpServletResponse != null){
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

            return null;
        } else {
            return invocationContext.proceed();
        }
    }

    private HttpServletResponse getHttpServletResponse(InvocationContext ic){
        for(Object parameter : ic.getParameters()){
            if(parameter instanceof HttpServletResponse){
                return (HttpServletResponse) parameter;
            }
        }

        return null;
    }

    private void logInsufficientPermission(InvocationContext invocationContext){
        String methodName = invocationContext.getMethod().getDeclaringClass().getSimpleName() +
                "." + invocationContext.getMethod().getName();
        LOG.warn("User " + sessionUser.getName() + " has insufficient permissions to call " + methodName);
    }
}
