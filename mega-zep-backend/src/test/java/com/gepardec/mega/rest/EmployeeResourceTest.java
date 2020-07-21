package com.gepardec.mega.rest;

import com.gepardec.mega.EmployeeServiceMock;
import com.gepardec.mega.GoogleTokenVerifierMock;
import com.gepardec.mega.SessionUserMock;
import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.domain.model.employee.Employee;
import com.gepardec.mega.util.EmployeeTestUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

@ExtendWith(MockitoExtension.class)
@QuarkusTest
@Disabled
public class EmployeeResourceTest {

    private GoogleIdToken googleIdToken;

    private EmployeeService employeeService;

    @Inject
    SessionUserMock sessionUserMock;

    @Inject
    GoogleTokenVerifierMock googleTokenVerifierMock;

    @Inject
    EmployeeServiceMock employeeServiceMock;

    @BeforeEach
    void beforeEach() throws Exception {
        final String userId = "1337-thomas.herzog";
        final String email = "thomas.herzog@gepardec.com";
        final GoogleIdTokenVerifier googleIdTokenVerifier = Mockito.mock(GoogleIdTokenVerifier.class, Answers.RETURNS_DEEP_STUBS);
        googleIdToken = Mockito.mock(GoogleIdToken.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        googleTokenVerifierMock.setDelegate(googleIdTokenVerifier);
        sessionUserMock.init(userId, email, "", Role.ADMINISTRATOR.roleId);

        employeeService = Mockito.mock(EmployeeService.class);
        employeeServiceMock.setDelegate(employeeService);
    }

    @Test
    void employees_withPOST_returnsMethodNotAllowed() {
        given().post("/employees")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void employees_withValidRequest_returnsActiveEmployees() {
        final List<Employee> employees = IntStream.range(1, 10).mapToObj(EmployeeTestUtil::createEmployee).collect(Collectors.toList());
        Mockito.when(employeeService.getAllActiveEmployees()).thenReturn(employees);

        final List<Employee> actual = given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(employees.size(), actual.size());
        for (int i = 0; i < employees.size(); i++) {
            EmployeeTestUtil.assertEmployee(actual.get(i), employees.get(i));
        }
    }

    @Test
    void employeesUpdate_withPOST_returnsMethodNotAllowed() {
        given().contentType(ContentType.JSON)
                .post("/employees")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void employeesUpdate_withEmptyBody_returnsBadRequest() {
        given().contentType(ContentType.JSON)
                .put("/employees")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void employeesUpdate_withEmptyArray_returnsBadRequest() {
        given().contentType(ContentType.JSON)
                .body(new ArrayList<>())
                .put("/employees")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void employeesUpdate_withInvalidEmployees_returnsInvalidEmails() {
        final List<Employee> employees = IntStream.range(1, 11).mapToObj(EmployeeTestUtil::createEmployee).collect(Collectors.toList());
        final List<String> expected = employees.subList(0, 5).stream().map(Employee::getEmail).collect(Collectors.toList());
        Mockito.when(employeeService.updateEmployeesReleaseDate(Mockito.anyList())).thenReturn(expected);
        employeeServiceMock.setDelegate(employeeService);

        final List<String> actual = given().contentType(ContentType.JSON)
                .body(employees)
                .put("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    void employeesUpdate_withAllValidEmployees_returnsNothing() {
        final List<Employee> employees = IntStream.range(1, 11).mapToObj(EmployeeTestUtil::createEmployee).collect(Collectors.toList());
        Mockito.when(employeeService.updateEmployeesReleaseDate(Mockito.anyList())).thenReturn(Collections.emptyList());
        employeeServiceMock.setDelegate(employeeService);

        final List<String> actual = given().contentType(ContentType.JSON)
                .body(employees)
                .put("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(new TypeRef<>() {
                });

        Assertions.assertTrue(actual.isEmpty());
    }
}
