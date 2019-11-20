package com.gepardec.mega;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.monthendreport.MonthendReport;
import com.gepardec.mega.monthendreport.TimeWarning;
import com.gepardec.mega.monthendreport.WarningType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

    @BeforeAll
    static void initTests() {
        googleUser.setEmail("max.mustermann@gepardec.com");

        try {
            final String output = objectMapper.writeValueAsString(googleUser);
            System.out.println(output);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void monthendReport_withUser_shouldShowWarnings() throws IOException {
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
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 5), WARNING_MORE_THAN_10_HOURS),
                //more than 10hours, but less than 10 for working
                () -> assertNull(timeWarningsByDay.get(LocalDate.of(2019, 11, 6))),

                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 7), WARNING_TOO_LESS_BREAK),

                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 11), WARNING_MORE_THAN_10_HOURS),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 13), WARNING_TOO_LATE_END),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 14), WARNING_TOO_LESS_REST),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 18), WARNING_TOO_LESS_BREAK, WARNING_MORE_THAN_10_HOURS),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 19), WARNING_TOO_LATE_END),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 20), WARNING_TOO_LESS_REST, WARNING_MORE_THAN_10_HOURS),
                () -> assertWarningTypeInWarningOfDay(timeWarningsByDay, LocalDate.of(2019, 11, 21), WARNING_TOO_LESS_REST, WARNING_TOO_EARLY_START)

        );
    }


    private static void assertWarningTypeInWarningOfDay(Map<LocalDate, List<WarningType>> warningsByDate, LocalDate date, WarningType... expectedWarningTypes) {
        assertTrue(warningsByDate.get(date).containsAll(expectedWarningTypes != null ? Arrays.asList(expectedWarningTypes) : new ArrayList<>(0)));
    }
}
