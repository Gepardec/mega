package com.gepardec.mega.application.schedule;

import com.gepardec.mega.notification.mail.ReminderEmailSender;
import com.gepardec.mega.service.api.init.StepEntrySyncService;
import com.gepardec.mega.service.api.init.SyncService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 10/3/2020
 */
@Dependent
public class Schedules {

    @Inject
    SyncService syncService;

    @Inject
    StepEntrySyncService stepEntrySyncService;

    @Inject
    ReminderEmailSender reminderEmailSender;

    @Scheduled(identity = "Sync ZEP-Employees with Users in the database",
            every = "PT30M",
            delay = 15, delayUnit = TimeUnit.SECONDS)
        // We need to wait for liquibase to finish, but is executed in parallel
    void syncEmployeesWithDatabase() {
        syncService.syncEmployees();
    }

    @Scheduled(identity = "Generate step entries on the first day of a month",
            cron = "0 0 0 1 * ? *")
    void generateStepEntries() {
        stepEntrySyncService.genereteStepEntries();
    }

    @Scheduled(identity = "Send E-Mail reminder to Users",
            cron = "0 0 7 ? * MON-FRI")
    void sendReminder() {
        reminderEmailSender.sendReminder();
    }
}
