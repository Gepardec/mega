package com.gepardec.mega.notification.mail;

import com.gepardec.mega.db.entity.User;
import com.gepardec.mega.db.repository.UserRepository;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static com.gepardec.mega.notification.mail.Reminder.OM_RELEASE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ReminderEmailSenderTest {

    @InjectMock
    private UserRepository userRepository;

    @Inject
    ReminderEmailSender reminderEmailSender;

    @Inject
    MockMailbox mailbox;

    @ConfigProperty(name = "mega.mail.reminder.pl")
    String projectLeadersMailAddresses;

    @ConfigProperty(name = "mega.mail.reminder.om")
    String omMailAddresses;

    @ConfigProperty(name = "quarkus.mailer.mock")
    boolean mailMockSetting;

    @BeforeEach
    void init() {
        assertTrue(mailMockSetting, "This test can only run when mail mocking is true");
        mailbox.clear();
    }


    @Test
    void getNameByMail_usualInput_shouldReturnFirstName() {
        assertEquals("John", ReminderEmailSender.getNameByMail("john.doe@gmail.com"));
    }

    @Test
    void getNameByMail_EmptyInput_shouldReturnFirstName() {
        assertEquals("", ReminderEmailSender.getNameByMail(""));
    }


    @Test
    void sendReminderToOm_mailAddressesAvailable_shouldSendMail() {
        List<String> mailAddresses = List.of(omMailAddresses.split("\\,"));
        reminderEmailSender.sendReminderToOm(OM_RELEASE);
        assertAll(
                () -> assertEquals(mailAddresses.size(), mailbox.getTotalMessagesSent()),
                () -> mailAddresses.forEach(mailAddress ->
                        assertEquals("UNIT-TEST: Reminder: Freigaben durchführen", mailbox.getMessagesSentTo(mailAddresses.get(0)).get(0)
                                .getSubject()))
        );
    }

    @Test
    void sendReminderToPl_mailAddressesAvailable_shouldSendMail() {
        List<String> mailAddresses = List.of(projectLeadersMailAddresses.split("\\,"));
        reminderEmailSender.sendReminderToPl();
        assertAll(
                () -> assertEquals(mailAddresses.size(), mailbox.getTotalMessagesSent()),
                () -> mailAddresses.forEach(mailAddress ->
                        assertEquals("UNIT-TEST: Reminder: Projekte kontrollieren und abrechnen", mailbox.getMessagesSentTo(mailAddress).get(0)
                                .getSubject()))
        );
    }

    @Test
    void sendReminderToUser() {
        final User user = new User();
        user.setEmail("thomas.herzog@gepardec.com");
        when(userRepository.listAll()).thenReturn(List.of(user));

        reminderEmailSender.sendReminderToUser();
        assertAll(
                () -> assertEquals(1, mailbox.getTotalMessagesSent()),
                () -> assertEquals("UNIT-TEST: Friendly Reminder: Buchungen kontrollieren", mailbox.getMessagesSentTo(user.getEmail()).get(0).getSubject())
        );
    }
}
