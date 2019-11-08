package com.gepardec.mega;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.mega.model.google.GoogleUser;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AuthTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static GoogleUser googleUser = new GoogleUser();

    @BeforeAll
    static void initTests () {
        googleUser.setId("123456879");
        googleUser.setEmail("christoph.ruhsam@gepardec.com");
        googleUser.setAuthToken("987654321");
    }

    @Test
    void testGoogleAuthentication() {
        given().contentType(ContentType.JSON).body(googleUser).post("/user/login").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testGoogleAuthenticationDetails() {
        final String response = given().contentType(ContentType.JSON).body(googleUser).post("/user/login").then().statusCode(HttpStatus.SC_OK).extract().asString();

        assertNotNull(response);
        assertNotEquals("", response);

        try {
            final MitarbeiterType mt = objectMapper.readValue(response, MitarbeiterType.class);
            assertNotNull(mt);
            assertNotEquals("", mt.getEmail());
            assertNotEquals("", mt.getVorname());
            assertNotEquals("", mt.getNachname());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
