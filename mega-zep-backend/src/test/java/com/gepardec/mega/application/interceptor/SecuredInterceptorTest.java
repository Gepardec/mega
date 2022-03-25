package com.gepardec.mega.application.interceptor;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.interceptor.InvocationContext;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        assertThatThrownBy(() -> securedInterceptor.invoke(invocationContext)).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void invoke_tokenInvalid_throwUnauthorizedException() {
        when(securityContext.getEmail()).thenReturn(null);
        assertThatThrownBy(() -> securedInterceptor.invoke(invocationContext)).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void invoke_userLoggedAndTokenValid_callsProceed() throws Exception {
        when(securityContext.getEmail()).thenReturn("test@gepardec.com");
        securedInterceptor.invoke(invocationContext);

        verify(invocationContext, times(1)).proceed();
    }
}
