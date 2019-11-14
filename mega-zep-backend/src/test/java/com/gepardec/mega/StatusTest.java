package com.gepardec.mega;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class StatusTest {

    @Test
    void testReadyness() {
        given().when().get("/health/ready")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}