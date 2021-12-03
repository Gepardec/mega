package com.gepardec.mega.rest;

import com.gepardec.mega.domain.model.*;
import com.gepardec.mega.rest.model.EmployeeStep;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
class StepEntryResourceTest {

    @InjectMock
    private UserContext userContext;

    @InjectMock
    private SecurityContext securityContext;

    @InjectMock
    private StepEntryService stepEntryService;

    @Test
    void close_whenPOST_thenReturnsStatusMETHOD_NOT_ALLOWED() {
        given().contentType(ContentType.JSON)
                .post("/stepentry/close")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void close_whenUserNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        final User user = createUserForRole(Role.EMPLOYEE);
        when(userContext.user()).thenReturn(user);

        given().contentType(ContentType.JSON).put("/stepentry/close")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void close_whenValid_thenReturnsUpdatedNumber() {
        when(stepEntryService.setOpenAndAssignedStepEntriesDone(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.anyLong(), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(true);

        final User user = createUserForRole(Role.EMPLOYEE);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        EmployeeStep employeeStep = createEmployeeStep();

        final boolean updated = given().contentType(ContentType.JSON)
                .body(employeeStep)
                .put("/stepentry/close")
                .as(Boolean.class);

        assertThat(updated).isTrue();
    }

    private User createUserForRole(final Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return User.builder()
                .userId("1")
                .dbId(1)
                .email("max.mustermann@gpeardec.com")
                .firstname("Max")
                .lastname("Mustermann")
                .roles(roles)
                .build();
    }

    private EmployeeStep createEmployeeStep() {
        Employee employee = Employee.builder()
                .userId("1")
                .email("max.mustermann@gpeardec.com")
                .build();

        return EmployeeStep.builder()
                .employee(employee)
                .stepId(1L)
                .currentMonthYear("2020-01-01")
                .build();
    }
}
