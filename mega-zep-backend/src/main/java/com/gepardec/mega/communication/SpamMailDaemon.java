package com.gepardec.mega.communication;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.gepardec.mega.communication.MailDaemon.getNameByMail;
import static com.gepardec.mega.communication.Reminder.*;

@ApplicationScoped
public class SpamMailDaemon {

    @ConfigProperty(name = "mega.mail.reminder.spam")
    String spamAddresses;

    @Inject
    MailSender mailSender;

    @Inject
    Logger logger;

    /**
     * not a common method - just testing the sending-process to our beloved product owner
     */
    //use only for spam-testing issues
    @Deprecated
    void spamPo() {
        logger.info("start spaming");
        mailSender.sendReminder(spamAddresses, getNameByMail(spamAddresses), EMPLOYEE_CHECK_PROJECTTIME);
        mailSender.sendReminder(spamAddresses, getNameByMail(spamAddresses), PL_PROJECT_CONTROLLING);
        mailSender.sendReminder(spamAddresses, getNameByMail(spamAddresses), OM_CONTROL_EMPLOYEES_CONTENT);
        mailSender.sendReminder(spamAddresses, getNameByMail(spamAddresses), OM_RELEASE);
        mailSender.sendReminder(spamAddresses, getNameByMail(spamAddresses), OM_ADMINISTRATIVE);
        mailSender.sendReminder(spamAddresses, getNameByMail(spamAddresses), OM_SALARY);
    }
}
