package com.gepardec.mega.communication;

import com.gepardec.mega.utils.FileHelper;
import com.google.common.net.MediaType;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class MailSender {
    private static final String NAME_PLACEHOLDER = "$firstName$";
    private static final String EOM_WIKI_PLACEHOLDER = "$wikiEomUrl$";
    private static final String MEGA_DASH_URL_PLACEHOLDER = "$megaDash$";
    private static final String TEMPLATE_MAILTEXT_PLACEHOLDER = "$mailText$";

    private static byte[] logoByteArray;


    @ConfigProperty(name = "mega.image.logo.url")
    String MEGA_IMAGE_LOGO_URL;

    @ConfigProperty(name = "mega.wiki.eom.url")
    String MEGA_WIKI_EOM_URL;

    @ConfigProperty(name = "mega.dash.url")
    String MEGA_DASH_URL;

    @ConfigProperty(name = "mega.mail.reminder.template.path")
    String mailTemplateTextPath;

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    Mailer mailer;

    @Inject
    FileHelper fileHelper;

    @Inject
    Logger LOG;

    private String mailTemplateText;

    @PostConstruct
    void initLogoAndTemplate() {
        mailTemplateText = fileHelper.readTextOfPath(mailTemplateTextPath)
                .replace(EOM_WIKI_PLACEHOLDER, MEGA_WIKI_EOM_URL)
                .replace(MEGA_DASH_URL_PLACEHOLDER, MEGA_DASH_URL);

        readLogo();
    }

    private void readLogo() {
        final InputStream logoInputStream = MailSender.class.getClassLoader().getResourceAsStream(MEGA_IMAGE_LOGO_URL);
        if (logoInputStream != null) {
            try {
                logoByteArray = new byte[logoInputStream.available()];
                final int result = logoInputStream.read(logoByteArray);
                if (result == -1) {
                    LOG.error("Error while reading image file.");
                }
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        } else {
            String msg = String.format("No image found under following Path: %s", MEGA_IMAGE_LOGO_URL);
            LOG.error(msg);
            throw new MissingLogoException(msg);
        }
    }

    public void sendReminder(String eMail, String firstName, Reminder reminder) {
        String subject = notificationConfig.getSubjectByReminder(reminder);
        String text = fileHelper.readTextOfPath(
                notificationConfig.getPathByReminder(reminder));
        sendMail(eMail, firstName, subject, text);
    }


    private void sendMail(String eMail, String firstName, String subject, String text) {
        String mailContent = mailTemplateText
                .replace(NAME_PLACEHOLDER, firstName)
                .replace(TEMPLATE_MAILTEXT_PLACEHOLDER, text);

        mailer.send(Mail.withHtml(eMail, subject, mailContent)
                .addInlineAttachment("LogoMEGADash.png", logoByteArray, MediaType.PNG.type(), "<LogoMEGAdash@gepardec.com>"));
    }


}