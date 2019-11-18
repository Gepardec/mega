package com.gepardec.mega;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.mega.model.google.GoogleUser;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class MonthendReportTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static GoogleUser googleUser = new GoogleUser();

    @BeforeAll
    static void initTests() {
        googleUser.setEmail("max.mustermann@gepardec.com");

        try {
            final String output = objectMapper.writeValueAsString(googleUser);
            System.out.println(output);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


    @Test
    void monthendReport_withUser_shouldShowWarnings() throws IOException {
        final String response = given()
                .contentType(ContentType.JSON)
                .body(googleUser)
                .post("/worker/employee/monthendReport")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asString();

        assertNotNull(response);
        assertNotEquals("", response);

//        final MonthendReport monthendReport = objectMapper.readValue(response, MonthendReport.class);
//
//        assertNotNull(monthendReport);
//        assertEquals(monthendReport.getBreakWarnings().size(), 6);
    }
}
