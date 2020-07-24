package com.gepardec.mega.notification.mail;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@ApplicationScoped
public class NotificationConfig {

    private static final String NAME_PLACEHOLDER = "$firstName$";
    private static final String EOM_WIKI_PLACEHOLDER = "$wikiEomUrl$";
    private static final String MEGA_DASH_URL_PLACEHOLDER = "$megaDash$";
    private static final String TEMPLATE_MAILTEXT_PLACEHOLDER = "$mailText$";

    private static final String CONFIG_KEY_TEMPLATE_MAIL_TEMPLATE = "mega.mail.reminder.%s.path";
    private static final String MESSAGE_KEY_TEMPLATE_REMINDER = "mail.reminder.%s.subject";

    @ConfigProperty(name = "mega.mail.reminder.template.path")
    String mailTemplateTextPath;

    @ConfigProperty(name = "mega.mail.subjectPrefix")
    String subjectPrefix;

    @ConfigProperty(name = "mega.image.logo.url")
    String megaImageLogoUrl;

    @ConfigProperty(name = "mega.wiki.eom.url")
    String megaWikiEomUrl;

    @ConfigProperty(name = "mega.dash.url")
    String megaDashUrl;

    @Inject
    Config config;

    @Inject
    ResourceBundle messages;

    public String templatePathForReminder(final String reminderName) {
        Objects.requireNonNull(reminderName, "Cannot retrieve template path for null reminderName");
        final String configKey = String.format(CONFIG_KEY_TEMPLATE_MAIL_TEMPLATE, reminderName);
        return config.getOptionalValue(configKey, String.class)
                .orElseThrow(() -> new IllegalArgumentException(String.format("No template path for reminder '%s' found", reminderName)));
    }

    public String subjectForReminder(String reminderName) {
        Objects.requireNonNull(reminderName, "Cannot retrieve subject for null reminderName");
        return subjectPrefix() + messages.getString(String.format(MESSAGE_KEY_TEMPLATE_REMINDER, reminderName));
    }

    public String subjectPrefix() {
        return Optional.ofNullable(subjectPrefix).orElse("");
    }

    public String getMailTemplateTextPath() {
        return mailTemplateTextPath;
    }

    public String getMegaImageLogoUrl() {
        return megaImageLogoUrl;
    }

    public String getMegaWikiEomUrl() {
        return megaWikiEomUrl;
    }

    public String getMegaDashUrl() {
        return megaDashUrl;
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
