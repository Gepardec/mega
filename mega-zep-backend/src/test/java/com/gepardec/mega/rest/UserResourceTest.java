package com.gepardec.mega.rest;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.domain.model.User;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class UserResourceTest {

    @Test
    void user_onGetUser_returnUser() {
        final User user = given().contentType(ContentType.JSON)
                .get("/user")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(User.class);

        Assertions.assertNotNull(user);
        Assertions.assertEquals("0", user.userId());
        Assertions.assertEquals("Thomas_0@gepardec.com", user.email());
        Assertions.assertEquals("Thomas_0", user.firstname());
        Assertions.assertEquals("Thomas_0_Nachname", user.lastname());
        Assertions.assertEquals(Role.USER, user.role());
        Assertions.assertEquals("https://www.gepardec.com/test.jpg", user.pictureUrl());
    }
}