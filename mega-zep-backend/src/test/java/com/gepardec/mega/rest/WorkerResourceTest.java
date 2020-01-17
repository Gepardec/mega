package com.gepardec.mega.rest;

import com.gepardec.mega.SessionUserMock;
import com.gepardec.mega.ZepSoapPortTypeMock;
import com.gepardec.mega.aplication.security.Role;
import de.provantis.zep.ZepSoapPortType;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Disabled
public class WorkerResourceTest {

    @Inject
    SessionUserMock sessionUserMock;

    @Inject
    ZepSoapPortTypeMock zepSoapPortTypeMock;

    @Mock
    ZepSoapPortType zepSoapPortType;

    @BeforeEach
    void beforeEach() {
        sessionUserMock.init("thomas.herzog@gepardec.com", "", Role.ADMINISTRATOR.roleId);
        zepSoapPortTypeMock.setDelegate(zepSoapPortType);
    }

    @Test
    void employee_withEmptyEmail_returnsBadRequest() {
        given().contentType(ContentType.TEXT)
                .post("/worker/employee")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
