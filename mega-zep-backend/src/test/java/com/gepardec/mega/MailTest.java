package com.gepardec.mega;

import com.gepardec.mega.communication.MailSender;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
@Disabled
class MailTest {

    @Inject
    MailSender mailSender;

    @Test
    void sendMail() {
        mailSender.send();
    }
}
