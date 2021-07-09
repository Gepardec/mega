package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.notification.mail.dates.BusinessDayCalculator;
import com.gepardec.mega.service.api.user.UserService;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.gepardec.mega.notification.mail.Mail.EMPLOYEE_CHECK_PROJECTTIME;
import static com.gepardec.mega.notification.mail.Mail.OM_ADMINISTRATIVE;
import static com.gepardec.mega.notification.mail.Mail.OM_CONTROL_EMPLOYEES_CONTENT;
import static com.gepardec.mega.notification.mail.Mail.OM_RELEASE;
import static com.gepardec.mega.notification.mail.Mail.OM_SALARY;
import static com.gepardec.mega.notification.mail.Mail.PL_PROJECT_CONTROLLING;

@ApplicationScoped
@Transactional(Transactional.TxType.SUPPORTS)
public class ReminderEmailSender {

    @Inject
    ApplicationConfig applicationConfig;

    @Inject
    BusinessDayCalculator businessDayCalculator;

    @Inject
    MailSender mailSender;

    @Inject
    UserService userService;

    @Inject
    Logger logger;

    @Inject
    NotificationConfig notificationConfig;

    public void sendReminder() {
        logger.info("Mail-Daemon-cron-job started at {}", DateUtils.today());
        Optional<Mail> reminder = businessDayCalculator.getEventForDate(DateUtils.today());
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
        final List<User> users = userService.findByRoles(Role.PROJECT_LEAD);
        if (users.isEmpty()) {
            logger.warn("No PL email addresses configured, there sending nothing");
            return;
        }
        users.forEach(user -> mailSender.send(PL_PROJECT_CONTROLLING, user.email(), user.firstname(), applicationConfig.getDefaultLocale()));
        logSentNotification(PL_PROJECT_CONTROLLING);
    }

    void sendReminderToOm(Mail mail) {
        final List<User> users = userService.findByRoles(Role.OFFICE_MANAGEMENT);
        if (users.isEmpty()) {
            logger.warn("No OM email addresses configured, there sending nothing");
            return;
        }
        users.forEach(user -> mailSender.send(mail, user.email(), user.firstname(), applicationConfig.getDefaultLocale()));
        logSentNotification(mail);
    }

    void sendReminderToUser() {
        if (notificationConfig.isEmployeesNotification()) {
            userService.findActiveUsers()
                    .forEach(user -> mailSender.send(EMPLOYEE_CHECK_PROJECTTIME, user.email(), user.firstname(), applicationConfig.getDefaultLocale()));
            logSentNotification(EMPLOYEE_CHECK_PROJECTTIME);
        } else {
            logger.info("NO Reminder to employes sent, cause mega.mail.employees.notification-property is false");
        }
    }

    private void logSentNotification(Mail mail) {
        logger.info("{}. working-day of month. Notification sent for Reminder {}", mail.getDay(), mail.name());
    }
}
