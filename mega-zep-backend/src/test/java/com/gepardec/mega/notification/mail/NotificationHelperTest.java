package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.NotificationConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ResourceBundle;

@ExtendWith(MockitoExtension.class)
class NotificationHelperTest {

    @Mock
    private NotificationConfig notificationConfig;

    @Mock
    private ResourceBundle resourceBundle;

    @InjectMocks
    private NotificationHelper notificationHelper;

    @Nested
    class TemplatePathForReminder {

        @Test
        void whenReminderEMPLOYEE_CHECK_PROJECTTIME_thenEmailPathReturned() {
            final String path = notificationHelper.templatePathForReminder(Reminder.EMPLOYEE_CHECK_PROJECTTIME);

            Assertions.assertEquals("emails/EMPLOYEE_CHECK_PROJECTTIME.html", path);
        }

        @Test
        void whenReminderOM_CONTROL_EMPLOYEES_CONTENT_thenEmailPathReturned() {
            final String path = notificationHelper.templatePathForReminder(Reminder.OM_CONTROL_EMPLOYEES_CONTENT);

            Assertions.assertEquals("emails/OM_CONTROL_EMPLOYEES_CONTENT.html", path);
        }

        @Test
        void whenReminderPL_PROJECT_CONTROLLING_thenEmailPathReturned() {
            final String path = notificationHelper.templatePathForReminder(Reminder.PL_PROJECT_CONTROLLING);

            Assertions.assertEquals("emails/PL_PROJECT_CONTROLLING.html", path);
        }

        @Test
        void whenReminderOM_RELEASE_thenEmailPathReturned() {
            final String path = notificationHelper.templatePathForReminder(Reminder.OM_RELEASE);

            Assertions.assertEquals("emails/OM_RELEASE.html", path);
        }

        @Test
        void whenReminderOM_ADMINISTRATIVE_thenEmailPathReturned() {
            final String path = notificationHelper.templatePathForReminder(Reminder.OM_ADMINISTRATIVE);

            Assertions.assertEquals("emails/OM_ADMINISTRATIVE.html", path);
        }

        @Test
        void whenReminderOM_SALARY_thenEmailPathReturned() {
            final String path = notificationHelper.templatePathForReminder(Reminder.OM_SALARY);

            Assertions.assertEquals("emails/OM_SALARY.html", path);
        }

        @Test
        void _whenReminderOM_CONTROL_PROJECTTIMES_thenEmailPathReturned() {
            final String path = notificationHelper.templatePathForReminder(Reminder.OM_CONTROL_PROJECTTIMES);

            Assertions.assertEquals("emails/OM_CONTROL_PROJECTTIMES.html", path);
        }
    }

    @Nested
    class SubjectForReminder {
        @Test
        void whenPrefixNull_thenNoPrefixAdded() {
            Mockito.when(notificationConfig.getSubjectPrefix()).thenReturn(null);
            Mockito.when(resourceBundle.getString("mail.reminder.EMPLOYEE_CHECK_PROJECTTIME.subject")).thenReturn("Subject");
            final String subject = notificationHelper.subjectForReminder(Reminder.EMPLOYEE_CHECK_PROJECTTIME);

            Assertions.assertEquals("Subject", subject);
        }

        @Test
        void whenPrefixSet_thenNoPrefixAdded() {
            Mockito.when(notificationConfig.getSubjectPrefix()).thenReturn("DEV: ");
            Mockito.when(resourceBundle.getString("mail.reminder.EMPLOYEE_CHECK_PROJECTTIME.subject")).thenReturn("Subject");
            final String subject = notificationHelper.subjectForReminder(Reminder.EMPLOYEE_CHECK_PROJECTTIME);

            Assertions.assertEquals("DEV: Subject", subject);
        }
    }

    @Test
    void getMailTemplateTextPath_whenCalled_thenReturnsBuildTemplatePath() {
        final String templatePath = notificationHelper.getMailTemplateTextPath();

        Assertions.assertEquals("emails/template.html", templatePath);
    }
}
