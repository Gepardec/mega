package com.gepardec.mega.notification.mail;

import com.gepardec.mega.domain.Role;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.user.UserService;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.gepardec.mega.notification.mail.Reminder.OM_RELEASE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ReminderEmailSenderTest {

    @InjectMock
    private UserService userService;

    @Inject
    ReminderEmailSender reminderEmailSender;

    @Inject
    MockMailbox mailbox;

    @ConfigProperty(name = "quarkus.mailer.mock")
    boolean mailMockSetting;

    @BeforeEach
    void init() {
        assertTrue(mailMockSetting, "This test can only run when mail mocking is true");
        mailbox.clear();
    }

    @Test
    void sendReminderToOm_whenNoUsers_thenNoMailSend() {
        when(userService.findByRoles(eq(Role.OFFICE_MANAGEMENT))).thenReturn(List.of());

        reminderEmailSender.sendReminderToOm(OM_RELEASE);

        assertEquals(0, mailbox.getTotalMessagesSent());
    }

    @Test
    void sendReminderToOm_whenUser_thenSendsMail() {
        when(userService.findByRoles(Role.OFFICE_MANAGEMENT)).thenReturn(List.of(userFor("thomas.herzog@gepardec.com", "Thomas")));

        reminderEmailSender.sendReminderToOm(OM_RELEASE);
        assertAll(
                () -> assertEquals(1, mailbox.getTotalMessagesSent()),
                () -> assertEquals("UNIT-TEST: Reminder: Freigaben durchführen",
                        mailbox.getMessagesSentTo("thomas.herzog@gepardec.com").get(0).getSubject())
        );
    }

    @Test
    void sendReminderToPl_whenNoUsers_thenNoMailSend() {
        when(userService.findActiveUsers()).thenReturn(List.of());

        reminderEmailSender.sendReminderToPl();

        assertEquals(0, mailbox.getTotalMessagesSent());
    }

    @Test
    void sendReminderToPl_whenUser_thenSendsMail() {
        when(userService.findByRoles(Role.PROJECT_LEAD)).thenReturn(List.of(userFor("thomas.herzog@gepardec.com", "Thomas")));

        reminderEmailSender.sendReminderToPl();
        assertAll(
                () -> assertEquals(1, mailbox.getTotalMessagesSent()),
                () -> assertEquals("UNIT-TEST: Reminder: Projekte kontrollieren und abrechnen",
                        mailbox.getMessagesSentTo("thomas.herzog@gepardec.com").get(0).getSubject())
        );
    }

    @Test
    void sendReminderToUser_whenNoUsers_thenNoMailSend() {
        when(userService.findActiveUsers()).thenReturn(List.of());

        reminderEmailSender.sendReminderToUser();

        assertEquals(0, mailbox.getTotalMessagesSent());
    }

    @Test
    void sendReminderToUser_whenUser_thenSendsMail() {
        when(userService.findActiveUsers()).thenReturn(List.of(userFor("thomas.herzog@gepardec.com", "Thomas")));

        reminderEmailSender.sendReminderToUser();
        assertAll(
                () -> assertEquals(1, mailbox.getTotalMessagesSent()),
                () -> assertEquals("UNIT-TEST: Friendly Reminder: Buchungen kontrollieren",
                        mailbox.getMessagesSentTo("thomas.herzog@gepardec.com").get(0).getSubject())
        );
    }

    private User userFor(final String email, final String firstname) {
        return User.builder()
                .dbId(1L)
                .userId("1")
                .email(email)
                .firstname(firstname)
                .lastname(firstname)
                .roles(Set.of(Role.EMPLOYEE))
                .build();
    }
}
