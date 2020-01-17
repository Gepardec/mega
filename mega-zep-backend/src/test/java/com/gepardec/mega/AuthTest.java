package com.gepardec.mega;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// FIXME: Not working test
@QuarkusTest
@Disabled
class AuthTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Object googleUser = new Object();

    @Test
    void testGoogleAuthentication() {
        given().contentType(ContentType.JSON)
                .body(googleUser)
                .post("/user/login")
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testGoogleAuthenticationDetails() throws IOException {
        final String response = given().contentType(ContentType.JSON)
                .body(googleUser)
                .post("/user/login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().asString();


        assertNotNull(response);
        assertNotEquals("", response);

        final MitarbeiterType mt = objectMapper.readValue(response, MitarbeiterType.class);
        assertNotNull(mt);
        assertNotEquals("", mt.getEmail());
        assertNotEquals("", mt.getVorname());
        assertNotEquals("", mt.getNachname());
    }

    @Test
    void logout_whenLogout_userDataNull() {
        given().get("/user/logout")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
