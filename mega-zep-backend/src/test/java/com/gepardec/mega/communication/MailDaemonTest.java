package com.gepardec.mega.communication;

import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.gepardec.mega.communication.Reminder.OM_RELEASE;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class MailDaemonTest {
    @Inject
    MailDaemon mailDaemon;

    @Inject
    MockMailbox mailbox;

    @ConfigProperty(name = "mega.mail.reminder.pl")
    String projectLeadersMailAddresses;

    @ConfigProperty(name = "mega.mail.reminder.om")
    String omMailAddresses;

    @ConfigProperty(name = "quarkus.mailer.mock")
    boolean mailMockSetting;

    @ConfigProperty(name = "mega.mail.subjectPrefix", defaultValue = "")
    String subjectPrefix;

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    WorkerService workerService;


    @BeforeEach
    void init() {
        assertTrue(mailMockSetting);
        mailbox.clear();
    }


    @Test
    void getNameByMail_usualInput_shouldReturnFirstName() {
        assertEquals("John", MailDaemon.getNameByMail("john.doe@gmail.com"));
    }

    @Test
    void getNameByMail_EmptyInput_shouldReturnFirstName() {
        assertEquals("", MailDaemon.getNameByMail(""));
    }


    @Test
    void sendReminderToOm_mailAddressesAvailable_shouldSendMail() {
        List<String> mailAddresses = Arrays.asList(omMailAddresses.split("\\,"));
        mailDaemon.sendReminderToOm(OM_RELEASE);
        assertAll(
                () -> assertEquals(mailAddresses.size(), mailbox.getTotalMessagesSent()),
                () -> mailAddresses.forEach(mailAddress ->
                        assertEquals(buildSubject(notificationConfig.getOmReleaseSubject()), mailbox.getMessagesSentTo(mailAddresses.get(0)).get(0).getSubject()))
        );
    }

    @Test
    void sendReminderToPl_mailAddressesAvailable_shouldSendMail() {
        List<String> mailAddresses = Arrays.asList(projectLeadersMailAddresses.split("\\,"));
        mailDaemon.sendReminderToPl();
        assertAll(
                () -> assertEquals(mailAddresses.size(), mailbox.getTotalMessagesSent()),
                () -> mailAddresses.forEach(mailAddress ->
                        assertEquals(buildSubject(notificationConfig.getPlSubject()), mailbox.getMessagesSentTo(mailAddress).get(0).getSubject()))
        );
    }

    @Disabled("till userNotifaction is enabled")
    @Test
    void sendReminderToUser() {
        List<String> addresses = workerService.getAllActiveEmployees().stream()
                .map(MitarbeiterType::getEmail)
                .collect(Collectors.toList());

        mailDaemon.sendReminderToUser();
        assertAll(
                () -> assertEquals(addresses.size(), mailbox.getTotalMessagesSent()),
                () -> addresses.forEach(address ->
                        assertEquals(buildSubject(notificationConfig.getEmployeeSubject()), mailbox.getMessagesSentTo(address).get(0).getSubject()))
        );
    }

    private String buildSubject(final String subject){
        return subjectPrefix + subject;
    }


}
