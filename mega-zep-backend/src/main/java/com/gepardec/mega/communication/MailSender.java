package com.gepardec.mega.communication;


import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gepardec.mega.communication.Reminder.USER_CHECK_PROJECTTIMES;

@ApplicationScoped
public class MailSender {

    private static final String REMINDER_1_ST_BUSINESS_DAY_HTML = "reminder1stBusinessDay.html";
    private static final String NAME_PLACEHOLDER = "$firstName$";
    private static final String LOGO_PATH = "src/main/resources/LogoMegaDash.png";

    private String mailBody = null;

    @Inject
    Mailer mailer;

    @Inject
    Logger logger;

    @PostConstruct
    void readFiles() {
        try {
            final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(REMINDER_1_ST_BUSINESS_DAY_HTML)).toURI());
            try (Stream<String> lines = Files.lines(path)) {
                mailBody = lines.collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (URISyntaxException | IOException e) {
            logger.error("Error reading message Text from File {} ", REMINDER_1_ST_BUSINESS_DAY_HTML);
        }
    }

    public void sendMonthlyFriendlyReminder(String eMail, String firstName) {
        String mailContent = Objects.requireNonNull(mailBody)
                .replace(NAME_PLACEHOLDER, firstName);

        mailer.send(Mail
                .withHtml(eMail, USER_CHECK_PROJECTTIMES.getText(), mailContent)
                .addInlineAttachment("LogoMegaDash.png", new File(LOGO_PATH),
                        "image/png", "<LogoMegaDash@gepardec.com>"));
    }

    public void sendMail(String eMail, Reminder reminder, String text) {
        mailer.send(Mail.withText(eMail, reminder.getText(), text));
    }
}