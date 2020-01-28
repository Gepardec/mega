package com.gepardec.mega.rest;

import com.gepardec.mega.GoogleTokenVerifierMock;
import com.gepardec.mega.SessionUserMock;
import com.gepardec.mega.WorkerServiceMock;
import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.rest.model.Employee;
import com.gepardec.mega.rest.model.JourneyWarning;
import com.gepardec.mega.rest.model.MonthlyReport;
import com.gepardec.mega.rest.model.TimeWarning;
import com.gepardec.mega.zep.service.impl.WorkerServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        final String email = "thomas.herzog@gepardec.com";
        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        googleTokenVerifierMock.setDelegate(googleIdTokenVerifier);
        sessionUserMock.init(email, "", Role.ADMINISTRATOR.roleId);
        workerServiceMock.setDelegate(workerService);
    }

    @Test
    void employee_withInvalidEmail_returnsNotFound() {
        given().contentType(ContentType.TEXT)
                .body("hacker@gmail.com")
                .post("/worker/employee")
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void employees_withPOST_returnsMethodNotAllowed() {
        given().post("/worker/employees")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void employees_withValidRequest_returnsActiveEmployees() {
        final List<MitarbeiterType> mitarbeiter = IntStream.range(1, 10).mapToObj(this::createMitarbeiter).collect(Collectors.toList());
        Mockito.when(workerService.getAllActiveEmployees()).thenReturn(mitarbeiter);
        workerServiceMock.setDelegate(workerService);

        final List<Employee> actual = given().get("/worker/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(mitarbeiter.size(), actual.size());
        for (int i = 0; i < mitarbeiter.size(); i++) {
            assertEmployee(actual.get(i), mitarbeiter.get(i));
        }
    }

    @Test
    void employeesUpdate_withPOST_returnsMethodNotAllowed() {
        given().contentType(ContentType.JSON)
                .post("/worker/employees")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void employeesUpdate_withEmptyBody_returnsBadRequest() {
        given().contentType(ContentType.JSON)
                .put("/worker/employees")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void employeesUpdate_withEmptyArray_returnsBadRequest() {
        given().contentType(ContentType.JSON)
                .body(new ArrayList<>())
                .put("/worker/employees")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void employeesUpdate_withInvalidEmployees_returnsInvalidEmails() {
        final List<Employee> employees = IntStream.range(1, 11).mapToObj(this::createEmployee).collect(Collectors.toList());
        final List<String> expected = employees.subList(0, 5).stream().map(Employee::getEmail).collect(Collectors.toList());
        Mockito.when(workerService.updateEmployeesReleaseDate(Mockito.anyMap())).thenReturn(expected);
        workerServiceMock.setDelegate(workerService);

        final List<String> actual = given().contentType(ContentType.JSON)
                .body(employees)
                .put("/worker/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(new TypeRef<>() {
                });

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    void employeesUpdate_withAllValidEmployees_returnsNothing() {
        final List<Employee> employees = IntStream.range(1, 11).mapToObj(this::createEmployee).collect(Collectors.toList());
        Mockito.when(workerService.updateEmployeesReleaseDate(Mockito.anyMap())).thenReturn(Collections.emptyList());
        workerServiceMock.setDelegate(workerService);

        final List<String> actual = given().contentType(ContentType.JSON)
                .body(employees)
                .put("/worker/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(new TypeRef<>() {
                });

        Assertions.assertTrue(actual.isEmpty());
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
        final MitarbeiterType mitarbeiter = createMitarbeiter(0);
        final com.gepardec.mega.monthlyreport.MonthlyReport expected = createZepMonthlyReport(mitarbeiter);
        Mockito.when(workerService.getMonthendReportForUser(Mockito.anyString())).thenReturn(expected);

        final MonthlyReport actual = given().contentType(ContentType.JSON)
                .get("/worker/monthendreports")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(MonthlyReport.class);

        assertEmployee(actual.getEmployee(), mitarbeiter);
        assertTimeWarnings(expected.getTimeWarnings(), actual.getTimeWarnings());
        assertJourneyWarnings(expected.getJourneyWarnings(), actual.getJourneyWarnings());
    }

    private com.gepardec.mega.monthlyreport.MonthlyReport createZepMonthlyReport(final MitarbeiterType mitarbeiter) {
        final List<com.gepardec.mega.monthlyreport.warning.TimeWarning> timeWarnings = Collections.singletonList(com.gepardec.mega.monthlyreport.warning.TimeWarning.of(LocalDate.now(), 0.0, 0.0, 0.0));
        final List<com.gepardec.mega.monthlyreport.journey.JourneyWarning> journeyWarnings = Collections.singletonList(new com.gepardec.mega.monthlyreport.journey.JourneyWarning(LocalDate.now(), Collections.singletonList("WARNING")));

        return new com.gepardec.mega.monthlyreport.MonthlyReport(timeWarnings, journeyWarnings, mitarbeiter);
    }

    private MitarbeiterType createMitarbeiter(final int userId) {
        final MitarbeiterType mitarbeiter = new MitarbeiterType();
        final String name = "Thomas_" + userId;

        mitarbeiter.setEmail(name + "@gepardec.com");
        mitarbeiter.setVorname(name);
        mitarbeiter.setNachname(name + "_Nachname");
        mitarbeiter.setTitel("Ing.");
        mitarbeiter.setUserId(String.valueOf(userId));
        mitarbeiter.setAnrede("Herr");
        mitarbeiter.setPreisgruppe("ARCHITEKT");
        mitarbeiter.setFreigabedatum("2020-01-01");
        mitarbeiter.setRechte(Role.USER.roleId);

        return mitarbeiter;
    }

    private Employee createEmployee(final int userId) {
        final Employee mitarbeiter = new Employee();
        final String name = "Thomas_" + userId;

        mitarbeiter.setEmail(name + "@gepardec.com");
        mitarbeiter.setFirstName(name);
        mitarbeiter.setSureName(name + "_Nachname");
        mitarbeiter.setTitle("Ing.");
        mitarbeiter.setUserId(String.valueOf(userId));
        mitarbeiter.setSalutation("Herr");
        mitarbeiter.setWorkDescription("ARCHITEKT");
        mitarbeiter.setReleaseDate("2020-01-01");
        mitarbeiter.setRole(Role.USER.roleId);

        return mitarbeiter;
    }

    private void assertEmployee(final Employee actual, final MitarbeiterType mitarbeiter) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(mitarbeiter.getRechte(), actual.getRole(), "role"),
                () -> Assertions.assertEquals(mitarbeiter.getUserId(), actual.getUserId(), "userId"),
                () -> Assertions.assertEquals(mitarbeiter.getTitel(), actual.getTitle(), "title"),
                () -> Assertions.assertEquals(mitarbeiter.getVorname(), actual.getFirstName(), "firstName"),
                () -> Assertions.assertEquals(mitarbeiter.getNachname(), actual.getSureName(), "sureName"),
                () -> Assertions.assertEquals(mitarbeiter.getAnrede(), actual.getSalutation(), "salutation"),
                () -> Assertions.assertEquals(mitarbeiter.getPreisgruppe(), actual.getWorkDescription(), "workDescription"),
                () -> Assertions.assertEquals(mitarbeiter.getFreigabedatum(), actual.getReleaseDate()));
    }

    private void assertJourneyWarnings(List<com.gepardec.mega.monthlyreport.journey.JourneyWarning> expected, List<JourneyWarning> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertJourneyWarning(expected.get(i), actual.get(i));
        }
    }

    private void assertJourneyWarning(com.gepardec.mega.monthlyreport.journey.JourneyWarning expected, JourneyWarning actual) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getDate(), actual.getDate(), "date"),
                () -> Assertions.assertIterableEquals(expected.getWarnings(), actual.getWarnings(), "warnings")
        );
    }

    private void assertTimeWarnings(List<com.gepardec.mega.monthlyreport.warning.TimeWarning> expected, List<TimeWarning> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertTimeWarning(expected.get(i), actual.get(i));
        }
    }

    private void assertTimeWarning(com.gepardec.mega.monthlyreport.warning.TimeWarning expected, TimeWarning actual) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getDate(), actual.getDate(), "date"),
                () -> Assertions.assertEquals(expected.getExcessWorkTime(), actual.getExcessWorkTime(), "exessWorkTime"),
                () -> Assertions.assertEquals(expected.getMissingBreakTime(), actual.getMissingBreakTime(), "missingBreakTime"),
                () -> Assertions.assertEquals(expected.getMissingRestTime(), actual.getMissingRestTime(), "missingRestTime")
        );
    }
}
