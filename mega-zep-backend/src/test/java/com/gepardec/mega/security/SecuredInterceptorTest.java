package com.gepardec.mega.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.interceptor.InvocationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Disabled
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
    void invoke_userLoggedAndTokenValid_throwUnauthorizedException() throws Exception {
        when(sessionUser.isLogged()).thenReturn(true);
        when(tokenVerifier.verify(anyString())).thenReturn(idToken);
        assertThrows(UnauthorizedException.class, () -> securedInterceptor.invoke(invocationContext));
    }
}
