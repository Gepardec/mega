package com.gepardec.mega.communication;

import com.gepardec.mega.communication.dates.BusinessDayCalculator;
import com.gepardec.mega.utils.DateUtils;
import com.gepardec.mega.zep.service.api.WorkerService;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

import static com.gepardec.mega.communication.Reminder.*;

@ApplicationScoped
public class MailDaemon {

    static final String CRON_CONFIG = "0 0 9 ? * MON-FRI";

    @Inject
    BusinessDayCalculator businessDayCalculator;

    @Inject
    MailSender mailSender;

    @Inject
    WorkerService workerService;

    @Inject
    Logger logger;

    @ConfigProperty(name = "mega.mail.reminder.pl", defaultValue = "")
    String projectLeadersMailAddresses;

    @ConfigProperty(name = "mega.mail.reminder.om", defaultValue = "")
    String omMailAddresses;

    @Scheduled(cron = CRON_CONFIG)
    void sendReminder() {
        Optional<Reminder> reminder = businessDayCalculator.getEventForDate(DateUtils.today());
        if (reminder.isPresent()) {
            switch (reminder.get()) {
                case EMPLOYEE_CHECK_PROJECTTIME: {
                    sendReminderToUser();
                    break;
                }
                case PL_PROJECT_CONTROLLING: {
                    sendReminderToPL();
                    break;
                }
                case OM_CHECK_EMPLOYEES_CONTENT: {
                    sendReminderToOm(OM_CHECK_EMPLOYEES_CONTENT);
                    break;
                }
                case OM_RELEASE: {
                    sendReminderToOm(OM_RELEASE);
                    break;
                }
                case OM_SALARY_CHARGING: {
                    sendReminderToOm(OM_SALARY_CHARGING);
                    break;
                }
                case OM_SALARY_TRANSFER: {
                    sendReminderToOm(OM_SALARY_TRANSFER);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    void sendReminderToPL() {
        if (projectLeadersMailAddresses == null) {
            String msg = "No mail address for project-leaders available, check value for property 'mega.mail.reminder.pl'!";
            logger.error(msg);
            throw new MissingReceiverException(msg);
        }
        Arrays.asList(projectLeadersMailAddresses.split(","))
                .forEach(mailAddress -> mailSender.sendMail(mailAddress, PL_PROJECT_CONTROLLING, ""));
    }

    void sendReminderToOm(Reminder reminder) {
        if (omMailAddresses == null) {
            String msg = "No mail address for om available, check value for property 'mega.mail.reminder.om'!";
            logger.error(msg);
            throw new MissingReceiverException(msg);
        }
        Arrays.asList(omMailAddresses.split(","))
                .forEach(mailAddress -> mailSender.sendMail(mailAddress, reminder, ""));
    }

    void sendReminderToUser() {
        workerService.getAllEmployees()
                .forEach(employee -> mailSender.sendMonthlyFriendlyReminder(employee.getVorname(), employee.getEmail()));
    }
}
