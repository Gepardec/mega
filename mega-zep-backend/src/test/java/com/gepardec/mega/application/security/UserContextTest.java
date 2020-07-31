package com.gepardec.mega.application.security;

import com.gepardec.mega.domain.model.User;
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
class UserContextTest {

    @Mock()
    HttpServletRequest request;

    @Mock
    GoogleIdTokenVerifier googleIdTokenVerifier;

    @Mock
    UserService userService;

    @InjectMocks
    UserContextImpl userContext;

    @BeforeEach
    void setUp() {
        Mockito.when(request.getHeader(Mockito.anyString())).thenReturn("12345");
    }

    @Test
    void postConstruct_invalidHeader_userNull() throws GeneralSecurityException, IOException {
        // Given
        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenThrow(new GeneralSecurityException());

        // When
        userContext.doPostConstruct();

        // Then
        Assertions.assertNull(userContext.getUser());
        Assertions.assertFalse(userContext.isLoggedIn());
    }

    @Test
    void postConstruct_validHeader_userSet() throws GeneralSecurityException, IOException {
        // Given
        final GoogleIdToken googleIdToken = Mockito.mock(GoogleIdToken.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(googleIdToken.getPayload().getEmail()).thenReturn("test@gepardec.com");
        Mockito.when(googleIdToken.getPayload().get("picture")).thenReturn("http://www.gepardec.com/test.jpg");

        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        Mockito.when(userService.getUser("test@gepardec.com", "http://www.gepardec.com/test.jpg")).thenReturn(User.builder().firstname("test").build());

        // When
        userContext.doPostConstruct();

        // Then
        Assertions.assertNotNull(userContext.getUser());
        Assertions.assertEquals("test", userContext.getUser().firstname());
        Assertions.assertTrue(userContext.isLoggedIn());
    }
}