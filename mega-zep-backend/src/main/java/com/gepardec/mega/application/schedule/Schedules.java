package com.gepardec.mega.application.schedule;

import com.gepardec.mega.notification.mail.ReminderEmailSender;
import com.gepardec.mega.service.api.init.SyncService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 10/3/2020
 */
@Dependent
public class Schedules {

    @Inject
    SyncService syncService;

    @Inject
    ReminderEmailSender reminderEmailSender;

    @Scheduled(identity = "Sync ZEP-Employees with Users in the database",
            every = "PT30M")
    void syncEmployeesWithDatabase() {
        syncService.syncEmployees();
    }

    @Scheduled(identity = "Send E-Mail reminder to Users",
            cron = "{mega.mail.cron}")
    void sendReminder() {
        reminderEmailSender.sendReminder();
    }
}
