package com.gepardec.mega.rest;

import com.gepardec.mega.GoogleTokenVerifierMock;
import com.gepardec.mega.MonthlyReportServiceMock;
import com.gepardec.mega.SessionUserMock;
import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Disabled
public class WorkerResourceTest {

    private GoogleIdToken googleIdToken;

    private MonthlyReportService monthlyReportService;

    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @Inject
    SessionUserMock sessionUserMock;

    @Inject
    MonthlyReportServiceMock workerServiceMock;

    @Inject
    GoogleTokenVerifierMock googleTokenVerifierMock;

    @BeforeEach
    void beforeEach() throws Exception {
        final String userId = "1337-thomas.herzog";
        final String email = "thomas.herzog@gepardec.com";
        googleIdToken = Mockito.mock(GoogleIdToken.class, Answers.RETURNS_DEEP_STUBS);
        googleIdTokenVerifier = Mockito.mock(GoogleIdTokenVerifier.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        googleTokenVerifierMock.setDelegate(googleIdTokenVerifier);
        sessionUserMock.init(userId, email, "", Role.ADMINISTRATOR.roleId);

        monthlyReportService = Mockito.mock(MonthlyReportService.class);
        workerServiceMock.setDelegate(monthlyReportService);
    }


    @Test
    void employeeMonthendReport_withPOST_returnsMethodNotAllowed() {
        given().contentType(ContentType.JSON)
                .post("/worker/monthendreports")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void employeeMonthendReport_withNoReport_returnsNotFound() {
        given().contentType(ContentType.JSON)
                .get("/worker/monthendreports")
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void employeeMonthendReport_withReport_returnsReport() {
        final Employee employee = createEmployee(0);
        final MonthlyReport expected = createZepMonthlyReport(employee);
        Mockito.when(monthlyReportService.getMonthendReportForUser(Mockito.anyString())).thenReturn(expected);

        final MonthlyReport actual = given().contentType(ContentType.JSON)
                .get("/worker/monthendreports")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(MonthlyReport.class);

        assertEmployee(actual.getEmployee(), employee);
        assertTimeWarnings(expected.getTimeWarnings(), actual.getTimeWarnings());
        assertJourneyWarnings(expected.getJourneyWarnings(), actual.getJourneyWarnings());
    }

    private MonthlyReport createZepMonthlyReport(final Employee employee) {
        final List<TimeWarning> timeWarnings = Collections.singletonList(TimeWarning.of(LocalDate.now(), 0.0, 0.0, 0.0));
        final List<JourneyWarning> journeyWarnings = Collections.singletonList(new JourneyWarning(LocalDate.now(), Collections.singletonList("WARNING")));

        final MonthlyReport monthlyReport = new MonthlyReport();
        monthlyReport.setTimeWarnings(timeWarnings);
        monthlyReport.setJourneyWarnings(journeyWarnings);
        monthlyReport.setEmployee(employee);
        return monthlyReport;
    }

    private void assertJourneyWarnings(List<JourneyWarning> expected, List<JourneyWarning> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertJourneyWarning(expected.get(i), actual.get(i));
        }
    }

    private void assertJourneyWarning(JourneyWarning expected, JourneyWarning actual) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getDate(), actual.getDate(), "date"),
                () -> Assertions.assertIterableEquals(expected.getWarnings(), actual.getWarnings(), "warnings")
        );
    }

    private void assertTimeWarnings(List<TimeWarning> expected, List<TimeWarning> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertTimeWarning(expected.get(i), actual.get(i));
        }
    }

    private void assertTimeWarning(TimeWarning expected, TimeWarning actual) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getDate(), actual.getDate(), "date"),
                () -> Assertions.assertEquals(expected.getExcessWorkTime(), actual.getExcessWorkTime(), "exessWorkTime"),
                () -> Assertions.assertEquals(expected.getMissingBreakTime(), actual.getMissingBreakTime(), "missingBreakTime"),
                () -> Assertions.assertEquals(expected.getMissingRestTime(), actual.getMissingRestTime(), "missingRestTime")
        );
    }

    private Employee createEmployee(final int userId) {
        final String name = "Thomas_" + userId;

        final Employee employee = Employee.builder()
                .email(name + "@gepardec.com")
                .firstName(name)
                .sureName(name + "_Nachname")
                .title("Ing.")
                .userId(String.valueOf(userId))
                .salutation("Herr")
                .workDescription("ARCHITEKT")
                .releaseDate("2020-01-01")
                .role(Role.USER.roleId)
                .active(true)
                .build();

        return employee;
    }

    private void assertEmployee(final Employee actual, final Employee employee) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(employee.role(), actual.role(), "role"),
                () -> Assertions.assertEquals(employee.userId(), actual.userId(), "userId"),
                () -> Assertions.assertEquals(employee.title(), actual.title(), "title"),
                () -> Assertions.assertEquals(employee.firstName(), actual.firstName(), "firstName"),
                () -> Assertions.assertEquals(employee.sureName(), actual.sureName(), "sureName"),
                () -> Assertions.assertEquals(employee.salutation(), actual.salutation(), "salutation"),
                () -> Assertions.assertEquals(employee.workDescription(), actual.workDescription(), "workDescription"),
                () -> Assertions.assertEquals(employee.releaseDate(), actual.releaseDate(), "releaseDate"),
                () -> Assertions.assertEquals(employee.active(), actual.active(), "isActive"));
    }
}
