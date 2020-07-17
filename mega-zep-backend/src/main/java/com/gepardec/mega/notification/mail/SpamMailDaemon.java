package com.gepardec.mega.notification.mail;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SpamMailDaemon {

    @ConfigProperty(name = "mega.mail.reminder.spam", defaultValue = "")
    String spamAddresses;

    @Inject
    MailSender mailSender;

    @Inject
    Logger logger;

    /**
     * not a common method - just testing the sending-process to our beloved product owner
     */

//    @Scheduled(every = "60m")
    void spamPo() {
        logger.info("start spaming");
        mailSender.sendReminder(spamAddresses, MailDaemon.getNameByMail(spamAddresses), Reminder.EMPLOYEE_CHECK_PROJECTTIME);
        mailSender.sendReminder(spamAddresses, MailDaemon.getNameByMail(spamAddresses), Reminder.PL_PROJECT_CONTROLLING);
        mailSender.sendReminder(spamAddresses, MailDaemon.getNameByMail(spamAddresses), Reminder.OM_CONTROL_EMPLOYEES_CONTENT);
        mailSender.sendReminder(spamAddresses, MailDaemon.getNameByMail(spamAddresses), Reminder.OM_RELEASE);
        mailSender.sendReminder(spamAddresses, MailDaemon.getNameByMail(spamAddresses), Reminder.OM_ADMINISTRATIVE);
        mailSender.sendReminder(spamAddresses, MailDaemon.getNameByMail(spamAddresses), Reminder.OM_SALARY);
    }
}
