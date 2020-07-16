package com.gepardec.mega.rest;

import com.gepardec.mega.GoogleTokenVerifierMock;
import com.gepardec.mega.SessionUserMock;
import com.gepardec.mega.WorkerServiceMock;
import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.domain.MonthlyReport;
import com.gepardec.mega.domain.JourneyWarning;
import com.gepardec.mega.domain.TimeWarning;
import com.gepardec.mega.domain.Employee;
import com.gepardec.mega.util.EmployeeTestUtil;
import com.gepardec.mega.service.impl.WorkerServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

@ExtendWith(MockitoExtension.class)
@QuarkusTest
public class WorkerResourceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private GoogleIdToken googleIdToken;

    @Mock
    private WorkerServiceImpl workerService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @Inject
    SessionUserMock sessionUserMock;

    @Inject
    WorkerServiceMock workerServiceMock;

    @Inject
    GoogleTokenVerifierMock googleTokenVerifierMock;

    @BeforeEach
    void beforeEach() throws Exception {
        final String userId = "1337-thomas.herzog";
        final String email = "thomas.herzog@gepardec.com";
        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        googleTokenVerifierMock.setDelegate(googleIdTokenVerifier);
        sessionUserMock.init(userId, email, "", Role.ADMINISTRATOR.roleId);
        workerServiceMock.setDelegate(workerService);
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
        final Employee employee = EmployeeTestUtil.createEmployee(0);
        final MonthlyReport expected = createZepMonthlyReport(employee);
        Mockito.when(workerService.getMonthendReportForUser(Mockito.anyString())).thenReturn(expected);

        final MonthlyReport actual = given().contentType(ContentType.JSON)
                .get("/worker/monthendreports")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(MonthlyReport.class);

        EmployeeTestUtil.assertEmployee(actual.getEmployee(), employee);
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
}
