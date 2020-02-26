package com.gepardec.mega.rest.probes;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ReadynessProbeTest {

    @Test
    void testReadyness() {
        given().when().get("/health/ready")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}