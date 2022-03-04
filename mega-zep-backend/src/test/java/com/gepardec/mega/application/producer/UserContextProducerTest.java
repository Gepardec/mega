package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.SecurityContext;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.service.api.user.UserService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@QuarkusTest
class UserContextProducerTest {

    @InjectMock
    SecurityContext securityContext;

    @InjectMock
    UserService userService;

    @Inject
    UserContextProducer producer;

    @Test
    void createUserContext_whenUserVerified_thenUserSetAndLogged() {
        // Given
        final User user = User.builder()
                .dbId(1)
                .userId("1")
                .firstname("Max")
                .lastname("Mustermann")
                .email("no-reply@gepardec.com")
                .roles(Set.of(Role.EMPLOYEE))
                .build();
        when(securityContext.email()).thenReturn("test@gepardec.com");
        when(userService.findUserForEmail("test@gepardec.com")).thenReturn(user);

        // When
        final UserContext userContext = producer.createUserContext();

        // Then
        assertThat(userContext.user()).isNotNull();
        assertThat(userContext.user()).isEqualTo(user);
    }

    @Test
    void createUserContext_whenSecurityContextIsEmpty_thenThrowsUnauthorizedException() {
        assertThatThrownBy(() -> producer.createUserContext()).isInstanceOf(UnauthorizedException.class);
    }
}
