package com.gepardec.mega.security;

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

    @Mock
    private SessionUser sessionUser;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Object target;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InvocationContext invocationContext;

    @InjectMocks
    private RolesAllowedInterceptor rolesAllowedInterceptor;

    @Test
    void intercept_notLogged_throwsForbiddenException() throws Exception {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(createAnnotation(new Role[]{Role.USER}));
        when(sessionUser.isLogged()).thenReturn(false);

        assertThrows(ForbiddenException.class, () -> rolesAllowedInterceptor.intercept(invocationContext));
    }

    @Test
    void invoke_loggedAndNotInRole_throwsForbiddenException() throws Exception {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(createAnnotation(new Role[]{Role.ADMINISTRATOR}));
        when(sessionUser.isLogged()).thenReturn(true);
        when(sessionUser.getRole()).thenReturn(Role.USER);

        assertThrows(ForbiddenException.class, () -> rolesAllowedInterceptor.intercept(invocationContext));
    }

    @Test
    void invoke_loggedAndInRoleMethodAnnotated_throwsForbiddenException() throws Exception {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(createAnnotation(Role.values()));
        when(sessionUser.isLogged()).thenReturn(true);
        when(sessionUser.getRole()).thenReturn(Role.USER);

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
