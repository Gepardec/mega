package com.gepardec.mega.communication;

import com.gepardec.mega.configuration.MailProperties;
import com.google.common.net.MediaType;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static com.gepardec.mega.communication.Reminder.EMPLOYEE_CHECK_PROJECTTIME;

@ApplicationScoped
public class MailSender {
    private static final String NAME_PLACEHOLDER = "$firstName$";
    private static final String EOM_WIKI_PLACEHOLDER = "$wikiEomUrl$";
    private static final String MEGA_DASH_URL_PLACEHOLDER = "$megaDash$";

    private static byte[] logoByteArray;

    @Inject
    MailProperties mailProperties;

    @Inject
    Mailer mailer;

    @Inject
    Logger LOG;

    @PostConstruct
    void init() {
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

    @SuppressWarnings("UnstableApiUsage")
    public void sendMonthlyFriendlyReminder (String eMail, String firstName) {
        String mailContent = Objects.requireNonNull(MEGA_REMINDER_1_ST_BUSINESS_DAY_CONTENT)
                .replace(NAME_PLACEHOLDER, firstName)
                .replace(EOM_WIKI_PLACEHOLDER, MEGA_WIKI_EOM_URL)
                .replace(MEGA_DASH_URL_PLACEHOLDER, MEGA_DASH_URL);

        mailer.send(Mail.withHtml(eMail, EMPLOYEE_CHECK_PROJECTTIME.getText(), mailContent)
                .addInlineAttachment("LogoMEGADash.png", logoByteArray, MediaType.PNG.type(), "<LogoMEGAdash@gepardec.com>"));
    }


    public void sendMail(String eMail, Reminder reminder, String text) {
        mailer.send(Mail.withText(eMail, reminder.getText(), text));
    }


    private static String getMailTextForReminder(Reminder reminder) {

        return null;
    }


    public void sendMailToPlForControlling() {
        String
    }
}