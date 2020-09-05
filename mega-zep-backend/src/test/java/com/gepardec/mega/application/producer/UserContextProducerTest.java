package com.gepardec.mega.application.producer;

import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.service.api.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

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

    @BeforeEach
    void setUp() {
        Mockito.when(request.getHeader(Mockito.anyString())).thenReturn("12345");
    }

    @Test
    void createUserContext_whenGeneralSecurityException_thenUserNullAndNotLogged() throws GeneralSecurityException, IOException {
        // Given
        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenThrow(new GeneralSecurityException());

        // When
        final UserContext userContext = producer.createUserContext();

        // Then
        Assertions.assertNull(userContext.user());
        Assertions.assertFalse(userContext.loggedIn());
    }

    @Test
    void createUserContext_whenIOException_thenUserNullAndNotLogged() throws GeneralSecurityException, IOException {
        // Given
        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenThrow(new IOException());

        // When
        final UserContext userContext = producer.createUserContext();

        // Then
        Assertions.assertNull(userContext.user());
        Assertions.assertFalse(userContext.loggedIn());
    }

    @Test
    void createUserContext_whenUserVerified_thenUserSetAndLogged() throws GeneralSecurityException, IOException {
        // Given
        final GoogleIdToken googleIdToken = Mockito.mock(GoogleIdToken.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(googleIdToken.getPayload().getEmail()).thenReturn("test@gepardec.com");
        Mockito.when(googleIdToken.getPayload().get("picture")).thenReturn("http://www.gepardec.com/test.jpg");

        final User user = User.builder()
                .userId("1")
                .firstname("Thomas")
                .lastname("Herzog")
                .email("thomas.herzog@gepardec.com")
                .build();
        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        Mockito.when(userService.getUser("test@gepardec.com", "http://www.gepardec.com/test.jpg")).thenReturn(user);

        // When
        final UserContext userContext = producer.createUserContext();

        // Then
        Assertions.assertNotNull(userContext.user());
        Assertions.assertEquals(user, userContext.user());
        Assertions.assertTrue(userContext.loggedIn());
    }
}