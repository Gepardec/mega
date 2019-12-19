package com.gepardec.mega.communication;

import com.gepardec.mega.communication.dates.BusinessDayCalculator;
import com.gepardec.mega.communication.exception.MissingReceiverException;
import com.gepardec.mega.utils.DateUtils;
import com.gepardec.mega.zep.service.api.WorkerService;
import io.quarkus.scheduler.Scheduled;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

import static com.gepardec.mega.communication.Reminder.*;

@ApplicationScoped
public class MailDaemon {
    @Inject
    BusinessDayCalculator businessDayCalculator;

    @Inject
    MailSender mailSender;

    @Inject
    WorkerService workerService;

    @Inject
    Logger logger;

    @ConfigProperty(name = "mega.mail.reminder.pl")
    String plMailAddresses;

    @ConfigProperty(name = "mega.mail.reminder.om")
    String omMailAddresses;

    @ConfigProperty(name = "mega.mail.employees.notification")
    boolean employeesNotification;

    @Scheduled(cron = "{mega.mail.cron.config}")
    void sendReminder() {
        logger.info("Mail-Daemon-cron-job started at {}", DateUtils.today().toString());
        Optional<Reminder> reminder = businessDayCalculator.getEventForDate(DateUtils.today());
        if (reminder.isPresent()) {
            switch (reminder.get()) {
                case EMPLOYEE_CHECK_PROJECTTIME: {
                    sendReminderToUser();
                    break;
                }
                case PL_PROJECT_CONTROLLING: {
                    sendReminderToPl();
                    break;
                }
                case OM_CONTROL_EMPLOYEES_CONTENT: {
                    sendReminderToOm(OM_CONTROL_EMPLOYEES_CONTENT);
                    break;
                }
                case OM_RELEASE: {
                    sendReminderToOm(OM_RELEASE);
                    break;
                }
                case OM_ADMINISTRATIVE: {
                    sendReminderToOm(OM_ADMINISTRATIVE);
                    break;
                }
                case OM_SALARY: {
                    sendReminderToOm(OM_SALARY);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    void sendReminderToPl() {
        if (plMailAddresses == null) {
            String msg = "No mail address for project-leaders available, check value for property 'mega.mail.reminder.pl'!";
            logger.error(msg);
            throw new MissingReceiverException(msg);
        }
        Arrays.asList(plMailAddresses.split("\\,"))
                .forEach(mailAddress -> mailSender.sendReminder(mailAddress, getNameByMail(mailAddress), PL_PROJECT_CONTROLLING));
        logger.info("PL-Notification sent for reminder {}", PL_PROJECT_CONTROLLING.name());
    }

    void sendReminderToOm(Reminder reminder) {
        if (omMailAddresses == null) {
            String msg = "No mail address for om available, check value for property 'mega.mail.reminder.om'!";
            logger.error(msg);
            throw new MissingReceiverException(msg);
        }
        Arrays.asList(omMailAddresses.split("\\,"))
                .forEach(mailAddress -> mailSender.sendReminder(mailAddress, getNameByMail(mailAddress), reminder));
        logger.info("OM-Notification sent for reminder {}", reminder.name());
    }

    void sendReminderToUser() {
        if (employeesNotification) {
            workerService.getAllActiveEmployees()
                    .forEach(employee -> mailSender.sendReminder(employee.getEmail(), employee.getVorname(), EMPLOYEE_CHECK_PROJECTTIME));
            logger.info("Reminder to employees sent");
        }
        logger.info("NO Reminder to employes sent, cause mega.mail.employees.notification-property is false");
    }

    //TODO: remove immediately when names are available with persistence layer
    static String getNameByMail(String eMail) {
        String[] mailParts = StringUtils.defaultIfBlank(eMail, "").split("\\.");
        if (mailParts != null && !StringUtils.isBlank(mailParts[0])) {
            String firstName = mailParts[0];
            return firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        }
        return StringUtils.EMPTY;
    }
}
