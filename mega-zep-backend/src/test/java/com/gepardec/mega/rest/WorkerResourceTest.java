package com.gepardec.mega.rest;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.*;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class WorkerResourceTest {

    @InjectMock
    MonthlyReportService monthlyReportService;

    @InjectMock
    private EmployeeService employeeService;

    @InjectMock
    private SecurityContext securityContext;

    @InjectMock
    private UserContext userContext;

    @Test
    void monthlyReport_whenPOST_thenReturnsHttpStatusMETHOD_NOT_ALLOWED() {
        given().contentType(ContentType.JSON)
                .post("/worker/monthendreports")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void monthlyReport_whenUserNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        final User user = createUserForRole(Role.USER);
        when(userContext.user()).thenReturn(user);

        given().get("/worker/monthendreports")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void employeeMonthendReport_withReport_returnsReport() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        final Employee employee = createEmployeeForUser(user);
        when(employeeService.getEmployee(anyString())).thenReturn(employee);
        final List<TimeWarning> timeWarnings = List.of(TimeWarning.of(LocalDate.now(), 0.0, 0.0, 0.0));
        final List<JourneyWarning> journeyWarnings = List.of(new JourneyWarning(LocalDate.now(), List.of("WARNING")));
        final MonthlyReport expected = MonthlyReport.of(employee, timeWarnings, journeyWarnings, List.of(), State.OPEN, false,true);
        when(monthlyReportService.getMonthendReportForUser(anyString())).thenReturn(expected);

        final MonthlyReport actual = given().contentType(ContentType.JSON)
                .get("/worker/monthendreports")
                .as(MonthlyReport.class);

        assertEquals(employee, actual.employee());
        assertEquals(actual.timeWarnings(), timeWarnings);
        assertEquals(actual.journeyWarnings(), journeyWarnings);
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
