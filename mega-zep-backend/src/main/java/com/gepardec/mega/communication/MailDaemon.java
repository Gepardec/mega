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

    @ConfigProperty(name = "mega.mail.reminder.pl")
    String plMailAddresses;

    @ConfigProperty(name = "mega.mail.reminder.om")
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

    void sendReminderToPL() {
        if (plMailAddresses == null) {
            String msg = "No mail address for project-leaders available, check value for property 'mega.mail.reminder.pl'!";
            logger.error(msg);
            throw new MissingReceiverException(msg);
        }
        Arrays.asList(plMailAddresses.split(","))
                .forEach(mailAddress -> mailSender.sendReminder(mailAddress, getNameByMail(mailAddress), PL_PROJECT_CONTROLLING));
    }

    void sendReminderToOm(Reminder reminder) {
        if (omMailAddresses == null) {
            String msg = "No mail address for om available, check value for property 'mega.mail.reminder.om'!";
            logger.error(msg);
            throw new MissingReceiverException(msg);
        }
        Arrays.asList(omMailAddresses.split(","))
                .forEach(mailAddress -> mailSender.sendReminder(mailAddress, getNameByMail(mailAddress), reminder));
    }

    void sendReminderToUser() {
        workerService.getAllEmployees()
                .forEach(employee -> mailSender.sendReminder(employee.getEmail(), employee.getVorname(), EMPLOYEE_CHECK_PROJECTTIME));
    }

    //TODO: remove immediately when names are available
    private static String getNameByMail(String eMail) {
        return eMail.split(".")[0];
    }
}
