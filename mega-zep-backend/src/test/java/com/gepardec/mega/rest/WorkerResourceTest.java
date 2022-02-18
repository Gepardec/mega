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
import com.gepardec.mega.service.api.EmployeeService;
import com.gepardec.mega.service.api.MonthlyReportService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
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

        List<TimeWarning> timeWarnings = List.of();
        List<JourneyWarning> journeyWarnings = List.of();

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

        assertThat(actual.employee()).isEqualTo(employee);
        assertThat(timeWarnings).isEqualTo(actual.timeWarnings());
        assertThat(journeyWarnings).isEqualTo(actual.journeyWarnings());
        assertThat(billableTime).isEqualTo(actual.billableTime());
        assertThat(totalWorkingTime).isEqualTo(actual.totalWorkingTime());
        assertThat(vacationDays).isEqualTo(actual.vacationDays());
        assertThat(homeofficeDays).isEqualTo(actual.homeofficeDays());
        assertThat(compensatoryDays).isEqualTo(actual.compensatoryDays());
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
                .email("max.mustermann@gpeardec.com")
                .firstname("Max")
                .lastname("Mustermann")
                .roles(Set.of(role))
                .build();
    }
}
