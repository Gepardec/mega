package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.SecurityContext;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityContextProducerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @InjectMocks
    private SecurityContextProducer producer;

    @Test
    void createSecurityContext_whenGoogleIdTokenVerified_thenGoogleIdTokenSetAndLogged() throws GeneralSecurityException, IOException {
        // Given
        when(request.getHeader(eq(SecurityContextProducer.X_AUTHORIZATION_HEADER))).thenReturn("12345");
        final GoogleIdToken googleIdToken = mock(GoogleIdToken.class, Answers.RETURNS_DEEP_STUBS);
        when(googleIdTokenVerifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload().getEmail()).thenReturn("test@gepardec.com");

        // When
        final SecurityContext securityContext = producer.createSecurityContext();

        // Then
        assertNotNull(securityContext.email());
        assertEquals("test@gepardec.com", securityContext.email());
    }

    @Test
    void createSecurityContext_whenGeneralSecurityException_thenGoogleIdTokenNullAndNotLogged() throws GeneralSecurityException, IOException {
        // Given
        when(request.getHeader(eq(SecurityContextProducer.X_AUTHORIZATION_HEADER))).thenReturn("12345");
        when(googleIdTokenVerifier.verify(anyString())).thenThrow(new GeneralSecurityException());

        // When
        final SecurityContext securityContext = producer.createSecurityContext();

        // Then
        assertNull(securityContext.email());
    }

    @Test
    void createSecurityContext_whenNoXAuthorizationHeader_thenThrowsUnauthorizedException() {
        when(request.getHeader(eq(SecurityContextProducer.X_AUTHORIZATION_HEADER))).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> producer.createSecurityContext());
    }

    @Test
    void createSecurityContext_whenGoogleIdTokenIsNull_thenThrowsUnauthorizedException() throws GeneralSecurityException, IOException {
        when(request.getHeader(eq(SecurityContextProducer.X_AUTHORIZATION_HEADER))).thenReturn("123");
        when(googleIdTokenVerifier.verify(eq("123"))).thenThrow(new UnauthorizedException());

        assertThrows(UnauthorizedException.class, () -> producer.createSecurityContext());
    }

    @Test
    void createSecurityContext_whenIOException_thenGoogleIdTokenNullAndNotLogged() throws GeneralSecurityException, IOException {
        // Given
        when(request.getHeader(eq(SecurityContextProducer.X_AUTHORIZATION_HEADER))).thenReturn("12345");
        when(googleIdTokenVerifier.verify(anyString())).thenThrow(new IOException());

        // When
        final SecurityContext securityContext = producer.createSecurityContext();

        // Then
        assertNull(securityContext.email());
    }
}