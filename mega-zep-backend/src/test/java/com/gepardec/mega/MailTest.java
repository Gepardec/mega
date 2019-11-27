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
        String name = "Max";
        String eMail = "max.mustermann@gepardec.com";
        mailSender.sendMonthlyFriendlyReminder(name, eMail);
    }
}