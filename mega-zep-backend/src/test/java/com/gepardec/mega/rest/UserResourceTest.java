package com.gepardec.mega.rest;

import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.SecurityContext;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
class UserResourceTest {

    @InjectMock
    private SecurityContext securityContext;

    @InjectMock
    private UserContext userContext;

    @Test
    void get_whenUserNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        final User user = createUserForRole(Role.EMPLOYEE);
        when(userContext.getUser()).thenReturn(user);

        given().get("/user")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void get_whenUserIsLogged_thenReturnsHttpStatusOK() {
        final User user = createUserForRole(Role.EMPLOYEE);
        when(securityContext.getEmail()).thenReturn(user.getEmail());
        when(userContext.getUser()).thenReturn(user);

        given().get("/user")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void get_whenUserIsLogged_thenReturnsUser() {
        final User user = createUserForRole(Role.EMPLOYEE);
        when(securityContext.getEmail()).thenReturn(user.getEmail());
        when(userContext.getUser()).thenReturn(user);
        final User actual = given()
                .get("/user")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(User.class);

        assertThat(actual.getUserId()).isEqualTo(user.getUserId());
        assertThat(actual.getEmail()).isEqualTo(user.getEmail());
    }

    private User createUserForRole(final Role role) {
        return User.builder()
                .dbId(1)
                .userId("1")
                .email("max.mustermann@gpeardec.com")
                .firstname("Max")
                .lastname("Mustermann")
                .roles(Set.of(role))
                .build();
    }
}
