package com.gepardec.mega;

import com.gepardec.mega.communication.MailSender;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class MailTest {

    @Inject
    MailSender mailSender;

    @Test
    void sendMail() {
        mailSender.sendMonthlyFriendlyReminder("megagepardec@gmail.com", "mario");
    }
}