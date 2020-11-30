package com.gepardec.mega.rest;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.*;
import com.gepardec.mega.rest.model.ManagementEntry;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    StepEntryService stepEntryService;

    @InjectMock
    CommentService commentService;

    @InjectMock
    private SecurityContext securityContext;

    @InjectMock
    private UserContext userContext;

    @Test
    void list_whenUserNotLoggedAndInRoleOFFICE_MANAGEMENT_thenReturnsHttpStatusUNAUTHORIZED() {
        when(userContext.user()).thenReturn(createUserForRole(Role.OFFICE_MANAGEMENT));

        given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void list_whenUserNotLoggedAndInRolePROJECT_LEAD_thenReturnsHttpStatusUNAUTHORIZED() {
        when(userContext.user()).thenReturn(createUserForRole(Role.PROJECT_LEAD));

        given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void list_whenUserLoggedAndInRoleEMPLOYEE_thenReturnsHttpStatusFORBIDDEN() {
        final User user = createUserForRole(Role.EMPLOYEE);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void list_whenUserLoggedAndInRoleOFFICE_MANAGEMENT_thenReturnsHttpStatusOK() {
        final User user = createUserForRole(Role.OFFICE_MANAGEMENT);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);
        final Employee userAsEmployee = createEmployeeForUser(user);
        when(employeeService.getEmployee(anyString())).thenReturn(userAsEmployee);

        given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void list_whenUserLoggedAndInRolePROJECT_LEAD_thenReturnsHttpStatusOK() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        given().get("/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void list_whenUserLoggedAndInRoleOFFICE_MANAGEMENT_thenReturnsEmployees() {
        final User user = createUserForRole(Role.OFFICE_MANAGEMENT);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);
        final Employee userAsEmployee = createEmployeeForUser(user);
        when(employeeService.getAllActiveEmployees()).thenReturn(List.of(userAsEmployee));

        final List<Employee> employees = given().get("/employees").as(new TypeRef<>() {

        });

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
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(userContext.user()).thenReturn(user);

        given().contentType(MediaType.TEXT_PLAIN)
                .put("/employees")
                .then().statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
    }

    @Test
    void update_whenEmptyBody_returnsHttpStatusBAD_REQUEST() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        given().contentType(MediaType.APPLICATION_JSON)
                .put("/employees")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void update_whenEmptyList_returnsHttpStatusBAD_REQUEST() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(List.of())
                .put("/employees")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void update_whenValidRequest_returnsHttpStatusOK() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);
        final Employee employee = createEmployeeForUser(user);

        given().contentType(MediaType.APPLICATION_JSON)
                .body(List.of(employee))
                .put("/employees")
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void update_whenValidRequestAndEmployeeServiceReturnsInvalidEmails_returnsInvalidEmails() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);
        final Employee userAsEmployee = createEmployeeForUser(user);
        final List<String> expected = List.of("invalid1@gmail.com", "invalid2@gmail.com");
        when(employeeService.updateEmployeesReleaseDate(anyList())).thenReturn(expected);

        final List<String> emails = given().contentType(MediaType.APPLICATION_JSON)
                .body(List.of(userAsEmployee))
                .put("/employees")
                .as(new TypeRef<>() {

                });

        assertEquals(2, emails.size());
        assertTrue(emails.containsAll(expected));
    }

    @Test
    void noMethod_whenHttpMethodIsPOST_returns405() {
        given().post("/employees")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void getAllOfficeManagementEntries_whenNotLoggedIn_thenReturnsHttpStatusUNAUTHORIZED() {
        given().contentType(ContentType.JSON)
                .delete("/employees/officemanagemententries")
                .then().assertThat().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void getAllOfficeManagementEntries_whenValid_thenReturnsListOfEntries() {
        final User user = createUserForRole(Role.OFFICE_MANAGEMENT);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        when(employeeService.getAllActiveEmployees())
                .thenReturn(List.of(Employee.builder().releaseDate("2020-01-01").email("marko.gattringer@gepardec.com").build()));

        List<com.gepardec.mega.db.entity.StepEntry> entries = List.of(
                createStepEntryForStep(StepName.CONTROL_EXTERNAL_TIMES, State.DONE),
                createStepEntryForStep(StepName.CONTROL_INTERNAL_TIMES, State.OPEN),
                createStepEntryForStep(StepName.CONTROL_TIME_EVIDENCES, State.DONE),
                createStepEntryForStep(StepName.CONTROL_TIMES, State.OPEN)
        );

        when(commentService.cntFinishedAndTotalCommentsForEmployee(ArgumentMatchers.any(Employee.class)))
                .thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());


        when(stepEntryService.findAllStepEntriesForEmployee(ArgumentMatchers.any(Employee.class)))
                .thenReturn(entries);


        List<ManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/employees/officemanagemententries")
                .as(new TypeRef<>() {});

        assertEquals(1L, result.size());
        ManagementEntry entry = result.get(0);
        assertEquals(com.gepardec.mega.domain.model.State.DONE, entry.customerCheckState());
        assertEquals(com.gepardec.mega.domain.model.State.OPEN, entry.internalCheckState());
        assertEquals(com.gepardec.mega.domain.model.State.OPEN, entry.employeeCheckState());
        assertEquals(com.gepardec.mega.domain.model.State.DONE, entry.projectCheckState());
        assertEquals("marko.gattringer@gepardec.com", entry.employee().email());
        assertEquals("2020-01-01", entry.employee().releaseDate());
        assertEquals(3L, entry.totalComments());
        assertEquals(2L, entry.finishedComments());
    }

    @Test
    void getAllOfficeManagementEntries_whenNoActiveEmployeesFound_thenReturnsEmptyResultList() {
        final User user = createUserForRole(Role.OFFICE_MANAGEMENT);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        when(employeeService.getAllActiveEmployees()).thenReturn(Collections.emptyList());

        List<com.gepardec.mega.db.entity.StepEntry> entries = List.of(
                createStepEntryForStep(StepName.CONTROL_EXTERNAL_TIMES, State.DONE),
                createStepEntryForStep(StepName.CONTROL_INTERNAL_TIMES, State.OPEN),
                createStepEntryForStep(StepName.CONTROL_TIME_EVIDENCES, State.DONE),
                createStepEntryForStep(StepName.CONTROL_TIMES, State.OPEN)
        );

        when(commentService.cntFinishedAndTotalCommentsForEmployee(ArgumentMatchers.any(Employee.class)))
                .thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());


        when(stepEntryService.findAllStepEntriesForEmployee(ArgumentMatchers.any(Employee.class)))
                .thenReturn(entries);


        List<ManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/employees/officemanagemententries")
                .as(new TypeRef<>() {
                });

        assertEquals(0L, result.size());
    }

    @Test
    void getAllOfficeManagementEntries_whenNoStepEntriesFound_thenReturnsEmptyResultList() {
        final User user = createUserForRole(Role.OFFICE_MANAGEMENT);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        when(commentService.cntFinishedAndTotalCommentsForEmployee(ArgumentMatchers.any(Employee.class)))
                .thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());

        when(stepEntryService.findAllStepEntriesForEmployee(ArgumentMatchers.any(Employee.class)))
                .thenReturn(Collections.emptyList());

        when(employeeService.getAllActiveEmployees())
                .thenReturn(List.of(Employee.builder().releaseDate("2020-01-01").email("marko.gattringer@gepardec.com").build()));

        List<ManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/employees/officemanagemententries")
                .as(new TypeRef<>() {
                });

        assertEquals(0L, result.size());
    }

    private com.gepardec.mega.db.entity.Step createStep(StepName stepName) {
        com.gepardec.mega.db.entity.Step step = new com.gepardec.mega.db.entity.Step();
        step.setName(stepName.name());
        return step;
    }

    private com.gepardec.mega.db.entity.StepEntry createStepEntryForStep(StepName stepName, State state) {
        com.gepardec.mega.db.entity.StepEntry stepEntry = new com.gepardec.mega.db.entity.StepEntry();
        stepEntry.setStep(createStep(stepName));
        stepEntry.setState(state);
        return stepEntry;
    }

    private Employee createEmployeeForUser(final User user) {
        return Employee.builder()
                .email(user.email())
                .firstname(user.firstname())
                .lastname(user.lastname())
                .title("Ing.")
                .userId(user.userId())
                .releaseDate("2020-01-01")
                .active(true)
                .build();
    }

    private User createUserForRole(final Role role) {
        return User.builder()
                .dbId(1)
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .firstname("Thomas")
                .lastname("Herzog")
                .roles(Set.of(role))
                .build();
    }
}
