package com.gepardec.mega.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class ConfigResourceTest {

    private static final String CLIENT_ID = "my-client-id";

    @BeforeAll
    static void beforeAll() {
        System.setProperty("google.frontend.clientId", CLIENT_ID);
    }

    @Test
    void get_withPOST_returnsMethodNotallowed() {
        given().contentType(ContentType.TEXT)
                .post("/config")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void get_withGET_returnsConfig() {
        given().contentType(ContentType.TEXT)
                .get("/config")
                .then().statusCode(HttpStatus.SC_OK)
                .body("oauthClientId", equalTo(CLIENT_ID));
    }
}
