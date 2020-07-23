package com.gepardec.mega.notification.mail;

import com.google.common.net.MediaType;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class MailSender {
    private static final String NAME_PLACEHOLDER = "$firstName$";
    private static final String EOM_WIKI_PLACEHOLDER = "$wikiEomUrl$";
    private static final String MEGA_DASH_URL_PLACEHOLDER = "$megaDash$";
    private static final String TEMPLATE_MAILTEXT_PLACEHOLDER = "$mailText$";

    @ConfigProperty(name = "mega.mail.subjectPrefix")
    String subjectPrefix;

    @ConfigProperty(name = "mega.image.logo.url")
    String megaImageLogoUrl;

    @ConfigProperty(name = "mega.wiki.eom.url")
    String megaWikiEomUrl;

    @ConfigProperty(name = "mega.dash.url")
    String megaDashUrl;

    @ConfigProperty(name = "mega.mail.reminder.template.path")
    String mailTemplateTextPath;

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    Mailer mailer;

    @Inject
    Logger logger;

    private String mailTemplateText;

    @PostConstruct
    void initLogoAndTemplate() {
        mailTemplateText = readTextOfPath(mailTemplateTextPath)
                .replace(MEGA_DASH_URL_PLACEHOLDER, megaDashUrl);
    }

    public void sendReminder(String eMail, String firstName, Reminder reminder) {
        String subject = getSubjectForReminder(reminder);
        String text = readTextOfPath(getPathForReminder(reminder));
        sendMail(eMail, firstName, subject, text);
    }


    private void sendMail(String eMail, String firstName, String subject, String text) {
        String mailContent = mailTemplateText
                .replace(NAME_PLACEHOLDER, firstName)
                .replace(TEMPLATE_MAILTEXT_PLACEHOLDER, text)
                .replace(EOM_WIKI_PLACEHOLDER, megaWikiEomUrl);


        mailer.send(Mail.withHtml(eMail, subject, mailContent)
                .addInlineAttachment("LogoMEGAdash.png", readLogo(), MediaType.PNG.type(), "<LogoMEGAdash@gepardec.com>"));
    }


    public String getPathForReminder(Reminder reminder) {
        String path;
        switch (reminder) {
            case EMPLOYEE_CHECK_PROJECTTIME: {
                path = notificationConfig.getEmployeePath();
                break;
            }
            case PL_PROJECT_CONTROLLING: {
                path = notificationConfig.getPlPath();
                break;
            }
            case OM_CONTROL_EMPLOYEES_CONTENT: {
                path = notificationConfig.getOmControlEmployeesDataPath();
                break;
            }
            case OM_RELEASE: {
                path = notificationConfig.getOmReleasePath();
                break;
            }
            case OM_ADMINISTRATIVE: {
                path = notificationConfig.getOmAdministrativePath();
                break;
            }
            case OM_SALARY: {
                path = notificationConfig.getOmSalaryPath();
                break;
            }
            case OM_CONTROL_PROJECTTIMES: {
                path = notificationConfig.getOmControlProjecttimesPath();
                break;
            }
            default: {
                throw new IllegalArgumentException(String.format("No mail template found for Reminder: '%s'", reminder.name()));
            }
        }
        return path;
    }

    public String getSubjectForReminder(Reminder reminder) {
        String subject;
        switch (reminder) {
            case EMPLOYEE_CHECK_PROJECTTIME: {
                subject = notificationConfig.getEmployeeSubject();
                break;
            }
            case PL_PROJECT_CONTROLLING: {
                subject = notificationConfig.getPlSubject();
                break;
            }
            case OM_CONTROL_EMPLOYEES_CONTENT: {
                subject = notificationConfig.getOmControlEmployeesDataSubject();
                break;
            }
            case OM_RELEASE: {
                subject = notificationConfig.getOmReleaseSubject();
                break;
            }
            case OM_ADMINISTRATIVE: {
                subject = notificationConfig.getOmAdministrativeSubject();
                break;
            }
            case OM_SALARY: {
                subject = notificationConfig.getOmSalarySubject();
                break;
            }
            case OM_CONTROL_PROJECTTIMES: {
                subject = notificationConfig.getOmControlProjecttimesSubject();
                break;
            }
            default: {
                throw new IllegalArgumentException(String.format("No mail subject found for Reminder: '%s'", reminder.name()));
            }
        }
        return Optional.ofNullable(subjectPrefix).orElse("") + subject;
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
        final URL url = MailSender.class.getClassLoader().getResource(megaImageLogoUrl);
        Objects.requireNonNull(url, String.format("File '%s' could not be loaded as a resource", megaImageLogoUrl));
        try {
            return Files.readAllBytes(Paths.get(url.toURI()));
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Could not read bytes of resource '%s'", megaImageLogoUrl));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(String.format("Could not get URI of found resource '%s'", megaImageLogoUrl));
        }
    }
}
