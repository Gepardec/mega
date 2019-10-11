package com.gepardec.mega;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class StatusTest {

    @Test
    public void testWorkerStatus() {
        final String expectedStatusBody = "{\"code\":200,\"status\":\"OK\"}";

        given().when().get("/worker/status")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body(is(expectedStatusBody));
    }
}
