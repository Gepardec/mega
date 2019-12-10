package com.gepardec.mega.communication;

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
import java.util.Objects;

import static com.gepardec.mega.communication.Reminder.USER_CHECK_PROJECTTIMES;

@ApplicationScoped
public class MailSender {

    @ConfigProperty(name = "mega.mail.firstbusinessday.reminder")
    String MEGA_REMINDER_1_ST_BUSINESS_DAY_CONTENT;

    @ConfigProperty(name = "mega.image.logo.url")
    String MEGA_IMAGE_LOGO_URL;

    @ConfigProperty(name = "mega.wiki.eom.url")
    String MEGA_WIKI_EOM_URL;

    private static final String NAME_PLACEHOLDER = "$firstName$";
    private static final String EOM_WIKI_PLACEHOLDER = "$wiki_eom_url$";

    private static byte[] logoByteArray;

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
                if(result == -1) {
                    LOG.error("Error while reading image file.");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public void sendMonthlyFriendlyReminder (String eMail, String firstName) {
        String mailContent = Objects.requireNonNull(MEGA_REMINDER_1_ST_BUSINESS_DAY_CONTENT)
                .replace(NAME_PLACEHOLDER, firstName)
                .replace(EOM_WIKI_PLACEHOLDER, MEGA_WIKI_EOM_URL);

        mailer.send(Mail.withHtml(eMail, USER_CHECK_PROJECTTIMES.getText(), mailContent)
                .addInlineAttachment("LogoMEGAdash.png", logoByteArray, MediaType.PNG.type(), "<logomegadash@gepardec.com>"));
    }


    public void sendMail(String eMail, Reminder reminder, String text) {
        mailer.send(Mail.withText(eMail, reminder.getText(), text));
    }
}