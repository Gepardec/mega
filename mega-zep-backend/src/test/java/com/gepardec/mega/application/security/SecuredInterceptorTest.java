package com.gepardec.mega.application.security;

import com.gepardec.mega.aplication.security.SecuredInterceptor;
import com.gepardec.mega.aplication.security.SessionUser;
import com.gepardec.mega.aplication.security.UnauthorizedException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
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
    private SessionUser sessionUser;

    @Mock
    private InvocationContext invocationContext;

    @Mock
    private GoogleIdTokenVerifier tokenVerifier;

    @Mock
    private GoogleIdToken idToken;

    @InjectMocks
    private SecuredInterceptor securedInterceptor;

    @Test
    void invoke_withNotLogged_throwUnauthorizedException() {
        assertThrows(UnauthorizedException.class, () -> securedInterceptor.invoke(invocationContext));
    }

    @Test
    void invoke_tokenInvalid_throwUnauthorizedException() {
        when(sessionUser.isLogged()).thenReturn(true);
        assertThrows(UnauthorizedException.class, () -> securedInterceptor.invoke(invocationContext));
    }

    @Test
    void invoke_userLoggedAndTokenValid_callsProceed() throws Exception {
        when(sessionUser.isLogged()).thenReturn(true);
        when(sessionUser.getIdToken()).thenReturn("idToken");
        when(tokenVerifier.verify(anyString())).thenReturn(idToken);
        securedInterceptor.invoke(invocationContext);

        verify(invocationContext, times(1)).proceed();
    }
}
