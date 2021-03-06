package com.gepardec.mega.rest;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.SecurityContext;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
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
import java.util.Set;

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
        final User user = createUserForRole(Role.EMPLOYEE);
        when(userContext.user()).thenReturn(user);

        given().get("/worker/monthendreports")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void employeeMonthendReport_withReport_returnsReport() {
        User user = createUserForRole(Role.EMPLOYEE);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Employee employee = createEmployeeForUser(user);
        when(employeeService.getEmployee(anyString())).thenReturn(employee);

        List<TimeWarning> timeWarnings = List.of(TimeWarning.of(LocalDate.now(), 0.0, 0.0, 0.0));
        List<JourneyWarning> journeyWarnings = List.of(new JourneyWarning(LocalDate.now(), List.of("WARNING")));

        int vacationDays = 0;
        int homeofficeDays = 0;
        int compensatoryDays = 0;
        String billableTime = "00:00";
        String totalWorkingTime = "00:00";

        MonthlyReport expected = MonthlyReport.builder()
                .employee(employee)
                .timeWarnings(timeWarnings)
                .journeyWarnings(journeyWarnings)
                .comments(List.of())
                .employeeCheckState(EmployeeState.OPEN)
                .isAssigned(false)
                .employeeProgresses(List.of())
                .otherChecksDone(true)
                .billableTime(billableTime)
                .totalWorkingTime(totalWorkingTime)
                .compensatoryDays(compensatoryDays)
                .homeofficeDays(homeofficeDays)
                .vacationDays(vacationDays)
                .build();

        when(monthlyReportService.getMonthendReportForUser(anyString())).thenReturn(expected);

        MonthlyReport actual = given().contentType(ContentType.JSON)
                .get("/worker/monthendreports")
                .as(MonthlyReport.class);

        assertEquals(employee, actual.employee());
        assertEquals(actual.timeWarnings(), timeWarnings);
        assertEquals(actual.journeyWarnings(), journeyWarnings);
        assertEquals(actual.billableTime(), billableTime);
        assertEquals(actual.totalWorkingTime(), totalWorkingTime);
        assertEquals(actual.vacationDays(), vacationDays);
        assertEquals(actual.homeofficeDays(), homeofficeDays);
        assertEquals(actual.compensatoryDays(), compensatoryDays);
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
