package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MailSenderTest {

    @ConfigProperty(name = "quarkus.mailer.mock")
    boolean mailMockSetting;

    @Inject
    MailSender mailSender;

    @Inject
    MockMailbox mailbox;

    @Inject
    ApplicationConfig applicationConfig;

    @BeforeEach
    void init() {
        assertTrue(mailMockSetting, "This test can only run when mail mocking is true");
        mailbox.clear();
    }

    public static Stream<Arguments> emailsWithSubject() {
        return Stream.of(
                Arguments.of(Mail.EMPLOYEE_CHECK_PROJECTTIME, "UNIT-TEST: Friendly Reminder: Buchungen kontrollieren"),
                Arguments.of(Mail.PL_PROJECT_CONTROLLING, "UNIT-TEST: Reminder: Projekte kontrollieren und abrechnen"),
                Arguments.of(Mail.OM_CONTROL_EMPLOYEES_CONTENT, "UNIT-TEST: Reminder: Kontrolle Mitarbeiter"),
                Arguments.of(Mail.OM_CONTROL_PROJECTTIMES, "UNIT-TEST: Reminder: Administrative Projektzeiten kontrollieren (Monatsende)"),
                Arguments.of(Mail.OM_RELEASE, "UNIT-TEST: Reminder: Freigaben durchführen"),
                Arguments.of(Mail.OM_ADMINISTRATIVE, "UNIT-TEST: Reminder: Administratives"),
                Arguments.of(Mail.OM_SALARY, "UNIT-TEST: Reminder: Gehälter"),
                Arguments.of(Mail.COMMENT_CREATED, "UNIT-TEST: MEGA: Anmerkung von Thomas erhalten"),
                Arguments.of(Mail.COMMENT_CLOSED, "UNIT-TEST: MEGA: Anmerkung von Thomas erledigt")
        );
    }

    @ParameterizedTest
    @MethodSource("emailsWithSubject")
    void send_toEmployees_shouldContainEmpleyNotificationData(final Mail mail, final String subject) {
        final String to = "garfield.atHome@gmail.com";
        final Map<String, String> mailParameter = Map.of(
                MailParameter.CREATOR, "Thomas",
                MailParameter.RECIPIENT, "Herbert",
                MailParameter.COMMENT, "my comment");
        mailSender.send(mail, to, "Jamal", Locale.GERMAN, mailParameter, List.of("Thomas"));
        List<io.quarkus.mailer.Mail> sent = mailbox.getMessagesSentTo(to);
        assertAll(
                () -> assertEquals(1, sent.size()),
                () -> assertTrue(Mail.COMMENT_CLOSED.equals(mail) ?
                        sent.get(0).getHtml().startsWith("<p>Hallo " + "Herbert") :
                        sent.get(0).getHtml().startsWith("<p>Hallo " + "Jamal")),
                () -> assertEquals(subject, sent.get(0).getSubject()));
    }

    @Test
    void send_send10Mails_allMailsShouldBeInMailBox() {
        final String to = "garfield.atHome@gmail.com";
        for (int i = 0; i < 10; i++) {
            mailSender.send(Mail.EMPLOYEE_CHECK_PROJECTTIME, to, "Jamal", Locale.GERMAN);
        }
        List<io.quarkus.mailer.Mail> sent = mailbox.getMessagesSentTo(to);
        assertEquals(10, sent.size());
    }

    @Test
    void send_projectControllingMailContainsPlanrechnungUrl() {
        final String to = "garfield.atHome@gmail.com";
        mailSender.send(Mail.PL_PROJECT_CONTROLLING, to, "Jamal", Locale.GERMAN);
        List<io.quarkus.mailer.Mail> sent = mailbox.getMessagesSentTo(to);
        assertAll(
                () -> assertEquals(1, sent.size()),
                () -> assertTrue(sent.get(0).getHtml().contains(applicationConfig.getBudgetCalculationExcelUrlAsString()))
        );
    }
}
