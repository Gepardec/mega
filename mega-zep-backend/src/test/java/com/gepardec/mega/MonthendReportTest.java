package com.gepardec.mega;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.monthendreport.JourneyWarning;
import com.gepardec.mega.monthendreport.MonthendReport;
import com.gepardec.mega.monthendreport.TimeWarning;
import com.gepardec.mega.monthendreport.WarningType;
import com.gepardec.mega.utils.DateUtils;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.gepardec.mega.monthendreport.WarningType.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class MonthendReportTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static GoogleUser googleUser = new GoogleUser();

    static void initWithUserAndReleaseDate(String email, LocalDate releaseDate) throws IOException {
        googleUser.setEmail(email);
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

        final MonthendReport monthendReport = objectMapper.readValue(response, MonthendReport.class);

        Map<LocalDate, List<WarningType>> journeyWarningsByDay = monthendReport.getJourneyWarnings().stream()
                .sorted(Comparator.comparing(JourneyWarning::getDate))
                .collect(Collectors.toMap(JourneyWarning::getDate, journeyWarning -> new ArrayList(journeyWarning.getWarnings()),
                        (v1, v2) -> v1,
                        LinkedHashMap::new));

        assertAll(
                () -> assertEquals(4, monthendReport.getJourneyWarnings().size()),
                () -> assertWarningTypeInWarningOfDay(journeyWarningsByDay, LocalDate.of(2019, 11, 4), WARNING_JOURNEY_TO_AIM_MISSING),
                () -> assertWarningTypeInWarningOfDay(journeyWarningsByDay, LocalDate.of(2019, 11, 12), WARNING_JOURNEY_TO_AIM_MISSING),
                () -> assertWarningTypeInWarningOfDay(journeyWarningsByDay, LocalDate.of(2019, 11, 14), WARNING_JOURNEY_TO_AIM_MISSING, WARNING_JOURNEY_BACK_MISSING),
                () -> assertWarningTypeInWarningOfDay(journeyWarningsByDay, LocalDate.of(2019, 11, 18), WARNING_JOURNEY_BACK_MISSING)
        );
    }

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

        final MonthendReport monthendReport = objectMapper.readValue(response, MonthendReport.class);

        Map<LocalDate, List<WarningType>> timeWarningsByDay = monthendReport.getTimeWarnings().stream()
                .sorted(Comparator.comparing(TimeWarning::getDate))
                .collect(Collectors.toMap(TimeWarning::getDate,
                        TimeWarning::getWarnings,
                        (v1, v2) -> v1,
                        LinkedHashMap::new));

        assertAll("Errors in timeWarning-Tests: ",
                () -> assertEquals(9, monthendReport.getTimeWarnings().size()),
                //more than 10 hourse work, but also journey-time
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 5), WARNING_TIME_MORE_THAN_10_HOURS),
                //more than 10hours, but less than 10 for working
                () -> assertNull(timeWarningsByDay.get(LocalDate.of(2019, 11, 6))),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 7), WARNING_TIME_TOO_LESS_BREAK),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 11), WARNING_TIME_MORE_THAN_10_HOURS),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 13), WARNING_TIME_TOO_LATE_END),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 14), WARNING_TIME_TOO_LESS_REST),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 18), WARNING_TIME_TOO_LESS_BREAK, WARNING_TIME_MORE_THAN_10_HOURS),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 19), WARNING_TIME_TOO_LATE_END),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 20), WARNING_TIME_TOO_LESS_REST, WARNING_TIME_MORE_THAN_10_HOURS),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 21), WARNING_TIME_TOO_LESS_REST, WARNING_TIME_TOO_EARLY_START)
        );
    }


    private static void assertWarningTypeInWarningOfDay(Map<LocalDate, List<WarningType>> warningsByDate, LocalDate date, WarningType... expectedWarningTypes) {
        assertTrue(warningsByDate.get(date).containsAll(expectedWarningTypes != null ? Arrays.asList(expectedWarningTypes) : new ArrayList<>(0)));
    }
}
