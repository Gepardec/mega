package com.gepardec.mega.application.interceptor;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.SecurityContext;
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
    private SecurityContext securityContext;

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
        when(securityContext.email()).thenReturn(null);
        assertThrows(UnauthorizedException.class, () -> securedInterceptor.invoke(invocationContext));
    }

    @Test
    void invoke_userLoggedAndTokenValid_callsProceed() throws Exception {
        when(securityContext.email()).thenReturn("test@gepardec.com");
        securedInterceptor.invoke(invocationContext);

        verify(invocationContext, times(1)).proceed();
    }
}
