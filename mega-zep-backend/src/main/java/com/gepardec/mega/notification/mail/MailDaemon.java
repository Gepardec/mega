package com.gepardec.mega.notification.mail;

import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.notification.mail.dates.BusinessDayCalculator;
import com.gepardec.mega.service.api.employee.EmployeeService;
import io.quarkus.scheduler.Scheduled;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

import static com.gepardec.mega.notification.mail.Reminder.*;

@ApplicationScoped
public class MailDaemon {
    @Inject
    BusinessDayCalculator businessDayCalculator;

    @Inject
    MailSender mailSender;

    @Inject
    EmployeeService employeeService;

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
                    logger.info("No notification sent today");
                    break;
                }
            }
        } else {
            logger.info("NO notification sent today");
        }
    }


    void sendReminderToPl() {
        if (plMailAddresses == null) {
            throw new IllegalStateException("No mail address for project-leaders available, check value for property 'mega.mail.reminder.pl'!");
        }
        Arrays.asList(plMailAddresses.split("\\,"))
                .forEach(mailAddress -> mailSender.sendReminder(mailAddress, getNameByMail(mailAddress), PL_PROJECT_CONTROLLING));
        logSentNotification(PL_PROJECT_CONTROLLING);
    }

    void sendReminderToOm(Reminder reminder) {
        if (omMailAddresses == null) {
            throw new IllegalStateException("No mail address for om available, check value for property 'mega.mail.reminder.om'!");
        }
        Arrays.asList(omMailAddresses.split("\\,"))
                .forEach(mailAddress -> mailSender.sendReminder(mailAddress, getNameByMail(mailAddress), reminder));
        logSentNotification(reminder);
    }

    void sendReminderToUser() {
        if (employeesNotification) {
            employeeService.getAllActiveEmployees()
                    .forEach(employee -> mailSender.sendReminder(employee.email(), employee.firstName(), EMPLOYEE_CHECK_PROJECTTIME));
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
