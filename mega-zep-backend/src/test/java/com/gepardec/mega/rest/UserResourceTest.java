package com.gepardec.mega.rest;

import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

@QuarkusTest
class UserResourceTest {

    @InjectMock
    private UserContext userContext;

    @Test
    void get_whenUserNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        final User user = createUserForRole(Role.USER);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(false);

        given().get("/user")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void get_whenUserIsLogged_thenReturnsHttpStatusOK() {
        final User user = createUserForRole(Role.USER);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);

        given().get("/user")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void get_whenUSerIsLogged_thenReturnsUser() {
        final User user = createUserForRole(Role.USER);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);

        final User actual = given()
                .get("/user")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(User.class);

        Assertions.assertEquals(user, actual);
    }

    private User createUserForRole(final Role role) {
        return User.builder()
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .firstname("Thomas")
                .lastname("Herzog")
                .role(role)
                .build();
    }
}