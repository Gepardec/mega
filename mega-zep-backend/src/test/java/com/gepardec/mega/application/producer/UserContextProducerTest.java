package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.SecurityContext;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.service.api.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserContextProducerTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserContextProducer producer;

    @Test
    void createUserContext_whenUserVerified_thenUserSetAndLogged() throws GeneralSecurityException, IOException {
        // Given
        final User user = User.builder()
                .dbId(1)
                .userId("1")
                .firstname("Thomas")
                .lastname("Herzog")
                .email("thomas.herzog@gepardec.com")
                .build();
        when(securityContext.email()).thenReturn("test@gepardec.com");
        when(securityContext.pictureUrl()).thenReturn("http://www.gepardec.com/test.jpg");
        when(userService.findUserForEmail("test@gepardec.com", "http://www.gepardec.com/test.jpg")).thenReturn(user);

        // When
        final UserContext userContext = producer.createUserContext();

        // Then
        assertNotNull(userContext.user());
        assertEquals(user, userContext.user());
    }

    @Test
    void createUserContext_whenSecurityContextIsEmpty_thenThrowsUnauthorizedException() {
        assertThrows(UnauthorizedException.class, () -> producer.createUserContext());
    }
}