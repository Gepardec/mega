package com.gepardec.mega.rest.probes;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ReadynessProbeTest {

    @Test
    void ready_whenCalled_thenReturnsHttpStatusOK() {
        given().when()
                .get("/health/ready")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void live_whenCalled_thenReturnsHttpStatusOK() {
        given().when()
                .get("/health/live")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void health_whenCalled_thenReturnsHttpStatusOK() {
        given().when()
                .get("/health")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
}
