package com.gepardec.mega.rest;

import com.gepardec.mega.UserServiceMock;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class UserResourceTest {

    @Inject
    UserServiceMock userServiceMock;

    private MitarbeiterType mitarbeiter;

    @BeforeEach
    void beforeEach() {
        mitarbeiter = UserServiceMock.createMitarbeiterType();
        userServiceMock.setMitarbeiter(mitarbeiter);
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
                .body("email", equalTo(mitarbeiter.getEmail()))
                .body("lastname", equalTo(mitarbeiter.getNachname()))
                .body("firstname", equalTo(mitarbeiter.getVorname()))
                .body("role", equalTo("USER"));
    }

    @Test
    void logout_whenLogout_userDataNull() {
        given().post("/user/logout")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
