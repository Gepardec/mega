package com.gepardec.mega;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

// FIXME: Not working test
@QuarkusTest
@Disabled
public class WorkerTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Object googleUser = new Object();

    @Test
    void testGetOneEmployees() throws IOException {
        final String response = given().contentType(ContentType.JSON).body(googleUser).post("/worker/employee").then().statusCode(HttpStatus.SC_OK).extract().asString();
        assertNotNull(response);
        assertNotEquals("", response);

        final MitarbeiterType mitarbeiterType = objectMapper.readValue(response, MitarbeiterType.class);
        assertNotNull(mitarbeiterType);
    }

    @Test
    void testGetAllEmployees() throws IOException {
        final String response = given().contentType(ContentType.JSON).body(googleUser).post("/worker/employees").then().statusCode(HttpStatus.SC_OK).extract().asString();
        assertNotNull(response);
        assertNotEquals("", response);

        List<MitarbeiterType> mitarbeiterTypeList = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, MitarbeiterType.class));
        assertNotNull(mitarbeiterTypeList);
        assertFalse(mitarbeiterTypeList.isEmpty());
    }
}
