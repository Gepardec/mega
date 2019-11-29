package com.gepardec.mega;

import com.gepardec.mega.communication.MailSender;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MailSenderTest {

    private static final String TO = "megagepardec@gmail.com";

    @Inject
    MailSender mailSender;

    @Inject
    MockMailbox mailbox;

    @BeforeEach
    void init() {
        mailbox.clear();
    }

    @Test
    void sendMail() {
        mailSender.sendMonthlyFriendlyReminder(TO, "Jamal");
        List<Mail> sent = mailbox.getMessagesSentTo(TO);
        assertAll(
                () -> assertEquals(1, sent.size()),
                () -> assertTrue(sent.get(0).getHtml().startsWith("<p>He Jamal")),
                () -> assertTrue(sent.get(0).getSubject().contains("Friendly Reminder"))
        );
    }
}