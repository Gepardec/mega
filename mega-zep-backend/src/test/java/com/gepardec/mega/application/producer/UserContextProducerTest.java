package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.service.api.user.UserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserContextProducerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserContextProducer producer;

    @Test
    void createUserContext_whenGeneralSecurityException_thenUserNullAndNotLogged() throws GeneralSecurityException, IOException {
        // Given
        when(request.getHeader(eq(UserContextProducer.X_AUTHORIZATION_HEADER))).thenReturn("12345");
        when(googleIdTokenVerifier.verify(anyString())).thenThrow(new GeneralSecurityException());

        // When
        final UserContext userContext = producer.createUserContext();

        // Then
        assertNull(userContext.user());
        assertFalse(userContext.loggedIn());
    }

    @Test
    void createUserContext_whenNoXAuthorizationHeader_thenThrowsUnauthorizedException() {
        when(request.getHeader(eq(UserContextProducer.X_AUTHORIZATION_HEADER))).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> producer.createUserContext());
    }

    @Test
    void createUserContext_whenGoogleIdTokenIsNull_thenThrowsUnauthorizedException() throws GeneralSecurityException, IOException {
        when(request.getHeader(eq(UserContextProducer.X_AUTHORIZATION_HEADER))).thenReturn("123");
        when(googleIdTokenVerifier.verify(eq("123"))).thenThrow(new UnauthorizedException());

        assertThrows(UnauthorizedException.class, () -> producer.createUserContext());
    }

    @Test
    void createUserContext_whenIOException_thenUserNullAndNotLogged() throws GeneralSecurityException, IOException {
        // Given
        when(request.getHeader(eq(UserContextProducer.X_AUTHORIZATION_HEADER))).thenReturn("12345");
        when(googleIdTokenVerifier.verify(anyString())).thenThrow(new IOException());

        // When
        final UserContext userContext = producer.createUserContext();

        // Then
        assertNull(userContext.user());
        assertFalse(userContext.loggedIn());
    }

    @Test
    void createUserContext_whenUserVerified_thenUserSetAndLogged() throws GeneralSecurityException, IOException {
        // Given
        when(request.getHeader(eq(UserContextProducer.X_AUTHORIZATION_HEADER))).thenReturn("12345");
        final GoogleIdToken googleIdToken = mock(GoogleIdToken.class, Answers.RETURNS_DEEP_STUBS);
        when(googleIdToken.getPayload().getEmail()).thenReturn("test@gepardec.com");
        when(googleIdToken.getPayload().get("picture")).thenReturn("http://www.gepardec.com/test.jpg");

        final User user = User.builder()
                .userId("1")
                .firstname("Thomas")
                .lastname("Herzog")
                .email("thomas.herzog@gepardec.com")
                .build();
        when(googleIdTokenVerifier.verify(anyString())).thenReturn(googleIdToken);
        when(userService.getUser("test@gepardec.com", "http://www.gepardec.com/test.jpg")).thenReturn(user);

        // When
        final UserContext userContext = producer.createUserContext();

        // Then
        assertNotNull(userContext.user());
        assertEquals(user, userContext.user());
        assertTrue(userContext.loggedIn());
    }
}