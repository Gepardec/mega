package com.gepardec.mega.notification.mail;

import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.UserService;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.gepardec.mega.notification.mail.Mail.OM_RELEASE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@QuarkusTest
class ReminderEmailSenderTest {

    @Inject
    ReminderEmailSender reminderEmailSender;

    @Inject
    MockMailbox mailbox;

    @ConfigProperty(name = "quarkus.mailer.mock")
    boolean mailMockSetting;

    @InjectMock
    UserService userService;

    @BeforeEach
    void init() {
        assertThat(mailMockSetting).as("This test can only run when mail mocking is true").isTrue();
        mailbox.clear();
    }

    @Test
    void sendReminderToOm_whenNoUsers_thenNoMailSend() {
        when(userService.findByRoles(List.of(Role.OFFICE_MANAGEMENT))).thenReturn(List.of());

        reminderEmailSender.sendReminderToOm(OM_RELEASE);

        assertThat(mailbox.getTotalMessagesSent()).isZero();
    }

    @Test
    void sendReminderToOm_whenUser_thenSendsMail() {
        when(userService.findByRoles(List.of(Role.OFFICE_MANAGEMENT))).thenReturn(List.of(userFor("no-reply@gepardec.com", "Max")));

        reminderEmailSender.sendReminderToOm(OM_RELEASE);
        assertAll(
                () -> assertThat(mailbox.getTotalMessagesSent()).isEqualTo(1),
                () -> assertThat(mailbox.getMessagesSentTo("no-reply@gepardec.com").get(0).getSubject())
                        .isEqualTo("UNIT-TEST: Reminder: Freigaben durchführen")
        );
    }

    @Test
    void sendReminderToPl_whenNoUsers_thenNoMailSend() {
        when(userService.findActiveUsers()).thenReturn(List.of());

        reminderEmailSender.sendReminderToPl();

        assertThat(mailbox.getTotalMessagesSent()).isZero();
    }

    @Test
    void sendReminderToPl_whenUser_thenSendsMail() {
        when(userService.findByRoles(List.of(Role.PROJECT_LEAD))).thenReturn(List.of(userFor("no-reply@gepardec.com", "Max")));

        reminderEmailSender.sendReminderToPl();
        assertAll(
                () -> assertThat(mailbox.getTotalMessagesSent()).isEqualTo(1),
                () -> assertThat(mailbox.getMessagesSentTo("no-reply@gepardec.com").get(0).getSubject()).isEqualTo("UNIT-TEST: Reminder: Projekte kontrollieren und abrechnen")
        );
    }

    @Test
    void sendReminderToUser_whenNoUsers_thenNoMailSend() {
        when(userService.findActiveUsers()).thenReturn(List.of());

        reminderEmailSender.sendReminderToUser();

        assertThat(mailbox.getTotalMessagesSent()).isZero();
    }

    @Test
    void sendReminderToUser_whenUser_thenSendsMail() {
        when(userService.findActiveUsers()).thenReturn(List.of(userFor("no-reply@gepardec.com", "Max")));

        reminderEmailSender.sendReminderToUser();
        assertAll(
                () -> assertThat(mailbox.getTotalMessagesSent()).isEqualTo(1),
                () -> assertThat(mailbox.getMessagesSentTo("no-reply@gepardec.com").get(0).getSubject()).isEqualTo("UNIT-TEST: Friendly Reminder: Buchungen kontrollieren")
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
