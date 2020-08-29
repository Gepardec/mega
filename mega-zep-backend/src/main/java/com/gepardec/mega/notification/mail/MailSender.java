package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.google.common.net.MediaType;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@ApplicationScoped
public class MailSender {

    @Inject
    NotificationHelper notificationHelper;

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    Mailer mailer;

    public void sendReminder(String eMail, String firstName, Reminder reminder) {
        String subject = notificationHelper.subjectForReminder(reminder);
        String text = readTextOfPath(notificationHelper.templatePathForReminder(reminder));
        sendMail(eMail, firstName, subject, text);
    }


    private void sendMail(String eMail, String firstName, String subject, String text) {
        final String mailTemplateText = readTextOfPath(notificationHelper.getMailTemplateTextPath())
                .replace(notificationHelper.getMegaDashUrlPlaceholder(), notificationConfig.getMegaDashUrl());
        final String mailContent = mailTemplateText
                .replace(notificationHelper.getNamePlaceholder(), firstName)
                .replace(notificationHelper.getTemplateMailtextPlaceholder(), text)
                .replace(notificationHelper.getEomWikiPlaceholder(), notificationConfig.getMegaWikiEomUrl());


        mailer.send(Mail.withHtml(eMail, subject, mailContent)
                .addInlineAttachment("LogoMEGAdash.png", readLogo(), MediaType.PNG.type(), "<LogoMEGAdash@gepardec.com>"));
    }

    private String readTextOfPath(String pathToRead) {
        final URL url = MailSender.class.getClassLoader().getResource(pathToRead);
        Objects.requireNonNull(url, String.format("File '%s' could not be loaded as a resource", pathToRead));
        try {
            return String.join(System.lineSeparator(), Files.readAllLines(Paths.get(url.toURI())));
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Could not read lines of resource '%s'", pathToRead));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(String.format("Could not get URI of found resource '%s'", pathToRead));
        }
    }

    private byte[] readLogo() {
        final String imageLogoUrl = notificationConfig.getMegaImageLogoUrl();
        final URL url = MailSender.class.getClassLoader().getResource(imageLogoUrl);
        Objects.requireNonNull(url, String.format("File '%s' could not be loaded as a resource", imageLogoUrl));
        try {
            return Files.readAllBytes(Paths.get(url.toURI()));
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Could not read bytes of resource '%s'", imageLogoUrl));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(String.format("Could not get URI of found resource '%s'", imageLogoUrl));
        }
    }
}
