package com.gepardec.mega.notification.mail.dates;

import com.gepardec.mega.notification.mail.Mail;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Optional;

import static com.gepardec.mega.notification.mail.Mail.EMPLOYEE_CHECK_PROJECTTIME;
import static com.gepardec.mega.notification.mail.Mail.OM_ADMINISTRATIVE;
import static com.gepardec.mega.notification.mail.Mail.OM_CONTROL_EMPLOYEES_CONTENT;
import static com.gepardec.mega.notification.mail.Mail.OM_CONTROL_PROJECTTIMES;
import static com.gepardec.mega.notification.mail.Mail.OM_RELEASE;
import static com.gepardec.mega.notification.mail.Mail.OM_SALARY;
import static com.gepardec.mega.notification.mail.Mail.PL_PROJECT_CONTROLLING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
class BusinessDayCalculatorTest {

    @Inject
    BusinessDayCalculator businessDayCalculator;

    @Test
    void getEventForDate_firstDayOfMonthBusinessDay_shouldReturnUserCheckprojecttimes() {
        assertReminderEquals(EMPLOYEE_CHECK_PROJECTTIME, businessDayCalculator.getEventForDate(LocalDate.of(2019, 10, 1)));
    }

    @Test
    void getEventForDate_firstDayOfMonthNoBusinessDay_shouldReturnEmptyValue() {
        assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 1)));
    }

    @Test
    void getEventForDate_forthDayOfMonthIsFirstBusinessDay_shouldReturnCheckprojecttimes() {
        assertReminderEquals(EMPLOYEE_CHECK_PROJECTTIME, businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 4)));
    }

    @Test
    void getEventforDate_firstDayOfMonthIsWeekendDay_shouldReturnEmpty() {
        assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 1)));
    }

    @Test
    void getEventForDate_lastDayOfMonthIsWeekDay_shouldReturnOmControlProjecttimes() {
        assertReminderEquals(OM_CONTROL_PROJECTTIMES, businessDayCalculator.getEventForDate(LocalDate.of(2020, 4, 30)));
    }

    @Test
    void getEventForDate_lastDayOfMonthIsSaturday_shouldReturn_OmControlProjecttimesOnFriday() {
        assertReminderEquals(OM_CONTROL_PROJECTTIMES, businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 28)));
        assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 29)));
    }

    @Test
    void getEventForDate_adminstrativesOn15thDayOfMonthIsWorkday_shouldReturnReminderOn15th() {
        assertReminderEquals(OM_ADMINISTRATIVE, businessDayCalculator.getEventForDate(LocalDate.of(2020, 4, 15)));
    }

    @Test
    void getEventForDate_adminstrativesOn15thDayOfMonthIsNoWorkday_shouldReturnReminderOn15th() {
        assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 15)));
        assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 16)));
        assertReminderEquals(OM_ADMINISTRATIVE, businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 17)));
    }

    @Test
    void getEventforDate_differentDatesOfMonthNovember_shouldReturnCorrectReminder() {
        assertAll(
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 1))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 2))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 3))),
                () -> assertReminderEquals(EMPLOYEE_CHECK_PROJECTTIME, businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 4))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 5))),
                () -> assertReminderEquals(OM_CONTROL_EMPLOYEES_CONTENT, businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 6))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 7))),
                () -> assertReminderEquals(PL_PROJECT_CONTROLLING, businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 8))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 9))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 10))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 11))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 12))),
                () -> assertReminderEquals(OM_RELEASE, businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 13))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 14))),
                () -> assertReminderEquals(OM_ADMINISTRATIVE, businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 15))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 16))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 17))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 18))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 19))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 20))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 21))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 22))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 23))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 24))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 25))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 26))),
                () -> assertReminderEquals(OM_SALARY, businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 27))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 28))),
                () -> assertReminderEquals(OM_CONTROL_PROJECTTIMES, businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 29))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2019, 11, 30)))
        );
    }

    @Test
    void getEventforDate_differentDatesOfMonthFebruary_shouldReturnCorrectReminder() {
        assertAll(
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 1))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 2))),
                () -> assertReminderEquals(EMPLOYEE_CHECK_PROJECTTIME, businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 3))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 4))),
                () -> assertReminderEquals(OM_CONTROL_EMPLOYEES_CONTENT, businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 5))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 6))),
                () -> assertReminderEquals(PL_PROJECT_CONTROLLING, businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 7))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 8))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 9))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 10))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 11))),
                () -> assertReminderEquals(OM_RELEASE, businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 12))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 13))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 14))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 15))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 16))),
                () -> assertReminderEquals(OM_ADMINISTRATIVE, businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 17))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 18))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 19))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 20))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 21))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 22))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 23))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 24))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 25))),
                () -> assertReminderEquals(OM_SALARY, businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 26))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 27))),
                () -> assertReminderEquals(OM_CONTROL_PROJECTTIMES, businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 28))),
                () -> assertReminderEmpty(businessDayCalculator.getEventForDate(LocalDate.of(2020, 2, 29)))
        );
    }

    @Test
    void removeWorkingDaysFromNextMonth_positiveInt() {
        LocalDate localDate = businessDayCalculator.removeWorkingdaysFromNextMonth(LocalDate.of(2022, 1, 10), 20);
        assertThat(localDate).isEqualTo(LocalDate.of(2022, 1, 3));
    }

    @Test
    void removeWorkingDaysFromNextMonth_negativeInt() {
        LocalDate localDate = businessDayCalculator.removeWorkingdaysFromNextMonth(LocalDate.of(2022, 1, 10), -20);
        assertThat(localDate).isEqualTo(LocalDate.of(2022, 1, 3));
    }

    @Test
    void addWorkingDays_0_returnsInputDate() {
        LocalDate date = businessDayCalculator.addWorkingdays(LocalDate.of(2022, 1, 3), 0);
        assertThat(date).isEqualTo(LocalDate.of(2022, 1, 3));
    }

    @Test
    void addWorkingDays_20_returns20220201() {
        LocalDate date = businessDayCalculator.addWorkingdays(LocalDate.of(2022, 1, 3), 20);
        assertThat(date).isEqualTo(LocalDate.of(2022, 2, 1));
    }

    private void assertReminderEmpty(Optional<Mail> actualReminder) {
        assertThat(actualReminder).isEmpty();
    }

    private void assertReminderEquals(Mail expectedMail, Optional<Mail> actualReminder) {
        assertThat(actualReminder).get().isEqualTo(expectedMail);
    }
}
