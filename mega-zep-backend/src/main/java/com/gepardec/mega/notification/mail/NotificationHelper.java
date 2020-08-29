package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.NotificationConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@ApplicationScoped
public class NotificationHelper {

    private static final String NAME_PLACEHOLDER = "$firstName$";
    private static final String EOM_WIKI_PLACEHOLDER = "$wikiEomUrl$";
    private static final String MEGA_DASH_URL_PLACEHOLDER = "$megaDash$";
    private static final String TEMPLATE_MAILTEXT_PLACEHOLDER = "$mailText$";

    private static final String MESSAGE_KEY_TEMPLATE_REMINDER = "mail.reminder.%s.subject";

    private static final String EMAIL_PATH = "emails";

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    ResourceBundle resourceBundle;

    public String templatePathForReminder(final Reminder reminder) {
        Objects.requireNonNull(reminder, "Cannot retrieve template path for null reminder");
        final String emailPath = EMAIL_PATH + "/" + reminder.name() + ".html";
        if (NotificationHelper.class.getClassLoader().getResource(emailPath) != null) {
            return emailPath;
        } else {
            throw new IllegalArgumentException(String.format("No template path for reminder '%s' found", reminder));
        }
    }

    public String subjectForReminder(Reminder reminder) {
        Objects.requireNonNull(reminder, "Cannot retrieve subject for null reminder");
        return subjectPrefixOrEmptyString() + resourceBundle.getString(String.format(MESSAGE_KEY_TEMPLATE_REMINDER, reminder.name()));
    }

    public String subjectPrefixOrEmptyString() {
        return Optional.ofNullable(notificationConfig.getSubjectPrefix()).orElse("");
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

    public String getMegaDashUrlPlaceholder() {
        return MEGA_DASH_URL_PLACEHOLDER;
    }

    public String getTemplateMailtextPlaceholder() {
        return TEMPLATE_MAILTEXT_PLACEHOLDER;
    }
}
