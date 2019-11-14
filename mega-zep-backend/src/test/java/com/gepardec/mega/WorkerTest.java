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
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class WorkerTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static GoogleUser googleUser = new GoogleUser();

    @BeforeAll
    static void initTests () {
        googleUser.setId("123456879");
        googleUser.setEmail("christoph.ruhsam@gepardec.com");
        googleUser.setAuthToken("987654321");
    }

    @Test
    void testGetOneEmployees() {
        final String response = given().contentType(ContentType.JSON).body(googleUser).post("/worker/employee").then().statusCode(HttpStatus.SC_OK).extract().asString();
        assertNotNull(response);
        assertNotEquals("", response);

        try {
            final MitarbeiterType mitarbeiterType = objectMapper.readValue(response, MitarbeiterType.class);
            assertNotNull(mitarbeiterType);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllEmployees() {
        final String response = given().contentType(ContentType.JSON).body(googleUser).post("/worker/employees").then().statusCode(HttpStatus.SC_OK).extract().asString();
        assertNotNull(response);
        assertNotEquals("", response);

        try {
            List<MitarbeiterType> mitarbeiterTypeList = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, MitarbeiterType.class));
            assertNotNull(mitarbeiterTypeList);
            assertFalse(mitarbeiterTypeList.isEmpty());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
