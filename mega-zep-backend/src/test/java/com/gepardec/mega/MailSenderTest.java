package com.gepardec.mega;

import com.gepardec.mega.communication.MailSender;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
//@Disabled
//FIXME set system property surefire
class MailSenderTest {

    private static final String TO = "mario.brandmueller@gepardec.com";

    @ConfigProperty(name = "quarkus.mailer.mock")
    boolean mailMockSetting;

    @Inject
    MailSender mailSender;

    @Inject
    MockMailbox mailbox;

    @BeforeEach
    void init() {
        mailbox.clear();
    }

    @Test
    void sendMail_commonCase_shouldContainExpectedData() {

//        assertTrue(mailMockSetting);
        mailSender.sendMonthlyFriendlyReminder(TO, "Werner");
        List<Mail> sent = mailbox.getMessagesSentTo(TO);
        assertAll(
                () -> assertEquals(1, sent.size()),
                () -> assertTrue(sent.get(0).getHtml().startsWith("<p>Hallo Jamal")),
                () -> assertTrue(sent.get(0).getSubject().contains("Friendly Reminder"))
        );
    }

    @Test
    void sendMail_send100Mails_allMailsShouldBeInMailBox() {
        for (int i = 0; i < 100; i++) {
            mailSender.sendMonthlyFriendlyReminder(TO, "Jamal");
        }
        List<Mail> sent = mailbox.getMessagesSentTo(TO);
        assertEquals(100, sent.size());
    }
}