package com.gepardec.mega.application.security;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.interceptor.InvocationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecuredInterceptorTest {

    @Mock
    private UserContext userContext;

    @Mock
    private InvocationContext invocationContext;

    @InjectMocks
    private SecuredInterceptor securedInterceptor;

    @Test
    void invoke_withNotLogged_throwUnauthorizedException() {
        assertThrows(UnauthorizedException.class, () -> securedInterceptor.invoke(invocationContext));
    }

    @Test
    void invoke_tokenInvalid_throwUnauthorizedException() {
        when(userContext.loggedIn()).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> securedInterceptor.invoke(invocationContext));
    }

    @Test
    void invoke_userLoggedAndTokenValid_callsProceed() throws Exception {
        when(userContext.loggedIn()).thenReturn(true);
        securedInterceptor.invoke(invocationContext);

        verify(invocationContext, times(1)).proceed();
    }
}
