package com.gepardec.mega.notification.mail;

import com.gepardec.mega.domain.utils.FileHelper;
import com.gepardec.mega.notification.mail.exception.MissingLogoException;
import com.google.common.net.MediaType;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class MailSender {

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    Mailer mailer;

    @Inject
    FileHelper fileHelper;

    @Inject
    Logger logger;

    private byte[] logoByteArray;
    private String mailTemplateText;

    @PostConstruct
    void initLogoAndTemplate() {
        mailTemplateText = fileHelper.readTextOfPath(notificationConfig.getMailTemplateTextPath())
                .replace(notificationConfig.getMegaDashUrlPlaceholder(), notificationConfig.getMegaDashUrl());

        readLogo();
    }

    private void readLogo() {
        final InputStream logoInputStream = MailSender.class.getClassLoader().getResourceAsStream(notificationConfig.getMegaImageLogoUrl());
        if (logoInputStream != null) {
            try {
                logoByteArray = new byte[logoInputStream.available()];
                final int result = logoInputStream.read(logoByteArray);
                if (result == -1) {
                    logger.error("Error while reading image file.");
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } else {
            String msg = String.format("No image found under following Path: %s", notificationConfig.getMegaImageLogoUrl());
            logger.error(msg);
            throw new MissingLogoException(msg);
        }
    }


    public void sendReminder(String eMail, String firstName, Reminder reminder) {
        String subject = notificationConfig.subjectForReminder(reminder.name());
        String text = fileHelper.readTextOfPath(notificationConfig.templatePathForReminder(reminder.name()));
        sendMail(eMail, firstName, subject, text);
    }


    private void sendMail(String eMail, String firstName, String subject, String text) {
        String mailContent = mailTemplateText
                .replace(notificationConfig.getNamePlaceholder(), firstName)
                .replace(notificationConfig.getTemplateMailtextPlaceholder(), text)
                .replace(notificationConfig.getEomWikiPlaceholder(), notificationConfig.getMegaWikiEomUrl());


        mailer.send(Mail.withHtml(eMail, subject, mailContent)
                .addInlineAttachment("LogoMEGAdash.png", logoByteArray, MediaType.PNG.type(), "<LogoMEGAdash@gepardec.com>"));
    }
}