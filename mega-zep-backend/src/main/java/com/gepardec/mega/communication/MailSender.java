package com.gepardec.mega.communication;


import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.slf4j.Logger;

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

@ApplicationScoped
public class MailSender {

    private static final String MAIL_TO_USER_PATH = "mailToUsers.html";
    private static final String NAME_PLACEHOLDER = "$firstName$";
    private static final String LOGO_PATH = "src/main/resources/LogoMegaDash.png";

    @Inject
    Mailer mailer;

    @Inject
    Logger logger;


    public void sendMonthlyFriendlyReminder(String firstName, String eMail) {

        String mailBody = null;
        try {
            final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(MAIL_TO_USER_PATH)).toURI());
            try (Stream<String> lines = Files.lines(path)) {
                mailBody = lines.collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (URISyntaxException | IOException e) {
            logger.error("Error reading message Text from File {} ", MAIL_TO_USER_PATH);
        }

        String subject = "Friendly Reminder: Buchungen kontrollieren";
        String mailContent = Objects.requireNonNull(mailBody)
                .replace(NAME_PLACEHOLDER, firstName);


        Mail mail = Mail.withHtml(eMail, subject, mailContent)
                .addInlineAttachment("LogoMegaDash.png", new File(LOGO_PATH), "image/png", "<LogoMegaDash@gepardec.com>");
        mailer.send(mail);
    }
}