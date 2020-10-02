package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.notification.mail.dates.BusinessDayCalculator;
import com.gepardec.mega.service.api.employee.EmployeeService;
import io.quarkus.scheduler.Scheduled;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.gepardec.mega.notification.mail.Reminder.*;

@ApplicationScoped
public class MailDaemon {

    @Inject
    ApplicationConfig applicationConfig;

    @Inject
    BusinessDayCalculator businessDayCalculator;

    @Inject
    MailSender mailSender;

    @Inject
    EmployeeService employeeService;

    @Inject
    Logger logger;

    @Inject
    NotificationConfig notificationConfig;

    @Scheduled(cron = "{mega.mail.cron}")
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
                    logger.info("No notification sent today");
                    break;
                }
            }
        } else {
            logger.info("NO notification sent today");
        }
    }


    void sendReminderToPl() {
        if (notificationConfig.getPlMailAddresses().trim().isEmpty()) {
            throw new IllegalStateException("No mail address for project-leaders available, check value for property 'mega.mail.reminder.pl'!");
        }
        List.of(notificationConfig.getPlMailAddresses().split("\\,"))
                .forEach(mailAddress -> mailSender.sendReminder(mailAddress, getNameByMail(mailAddress), PL_PROJECT_CONTROLLING, applicationConfig.getDefaultLocale()));
        logSentNotification(PL_PROJECT_CONTROLLING);
    }

    void sendReminderToOm(Reminder reminder) {
        if (notificationConfig.getOmMailAddresses().trim().isEmpty()) {
            throw new IllegalStateException("No mail address for om available, check value for property 'mega.mail.reminder.om'!");
        }
        List.of(notificationConfig.getOmMailAddresses().split("\\,"))
                .forEach(mailAddress -> mailSender.sendReminder(mailAddress, getNameByMail(mailAddress), reminder, applicationConfig.getDefaultLocale()));
        logSentNotification(reminder);
    }

    void sendReminderToUser() {
        if (notificationConfig.isEmployeesNotification()) {
            employeeService.getAllActiveEmployees()
                    .forEach(employee -> mailSender.sendReminder(employee.email(), employee.firstName(), EMPLOYEE_CHECK_PROJECTTIME, applicationConfig.getDefaultLocale()));
            logSentNotification(EMPLOYEE_CHECK_PROJECTTIME);
        } else {
            logger.info("NO Reminder to employes sent, cause mega.mail.employees.notification-property is false");
        }
    }

    //TODO: remove immediately when names are available with persistence layer
    static String getNameByMail(String eMail) {
        String[] mailParts = StringUtils.defaultIfBlank(eMail, "").split("\\.");
        if (!StringUtils.isBlank(mailParts[0])) {
            String firstName = mailParts[0];
            return firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        }
        return StringUtils.EMPTY;
    }

    private void logSentNotification(Reminder reminder) {
        logger.info("{}. working-day of month. Notification sent for Reminder {}", reminder.getDay(), reminder.name());
    }
}
