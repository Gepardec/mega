package com.gepardec.mega.communication;

import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import javax.inject.Inject;

@QuarkusTest
@Disabled
public class MailDaemonTest {
    @Inject
    MailDaemon mailDaemon;

    @Inject
    MockMailbox mailbox;

    @ConfigProperty(name = "mega.mail.reminder.pl")
    String projectLeadersMailAddresses;

    @ConfigProperty(name = "mega.mail.reminder.om")
    String omMailAddresses;

    @Inject
    NotificationConfig notificationConfig;


    @BeforeEach
    void init() {
        mailbox.clear();
    }


//    @Test
//    void sendReminderToOm_mailAddressesAvailable_shouldSendMail() {
//        List<String> addresses = Arrays.asList(omMailAddresses.split(","));
//        mailDaemon.sendReminderToOm(OM_RELEASE);
//        assertAll(
//                () -> assertEquals(1, mailbox.getTotalMessagesSent()),
//                () -> assertEquals(notificationConfig.getSubjectByReminder(OM_RELEASE), mailbox.getMessagesSentTo(addresses.get(0)).get(0).getSubject())
//        );
//    }
//
//    @Test
//    void sendReminderToPl_mailAddressesAvailable_shouldSendMail() {
//        List<String> addresses = Arrays.asList(projectLeadersMailAddresses.split(","));
//        mailDaemon.sendReminderToPL();
//        assertAll(
//                () -> assertEquals(6, mailbox.getTotalMessagesSent()),
//                () -> addresses.forEach(address -> assertEquals(notificationConfig.getSubjectByReminder(PL_PROJECT_CONTROLLING), mailbox.getMessagesSentTo(address).get(0).getSubject()))
//        );
//    }
}
