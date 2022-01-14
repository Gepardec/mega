package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.application.producer.ResourceBundleProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationHelperTest {

    private final Locale locale = Locale.GERMAN;

    @Mock
    private NotificationConfig notificationConfig;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ResourceBundleProducer resourceBundleProducer;

    @InjectMocks
    private NotificationHelper notificationHelper;

    @Test
    void whenReminderEMPLOYEE_CHECK_PROJECTTIME_thenEmailPathReturned() {
        final String path = notificationHelper.templatePathForMail(Mail.EMPLOYEE_CHECK_PROJECTTIME, locale);

        assertThat(path).isEqualTo("emails/EMPLOYEE_CHECK_PROJECTTIME.html");
    }

    @Test
    void whenReminderOM_CONTROL_EMPLOYEES_CONTENT_thenEmailPathReturned() {
        final String path = notificationHelper.templatePathForMail(Mail.OM_CONTROL_EMPLOYEES_CONTENT, locale);

        assertThat(path).isEqualTo("emails/OM_CONTROL_EMPLOYEES_CONTENT.html");
    }

    @Test
    void whenReminderPL_PROJECT_CONTROLLING_thenEmailPathReturned() {
        final String path = notificationHelper.templatePathForMail(Mail.PL_PROJECT_CONTROLLING, locale);

        assertThat(path).isEqualTo("emails/PL_PROJECT_CONTROLLING.html");
    }

    @Test
    void whenReminderOM_RELEASE_thenEmailPathReturned() {
        final String path = notificationHelper.templatePathForMail(Mail.OM_RELEASE, locale);

        assertThat(path).isEqualTo("emails/OM_RELEASE.html");
    }

    @Test
    void whenReminderOM_ADMINISTRATIVE_thenEmailPathReturned() {
        final String path = notificationHelper.templatePathForMail(Mail.OM_ADMINISTRATIVE, locale);

        assertThat(path).isEqualTo("emails/OM_ADMINISTRATIVE.html");
    }

    @Test
    void whenReminderOM_SALARY_thenEmailPathReturned() {
        final String path = notificationHelper.templatePathForMail(Mail.OM_SALARY, locale);

        assertThat(path).isEqualTo("emails/OM_SALARY.html");
    }

    @Test
    void _whenReminderOM_CONTROL_PROJECTTIMES_thenEmailPathReturned() {
        final String path = notificationHelper.templatePathForMail(Mail.OM_CONTROL_PROJECTTIMES, locale);

        assertThat(path).isEqualTo("emails/OM_CONTROL_PROJECTTIMES.html");
    }

    @Test
    void whenPrefixNull_thenNoPrefixAdded() {
        when(notificationConfig.getSubjectPrefix()).thenReturn(null);
        when(resourceBundleProducer.getResourceBundle(any(Locale.class)).getString("mail.EMPLOYEE_CHECK_PROJECTTIME.subject"))
                .thenReturn("Subject");
        final String subject = notificationHelper.subjectForMail(Mail.EMPLOYEE_CHECK_PROJECTTIME, Locale.GERMAN);

        assertThat(subject).isEqualTo("Subject");
    }

    @Test
    void whenPrefixSet_thenNoPrefixAdded() {
        when(notificationConfig.getSubjectPrefix()).thenReturn("DEV: ");
        when(resourceBundleProducer.getResourceBundle(any(Locale.class)).getString("mail.EMPLOYEE_CHECK_PROJECTTIME.subject"))
                .thenReturn("Subject");
        final String subject = notificationHelper.subjectForMail(Mail.EMPLOYEE_CHECK_PROJECTTIME, Locale.GERMAN);

        assertThat(subject).isEqualTo("DEV: Subject");
    }
}
