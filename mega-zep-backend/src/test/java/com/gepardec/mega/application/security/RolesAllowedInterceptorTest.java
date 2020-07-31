package com.gepardec.mega.application.security;

import com.gepardec.mega.application.security.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesAllowedInterceptorTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private UserContext userContext;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InvocationContext invocationContext;

    @InjectMocks
    private RolesAllowedInterceptor rolesAllowedInterceptor;

    @Test
    void intercept_notLogged_throwsForbiddenException() {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(createAnnotation(new Role[]{Role.USER}));

        assertThrows(ForbiddenException.class, () -> rolesAllowedInterceptor.intercept(invocationContext));
    }

    @Test
    void invoke_loggedAndNotInRole_throwsForbiddenException() {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(createAnnotation(new Role[]{Role.ADMINISTRATOR}));
        when(userContext.getUser().role()).thenReturn(Role.USER);

        assertThrows(ForbiddenException.class, () -> rolesAllowedInterceptor.intercept(invocationContext));
    }

    @Test
    void invoke_loggedAndInRoleMethodAnnotated_throwsForbiddenException() throws Exception {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(createAnnotation(Role.values()));
        when(userContext.getUser().role()).thenReturn(Role.USER);

        rolesAllowedInterceptor.intercept(invocationContext);

        verify(invocationContext, times(1)).proceed();
    }

    private RolesAllowed createAnnotation(final Role[] roles) {
        return new RolesAllowed() {
            @Override
            public Role[] allowedRoles() {
                return roles;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return RolesAllowed.class;
            }
        };
    }
}
