package com.gepardec.mega.rest;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.service.api.employee.EmployeeService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.common.mapper.TypeRef;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class EmployeeResourceTest {

    @InjectMock
    EmployeeService employeeService;

    @InjectMock
    private UserContext userContext;

    @Test
    void list_whenUserNotLoggedAndInRoleADMINISTRATOR_thenReturnsHttpStatusUNAUTHORIZED() {
        when(userContext.user()).thenReturn(createUserForRole(Role.ADMINISTRATOR));
        when(userContext.loggedIn()).thenReturn(false);

        given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void list_whenUserLoggedAndInRoleUSER_thenReturnsHttpStatusFORBIDDEN() {
        when(userContext.user()).thenReturn(createUserForRole(Role.USER));
        when(userContext.loggedIn()).thenReturn(true);

        given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void list_whenUserLoggedAndInRoleCONTROLLER_thenReturnsHttpStatusOK() {
        final User user = createUserForRole(Role.CONTROLLER);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);
        final Employee userAsEmployee = createEmployeeForUser(user);
        when(employeeService.getEmployee(anyString())).thenReturn(userAsEmployee);

        given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void list_whenUserLoggedAndInRoleADMINISTRATOR_thenReturnsHttpStatusOK() {
        final User user = createUserForRole(Role.ADMINISTRATOR);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);

        given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void list_whenUserLoggedAndInRoleADMINISTRATOR_thenReturnsEmployees() {
        final User user = createUserForRole(Role.ADMINISTRATOR);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);
        final Employee userAsEmployee = createEmployeeForUser(user);
        when(employeeService.getAllActiveEmployees()).thenReturn(List.of(userAsEmployee));

        final List<Employee> employees = given().get("/employees").as(new TypeRef<>() {});

        assertEquals(1, employees.size());
        final Employee actual = employees.get(0);
        assertEquals(userAsEmployee, actual);
    }

    @Test
    void update_whenContentTypeNotSet_returnsHttpStatusUNSUPPORTED_MEDIA_TYPE() {
        given().put("/employees")
                .then().statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
    }

    @Test
    void update_whenContentTypeIsTextPlain_returnsHttpStatusUNSUPPORTED_MEDIA_TYPE() {
        final User user = createUserForRole(Role.ADMINISTRATOR);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);

        given().contentType(MediaType.TEXT_PLAIN)
                .put("/employees")
                .then().statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
    }

    @Test
    void update_whenEmptyBody_returnsHttpStatusBAD_REQUEST() {
        final User user = createUserForRole(Role.ADMINISTRATOR);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);

        given().contentType(MediaType.APPLICATION_JSON)
                .put("/employees")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void update_whenEmptyList_returnsHttpStatusBAD_REQUEST() {
        final User user = createUserForRole(Role.ADMINISTRATOR);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(List.of())
                .put("/employees")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void update_whenValidRequest_returnsHttpStatusOK() {
        final User user = createUserForRole(Role.ADMINISTRATOR);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);
        final Employee employee = createEmployeeForUser(user);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(List.of(employee))
                .put("/employees")
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void update_whenValidRequestAndEmployeeServiceReturnsInvalidEmails_returnsInvalidEmails() {
        final User user = createUserForRole(Role.ADMINISTRATOR);
        when(userContext.user()).thenReturn(user);
        when(userContext.loggedIn()).thenReturn(true);
        final Employee userAsEmployee = createEmployeeForUser(user);
        final List<String> expected = List.of("invalid1@gmail.com", "invalid2@gmail.com");
        when(employeeService.updateEmployeesReleaseDate(anyList())).thenReturn(expected);

        final List<String> emails = given().contentType(MediaType.APPLICATION_JSON)
                .body(List.of(userAsEmployee))
                .put("/employees")
                .as(new TypeRef<>() {});

        assertEquals(2, emails.size());
        assertTrue(emails.containsAll(expected));
    }

    @Test
    void noMethod_whenHttpMethodIsPOST_returns405() {
        given().post("/employees")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    private Employee createEmployeeForUser(final User user) {
        return Employee.builder()
                .email(user.email())
                .firstName(user.firstname())
                .sureName(user.lastname())
                .title("Ing.")
                .userId(user.userId())
                .releaseDate("2020-01-01")
                .role(user.role().roleId)
                .active(true)
                .build();
    }

    private User createUserForRole(final Role role) {
        return User.builder()
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .firstname("Thomas")
                .lastname("Herzog")
                .role(role)
                .build();
    }
}
