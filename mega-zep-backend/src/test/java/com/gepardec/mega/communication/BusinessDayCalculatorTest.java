package com.gepardec.mega.communication;

import com.gepardec.mega.communication.dates.BusinessDayCalculator;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Optional;

import static com.gepardec.mega.communication.Reminder.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class BusinessDayCalculatorTest {


    @Inject
    BusinessDayCalculator businessDayCalculator;

    @Test
    void getEventForDate_firstDayOfMonthBusinessDay_shouldReturnUserCheckprojecttimes() {
        assertReminderForDate(EMPLOYEE_CHECK_PROJECTTIME, 2019, 10, 1);
    }

    @Test
    void getEventForDate_firstDayOfMonthNoBusinessDay_shouldReturnEmptyValue() {
        assertEmptyForGetEventForDate(2019, 11, 1);
    }

    @Test
    void getEventForDate_forthDayOfMonthIsFirstBusinessDay_shouldReturnCheckprojecttimes() {
        assertReminderForDate(EMPLOYEE_CHECK_PROJECTTIME, 2019, 11, 4);
    }

    @Test
    void getEventforDate_firstDayOfMonthIsWeekendDay_shouldReturnEmpty() {
        assertEmptyForGetEventForDate(2019, 11, 1);
    }


    @Test
    void getEventforDate_firstDayOfMonthIsWeekendday_shouldReturnEmpty() {
        assertEmptyForGetEventForDate(2019, 11, 1);
    }


    @Test
    void getEventforDate_differentDatesOfWholeMoth_shouldReturnCorrectReminder() {
        assertAll(
                () -> assertEmptyForGetEventForDate(2019, 11, 1),
                () -> assertEmptyForGetEventForDate(2019, 11, 2),
                () -> assertEmptyForGetEventForDate(2019, 11, 3),
                () -> assertReminderForDate(EMPLOYEE_CHECK_PROJECTTIME, 2019, 11, 4),
                () -> assertEmptyForGetEventForDate(2019, 11, 5),
                () -> assertReminderForDate(OM_CHECK_EMPLOYEES_CONTENT, 2019, 11, 6),
                () -> assertEmptyForGetEventForDate(2019, 11, 7),
                () -> assertEmptyForGetEventForDate(2019, 11, 8),
                () -> assertEmptyForGetEventForDate(2019, 11, 9),
                () -> assertEmptyForGetEventForDate(2019, 11, 10),
                () -> assertEmptyForGetEventForDate(2019, 11, 11),
                () -> assertEmptyForGetEventForDate(2019, 11, 12),
                () -> assertReminderForDate(OM_RELEASE, 2019, 11, 13),
                () -> assertEmptyForGetEventForDate(2019, 11, 14),
                () -> assertEmptyForGetEventForDate(2019, 11, 15),
                () -> assertEmptyForGetEventForDate(2019, 11, 16),
                () -> assertEmptyForGetEventForDate(2019, 11, 17),
                () -> assertEmptyForGetEventForDate(2019, 11, 18),
                () -> assertEmptyForGetEventForDate(2019, 11, 19),
                () -> assertEmptyForGetEventForDate(2019, 11, 20),
                () -> assertEmptyForGetEventForDate(2019, 11, 21),
                () -> assertReminderForDate(OM_SALARY_CHARGING, 2019, 11, 22),
                () -> assertEmptyForGetEventForDate(2019, 11, 23),
                () -> assertEmptyForGetEventForDate(2019, 11, 24),
                () -> assertEmptyForGetEventForDate(2019, 11, 25),
                () -> assertEmptyForGetEventForDate(2019, 11, 26),
                () -> assertReminderForDate(OM_SALARY_TRANSFER, 2019, 11, 27),
                () -> assertEmptyForGetEventForDate(2019, 11, 28),
                () -> assertEmptyForGetEventForDate(2019, 11, 29),
                () -> assertEmptyForGetEventForDate(2019, 11, 30)
        );
    }


    private void assertEmptyForGetEventForDate(int year, int month, int day) {
        assertEquals(Optional.empty(), businessDayCalculator.getEventForDate(LocalDate.of(year, month, day)));
    }


    private void assertReminderForDate(Reminder reminder, int year, int month, int day) {
        assertEquals(Optional.of(reminder), businessDayCalculator.getEventForDate(LocalDate.of(year, month, day)));
    }

}
