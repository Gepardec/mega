package com.gepardec.mega.rest;

import com.gepardec.mega.UserServiceMock;
import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.domain.model.User;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class UserResourceTest {

    @Inject
    UserServiceMock userServiceMock;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = UserServiceMock.createUser();
        userServiceMock.setUser(user);
    }

    @Test
    void login_withoutIdToken_returnsBadRequest() {
        given().contentType(ContentType.TEXT)
                .post("/user/login")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void login_withIdTokenAndContentTypeXML_returnsUnsupportedMediaType() {
        given().contentType(ContentType.XML)
                .post("/user/login")
                .then().statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
    }

    @Test
    void login_withIdTokenAndContentTypeJSON_returnsOK() {
        given().contentType(ContentType.JSON)
                .body("fakeIdToken")
                .post("/user/login")
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void login_withIdTokenAndContentTypeTEXT_returnsOK() {
        given().contentType(ContentType.TEXT)
                .body("fakeIdToken")
                .post("/user/login")
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void login_withIdToken_returnsLoggedUser() {
        given().contentType(ContentType.TEXT)
                .body("fakeIdToken")
                .post("/user/login")
                .then().statusCode(HttpStatus.SC_OK)
                .body("email", equalTo(user.email()))
                .body("lastname", equalTo(user.lastname()))
                .body("firstname", equalTo(user.firstname()))
                .body("role", equalTo(Optional.ofNullable(user.role()).map(Role::name).orElse(null)))
                .body("pictureUrl", equalTo(user.pictureUrl()));
    }

    @Test
    void logout_whenLogout_userDataNull() {
        given().post("/user/logout")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
