package com.gepardec.mega;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.monthlyreport.journey.JourneyWarning;
import com.gepardec.mega.monthlyreport.warning.TimeWarning;
import com.gepardec.mega.monthlyreport.warning.WarningConfig;
import com.gepardec.mega.utils.DateUtils;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

// FIXME: Not working test
@QuarkusTest
@Disabled
public class MonthlyReportTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Object googleUser = new Object();

    @Inject
    WarningConfig warningConfig;


    void initWithUserAndReleaseDate(String email, LocalDate releaseDate) throws IOException {
//        googleUser.setEmail(email);
        final String response = given().contentType(MediaType.APPLICATION_JSON)
                .body(googleUser)
                .post("/user/login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().asString();

        final MitarbeiterType mitarbeiterType = objectMapper.readValue(response, MitarbeiterType.class);
        mitarbeiterType.setFreigabedatum(DateUtils.dateToString(releaseDate));

        given().contentType(MediaType.APPLICATION_JSON)
                .body(mitarbeiterType)
                .put("/worker/employee/update")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }

    @Disabled
    @Test
    void monthendReport_withUserGiselaGewissenhaft_shouldShowJourneyWarnings() throws IOException {
        initWithUserAndReleaseDate("gisela.gewissenhaft@gepardec.com", LocalDate.of(2019, 10, 31));

        final String response = given()
                .contentType(ContentType.JSON)
                .body(googleUser)
                .post("/worker/employee/monthendReport")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asString();

        final MonthlyReport monthlyReport = objectMapper.readValue(response, MonthlyReport.class);

        Map<LocalDate, List<String>> journeyWarningsByDay = monthlyReport.getJourneyWarnings().stream()
                .sorted(Comparator.comparing(JourneyWarning::getDate))
                .collect(Collectors.toMap(JourneyWarning::getDate,
                        journeyWarning -> new ArrayList(journeyWarning.getWarnings()), (v1, v2) -> v1, LinkedHashMap::new));

        assertAll(
                () -> assertEquals(4, monthlyReport.getJourneyWarnings().size()),
                () -> assertWarningTypeInWarningOfDay(journeyWarningsByDay, LocalDate.of(2019, 11, 4), warningConfig.getMissingJourneyToAim()),
                () -> assertWarningTypeInWarningOfDay(journeyWarningsByDay, LocalDate.of(2019, 11, 12), warningConfig.getMissingJourneyToAim()),
                () -> assertWarningTypeInWarningOfDay(journeyWarningsByDay, LocalDate.of(2019, 11, 14), warningConfig.getMissingJourneyToAim(), warningConfig.getMissingJourneyBack()),
                () -> assertWarningTypeInWarningOfDay(journeyWarningsByDay, LocalDate.of(2019, 11, 18), warningConfig.getMissingJourneyBack())
        );
    }

    @Disabled
    @Test
    void monthendReport_withUserMaxMustermann_shouldShowTimeWarnings() throws IOException {
        initWithUserAndReleaseDate("max.mustermann@gepardec.com", LocalDate.of(2019, 10, 31));
        final String response = given()
                .contentType(ContentType.JSON)
                .body(googleUser)
                .post("/worker/employee/monthendReport")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asString();

        final MonthlyReport monthlyReport = objectMapper.readValue(response, MonthlyReport.class);


        Map<LocalDate, TimeWarning> timeWarningsByDay = monthlyReport.getTimeWarnings()
                .stream().collect(Collectors.toMap(TimeWarning::getDate, timeWarning -> timeWarning, (v1, v2) -> v1, LinkedHashMap::new));

        assertAll("Errors in timeWarning-Tests: ",
                () -> assertEquals(9, monthlyReport.getTimeWarnings().size()),
                //more than 10 hourse work, but also journey-time
                () -> assertTimeWarning(timeWarningsByDay, TimeWarning.of(LocalDate.of(2019, 11, 5), null, null, 0.25)),
                //more than 10hours, but less than 10 for working
                () -> assertNull(timeWarningsByDay.get(LocalDate.of(2019, 11, 6))),
                () -> assertTimeWarning(timeWarningsByDay, TimeWarning.of(LocalDate.of(2019, 11, 7), null, 0.5, null)),
                () -> assertTimeWarning(timeWarningsByDay, TimeWarning.of(LocalDate.of(2019, 11, 11), null, null, 1.5)),
//              () -> assertTimeWarning(timeWarningsByDay, TimeWarning.of(LocalDate.of(2019, 11, 13), WARNING_TIME_TOO_LATE_END),
                () -> assertTimeWarning(timeWarningsByDay, TimeWarning.of(LocalDate.of(2019, 11, 14), 3.5, null, null)),
                () -> assertTimeWarning(timeWarningsByDay, TimeWarning.of(LocalDate.of(2019, 11, 18), null, 0.5, 6d)),
//              () -> assertTimeWarning(timeWarningsByDay, TimeWarning.of(LocalDate.of(2019, 11, 19), WARNING_TIME_TOO_LATE_END),
                () -> assertTimeWarning(timeWarningsByDay, TimeWarning.of(LocalDate.of(2019, 11, 20), 4.75, null, 5.5)),
                () -> assertTimeWarning(timeWarningsByDay, TimeWarning.of(LocalDate.of(2019, 11, 21), 4.0, null, null))
        );
    }


    private void assertWarningTypeInWarningOfDay(Map<LocalDate, List<String>> warningsByDate, LocalDate date, String... expectedWarnings) {
        assertTrue(warningsByDate.get(date).containsAll(expectedWarnings != null ? Arrays.asList(expectedWarnings) : new ArrayList<>(0)));
    }

    private void assertTimeWarning(Map<LocalDate, TimeWarning> timeWarningByDay, TimeWarning expectedTimeWarning) {
        TimeWarning actualTimeWarning = timeWarningByDay.get(expectedTimeWarning.getDate());
        assertAll(
                () -> assertEquals(actualTimeWarning.getExcessWorkTime(), expectedTimeWarning.getExcessWorkTime()),
                () -> assertEquals(actualTimeWarning.getMissingBreakTime(), expectedTimeWarning.getMissingBreakTime()),
                () -> assertEquals(actualTimeWarning.getMissingRestTime(), expectedTimeWarning.getMissingRestTime())
        );
    }
}