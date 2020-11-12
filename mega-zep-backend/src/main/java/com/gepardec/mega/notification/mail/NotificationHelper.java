package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.NotificationConfig;
import org.apache.commons.io.IOUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@ApplicationScoped
public class NotificationHelper {

    private static final String NAME_PLACEHOLDER = "$firstName$";

    private static final String EXCEL_URL_PLACEHOLDER = "$excelUrl$";

    private static final String EOM_WIKI_PLACEHOLDER = "$wikiEomUrl$";

    private static final String MEGA_DASH_URL_PLACEHOLDER = "$megaDash$";

    private static final String TEMPLATE_MAILTEXT_PLACEHOLDER = "$mailText$";

    private static final String MESSAGE_KEY_TEMPLATE_REMINDER = "mail.reminder.%s.subject";

    private static final String EMAIL_PATH = "emails";

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    ResourceBundle resourceBundle;

    private final Locale currentLocale;

    @Inject
    public NotificationHelper(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

    public String templatePathForReminder(final Reminder reminder) {
        Objects.requireNonNull(reminder, "Cannot retrieve template path for null reminder");
        final String emailPath = Optional.ofNullable(localizedEmailOrNull(reminder, currentLocale))
                .orElse(localizedEmailOrNull(reminder, Locale.ROOT));
        if (emailPath == null) {
            throw new IllegalArgumentException(String.format("No template path for reminder '%s' found", reminder));
        }
        return emailPath;
    }

    public String readEmailTemplateResourceFromStream(String resourcePath) {
        try (final InputStream is = MailSender.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalStateException("Could not get inputStream of email template resource '" + resourcePath + "' resource");
            }
            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read email template '" + resourcePath + "' resource as stream", e);
        }
    }

    public byte[] readLogo() {
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

    public String subjectForReminder(Reminder reminder) {
        Objects.requireNonNull(reminder, "Cannot retrieve subject for null reminder");
        return subjectPrefixOrEmptyString() + resourceBundle.getString(String.format(MESSAGE_KEY_TEMPLATE_REMINDER, reminder.name()));
    }

    public String subjectPrefixOrEmptyString() {
        return Optional.ofNullable(notificationConfig.getSubjectPrefix()).orElse("");
    }

    private String localizedEmailOrNull(final Reminder reminder, final Locale locale) {
        String emailPath = EMAIL_PATH + "/";
        if (!locale.getLanguage().isEmpty()) {
            emailPath += locale.getLanguage().toLowerCase() + "/";
        }
        emailPath += reminder.name() + ".html";
        if (NotificationHelper.class.getClassLoader().getResource(emailPath) != null) {
            return emailPath;
        } else {
            return null;
        }
    }

    public String getMailTemplateTextPath() {
        return EMAIL_PATH + "/template.html";
    }

    public String getNamePlaceholder() {
        return NAME_PLACEHOLDER;
    }

    public String getEomWikiPlaceholder() {
        return EOM_WIKI_PLACEHOLDER;
    }

    public String getExcelUrlPlaceholder() {
        return EXCEL_URL_PLACEHOLDER;
    }

    public String getMegaDashUrlPlaceholder() {
        return MEGA_DASH_URL_PLACEHOLDER;
    }

    public String getTemplateMailtextPlaceholder() {
        return TEMPLATE_MAILTEXT_PLACEHOLDER;
    }
}
