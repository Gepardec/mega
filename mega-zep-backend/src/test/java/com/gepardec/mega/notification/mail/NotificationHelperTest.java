package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.application.producer.ResourceBundleProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

// TODO: Enhance tests for other locales as well
@ExtendWith(MockitoExtension.class)
class NotificationHelperTest {

    @Mock
    private NotificationConfig notificationConfig;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ResourceBundleProducer resourceBundleProducer;

    @InjectMocks
    private NotificationHelper notificationHelper;

    @Nested
    class TemplatePathForReminder {

        @Nested
        class WithLocaleGerman {

            private final Locale locale = Locale.GERMAN;

            @Test
            void whenReminderEMPLOYEE_CHECK_PROJECTTIME_thenEmailPathReturned() {
                final String path = notificationHelper.templatePathForReminder(Reminder.EMPLOYEE_CHECK_PROJECTTIME, locale);

                Assertions.assertEquals("emails/EMPLOYEE_CHECK_PROJECTTIME.html", path);
            }

            @Test
            void whenReminderOM_CONTROL_EMPLOYEES_CONTENT_thenEmailPathReturned() {
                final String path = notificationHelper.templatePathForReminder(Reminder.OM_CONTROL_EMPLOYEES_CONTENT, locale);

                Assertions.assertEquals("emails/OM_CONTROL_EMPLOYEES_CONTENT.html", path);
            }

            @Test
            void whenReminderPL_PROJECT_CONTROLLING_thenEmailPathReturned() {
                final String path = notificationHelper.templatePathForReminder(Reminder.PL_PROJECT_CONTROLLING, locale);

                Assertions.assertEquals("emails/PL_PROJECT_CONTROLLING.html", path);
            }

            @Test
            void whenReminderOM_RELEASE_thenEmailPathReturned() {
                final String path = notificationHelper.templatePathForReminder(Reminder.OM_RELEASE, locale);

                Assertions.assertEquals("emails/OM_RELEASE.html", path);
            }

            @Test
            void whenReminderOM_ADMINISTRATIVE_thenEmailPathReturned() {
                final String path = notificationHelper.templatePathForReminder(Reminder.OM_ADMINISTRATIVE, locale);

                Assertions.assertEquals("emails/OM_ADMINISTRATIVE.html", path);
            }

            @Test
            void whenReminderOM_SALARY_thenEmailPathReturned() {
                final String path = notificationHelper.templatePathForReminder(Reminder.OM_SALARY, locale);

                Assertions.assertEquals("emails/OM_SALARY.html", path);
            }

            @Test
            void _whenReminderOM_CONTROL_PROJECTTIMES_thenEmailPathReturned() {
                final String path = notificationHelper.templatePathForReminder(Reminder.OM_CONTROL_PROJECTTIMES, locale);

                Assertions.assertEquals("emails/OM_CONTROL_PROJECTTIMES.html", path);
            }
        }
    }

    @Nested
    class SubjectForReminder {

        @Test
        void whenPrefixNull_thenNoPrefixAdded() {
            when(notificationConfig.getSubjectPrefix()).thenReturn(null);
            when(resourceBundleProducer.getResourceBundle(any(Locale.class)).getString("mail.reminder.EMPLOYEE_CHECK_PROJECTTIME.subject"))
                    .thenReturn("Subject");
            final String subject = notificationHelper.subjectForReminder(Reminder.EMPLOYEE_CHECK_PROJECTTIME, Locale.GERMAN);

            Assertions.assertEquals("Subject", subject);
        }

        @Test
        void whenPrefixSet_thenNoPrefixAdded() {
            when(notificationConfig.getSubjectPrefix()).thenReturn("DEV: ");
            when(resourceBundleProducer.getResourceBundle(any(Locale.class)).getString("mail.reminder.EMPLOYEE_CHECK_PROJECTTIME.subject"))
                    .thenReturn("Subject");
            final String subject = notificationHelper.subjectForReminder(Reminder.EMPLOYEE_CHECK_PROJECTTIME, Locale.GERMAN);

            Assertions.assertEquals("DEV: Subject", subject);
        }
    }
}
