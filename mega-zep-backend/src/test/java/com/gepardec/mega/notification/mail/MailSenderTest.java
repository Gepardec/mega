package com.gepardec.mega.notification.mail;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MailSenderTest {

    private static final String TO = "garfield.atHome@gmail.com";

    @ConfigProperty(name = "quarkus.mailer.mock")
    boolean mailMockSetting;

    @Inject
    MailSender mailSender;

    @Inject
    MockMailbox mailbox;

    @BeforeEach
    void init() {
        assertTrue(mailMockSetting, "This test can only run when mail mocking is true");
        mailbox.clear();
    }

    @Test
    void sendMail_toEmployees_shouldContainEmpleyNotificationData() {
        testMailFor("Jamal", Reminder.EMPLOYEE_CHECK_PROJECTTIME, "UNIT-TEST: Friendly Reminder: Buchungen kontrollieren");
    }

    @Test
    void sendMail_toPL_shouldCountainPlData() {
        testMailFor("Simba", Reminder.PL_PROJECT_CONTROLLING, "UNIT-TEST: Reminder: Projekte kontrollieren und abrechnen");
    }

    @Test
    void sendMail_toOmControlContent() {
        testMailFor("Garfield", Reminder.OM_CONTROL_EMPLOYEES_CONTENT, "UNIT-TEST: Reminder: Kontrolle Mitarbeiter");
    }

    @Test
    void sendMail_toOmControlProjecttimes() {
        testMailFor("Dagobert", Reminder.OM_CONTROL_PROJECTTIMES, "UNIT-TEST: Reminder: Administrative Projektzeiten kontrollieren (Monatsende)");
    }


    @Test
    void sendMail_toOmRelease() {
        testMailFor("Pacman", Reminder.OM_RELEASE, "UNIT-TEST: Reminder: Freigaben durchführen");
    }

    @Test
    void sendMail_toOmAdministrative() {
        testMailFor("ALF", Reminder.OM_ADMINISTRATIVE, "UNIT-TEST: Reminder: Administratives");
    }

    @Test
    void sendMail_toOmSalary() {
        testMailFor("Spiderman", Reminder.OM_SALARY, "UNIT-TEST: Reminder: Gehälter");
    }


    @Test
    void sendMail_send100Mails_allMailsShouldBeInMailBox() {
        for (int i = 0; i < 100; i++) {
            mailSender.sendReminder(TO, "Jamal", Reminder.EMPLOYEE_CHECK_PROJECTTIME, Locale.GERMAN);
        }
        List<Mail> sent = mailbox.getMessagesSentTo(TO);
        assertEquals(100, sent.size());
    }

    private void testMailFor(String name, Reminder reminder, String subject) {
        mailSender.sendReminder(TO, name, reminder, Locale.GERMAN);
        List<Mail> sent = mailbox.getMessagesSentTo(TO);
        assertAll(
                () -> assertEquals(1, sent.size()),
                () -> assertTrue(sent.get(0).getHtml().startsWith("<p>Hallo " + name)),
                () -> assertEquals(sent.get(0).getSubject(), subject));
    }

    // TODO: Add tests for each Reminder if the template and subject can be loaded. Mock NotificationConfig for that
}
