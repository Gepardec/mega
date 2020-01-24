package com.gepardec.mega.rest;

import com.gepardec.mega.GoogleTokenVerifierMock;
import com.gepardec.mega.SessionUserMock;
import com.gepardec.mega.WorkerServiceMock;
import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.rest.model.Employee;
import com.gepardec.mega.zep.service.impl.WorkerServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import de.provantis.zep.MitarbeiterType;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

@ExtendWith(MockitoExtension.class)
@QuarkusTest
@Disabled
public class WorkerResourceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private GoogleIdToken googleIdToken;

    @Mock
    private WorkerServiceImpl workerService;

    @Inject
    SessionUserMock sessionUserMock;

    @Inject
    WorkerServiceMock workerServiceMock;

    @Inject
    GoogleTokenVerifierMock googleTokenVerifierMock;

    @BeforeEach
    void beforeEach() {
        final String email = "thomas.herzog@gepardec.com";
        googleTokenVerifierMock.setAnswer((idToken) -> googleIdToken);
        sessionUserMock.init(email, "", Role.ADMINISTRATOR.roleId);
    }

    @Test
    void employee_withGET_returnsMethodNotAllowed() {
        given().contentType(ContentType.TEXT)
                .get("/worker/employee")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void employee_withEmptyEmail_returnsBadRequest() {
        given().contentType(ContentType.TEXT)
                .post("/worker/employee")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void employee_withInvalidEmail_returnsNotFound() {
        given().contentType(ContentType.TEXT)
                .body("hacker@gmail.com")
                .post("/worker/employee")
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void employee_withValidEmail_returnsEmployee() {
        final MitarbeiterType mitarbeiter = createMitarbeiter("Thomas");
        Mockito.when(workerService.getEmployee(mitarbeiter.getEmail())).thenReturn(mitarbeiter);
        workerServiceMock.setDelegate(workerService);

        final Employee actual = given().contentType(ContentType.TEXT)
                .body(mitarbeiter.getEmail())
                .post("/worker/employee")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Employee.class);

        assertEmployee(actual, mitarbeiter);
    }


    @Test
    void employees_withGET_returnsMethodNotAllowed() {
        given().get("/worker/employees")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void employees_withValidRequest_returnsActiveEmployees() {
        final List<MitarbeiterType> mitarbeiter = IntStream.range(1, 10).mapToObj(i -> createMitarbeiter("Thomas_" + i)).collect(Collectors.toList());
        Mockito.when(workerService.getAllActiveEmployees()).thenReturn(mitarbeiter);
        workerServiceMock.setDelegate(workerService);

        final List<Employee> actual = given().post("/worker/employees")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(new TypeRef<List<Employee>>() {});

        Assertions.assertEquals(mitarbeiter.size(), actual.size());
        for (int i = 0; i < mitarbeiter.size(); i++) {
            assertEmployee(actual.get(i), mitarbeiter.get(i));
        }
    }

    // TODO:We need to refactore the Monthly report to a value model, contains to much löogic and is returned as a response.
//    @Test
//    void employeeMonthendReport_withLoggedUserEmail_returnsMonthlyReport() {
//    }

    private MitarbeiterType createMitarbeiter(final String name) {
        final MitarbeiterType mitarbeiter = new MitarbeiterType();
        mitarbeiter.setEmail(name + "@gepardec.com");
        mitarbeiter.setVorname(name);
        mitarbeiter.setNachname(name + "_Nachname");
        mitarbeiter.setTitel("Ing.");
        mitarbeiter.setUserId("1");
        mitarbeiter.setAnrede("Herr");
        mitarbeiter.setPreisgruppe("ARCHITEKT");
        mitarbeiter.setFreigabedatum("2020-01-01");
        mitarbeiter.setRechte(Role.USER.roleId);

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
}
