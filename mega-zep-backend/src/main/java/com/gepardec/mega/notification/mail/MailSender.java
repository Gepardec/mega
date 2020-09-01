package com.gepardec.mega.notification.mail;

import com.google.common.net.MediaType;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.apache.commons.io.IOUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class MailSender {

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    Mailer mailer;

    public void sendReminder(String eMail, String firstName, Reminder reminder) {
        String subject = notificationConfig.subjectForReminder(reminder.name());
        String text = readEmailTemplateResourceFromStream(notificationConfig.templatePathForReminder(reminder.name()));
        sendMail(eMail, firstName, subject, text);
    }


    private void sendMail(String eMail, String firstName, String subject, String text) {
        final String mailTemplateText = readEmailTemplateResourceFromStream(notificationConfig.getMailTemplateTextPath())
                .replace(notificationConfig.getMegaDashUrlPlaceholder(), notificationConfig.getMegaDashUrl());
        final String mailContent = mailTemplateText
                .replace(notificationConfig.getNamePlaceholder(), firstName)
                .replace(notificationConfig.getTemplateMailtextPlaceholder(), text)
                .replace(notificationConfig.getEomWikiPlaceholder(), notificationConfig.getMegaWikiEomUrl());


        mailer.send(Mail.withHtml(eMail, subject, mailContent)
                .addInlineAttachment("LogoMEGAdash.png", readLogo(), MediaType.PNG.type(), "<LogoMEGAdash@gepardec.com>"));
    }

    private String readEmailTemplateResourceFromStream(String resourcePath) {
        try (final InputStream is = MailSender.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalStateException("Could not get inputStream of email template resource '" + resourcePath + "' resource");
            }
            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read email template '" + resourcePath + "' resource as stream", e);
        }
    }

    private byte[] readLogo() {
        final String logoResourcePath = notificationConfig.getMegaImageLogoUrl();
        try (final InputStream is = MailSender.class.getClassLoader().getResourceAsStream(logoResourcePath)) {
            if (is == null) {
                throw new IllegalStateException("Could not get inputStream of logo resource '" + logoResourcePath + "' resource");
            }
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read logo '" + logoResourcePath + "' resource as stream", e);
        }
    }
}
