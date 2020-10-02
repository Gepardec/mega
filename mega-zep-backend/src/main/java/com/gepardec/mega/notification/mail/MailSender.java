package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.google.common.net.MediaType;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Locale;

@ApplicationScoped
public class MailSender {

    @Inject
    NotificationHelper notificationHelper;

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    Mailer mailer;

    public void sendReminder(String eMail, String firstName, Reminder reminder, Locale locale) {
        String subject = notificationHelper.subjectForReminder(reminder, locale);
        String text = notificationHelper.readEmailTemplateResourceFromStream(notificationHelper.templatePathForReminder(reminder, locale));
        sendMail(eMail, firstName, subject, text);
    }

    private void sendMail(String eMail, String firstName, String subject, String text) {
        final String mailTemplateText = notificationHelper.readEmailTemplateResourceFromStream(notificationHelper.getMailTemplateTextPath())
                .replace(notificationHelper.getMegaDashUrlPlaceholder(), notificationConfig.getMegaDashUrl());
        final String mailContent = mailTemplateText
                .replace(notificationHelper.getNamePlaceholder(), firstName)
                .replace(notificationHelper.getTemplateMailtextPlaceholder(), text)
                .replace(notificationHelper.getEomWikiPlaceholder(), notificationConfig.getMegaWikiEomUrl());

        mailer.send(Mail.withHtml(eMail, subject, mailContent)
                .addInlineAttachment("logo.png", notificationHelper.readLogo(), MediaType.PNG.type(), "<LogoMEGAdash@gepardec.com>"));
    }
}
