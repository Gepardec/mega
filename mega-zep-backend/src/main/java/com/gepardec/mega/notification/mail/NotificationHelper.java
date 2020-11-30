package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.application.producer.ResourceBundleProducer;
import org.apache.commons.io.IOUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class NotificationHelper {

    private static final String MESSAGE_KEY_TEMPLATE_REMINDER = "mail.%s.subject";

    private static final String EMAIL_PATH = "emails";

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    ResourceBundleProducer resourceBundleProducer;

    public String templatePathForMail(final Mail mail, final Locale locale) {
        Objects.requireNonNull(mail, "Cannot retrieve template path for null reminder");
        final String emailPath = Optional.ofNullable(localizedEmailOrNull(mail, locale))
                .orElse(localizedEmailOrNull(mail, Locale.ROOT));
        if (emailPath == null) {
            throw new IllegalArgumentException(String.format("No template path for reminder '%s' found", mail));
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

    public String subjectForMail(Mail mail, final Locale locale) {
        Objects.requireNonNull(mail, "Cannot retrieve subject for null reminder");
        return subjectPrefixOrEmptyString() + resourceBundleProducer.getResourceBundle(locale)
                .getString(String.format(MESSAGE_KEY_TEMPLATE_REMINDER, mail.name()));
    }

    public String subjectForMail(Mail mail, final Locale locale, final List<String> parameter) {
        Objects.requireNonNull(mail, "Cannot retrieve subject for null reminder");
        final String subject = subjectPrefixOrEmptyString() + resourceBundleProducer.getResourceBundle(locale)
                .getString(String.format(MESSAGE_KEY_TEMPLATE_REMINDER, mail.name()));
        if (parameter != null && !parameter.isEmpty()) {
            return MessageFormat.format(subject, parameter.toArray());
        }

        return subject;
    }

    public String subjectPrefixOrEmptyString() {
        return Optional.ofNullable(notificationConfig.getSubjectPrefix()).orElse("");
    }

    private String localizedEmailOrNull(final Mail mail, final Locale locale) {
        String emailPath = EMAIL_PATH + "/";
        if (!locale.getLanguage().isEmpty()) {
            emailPath += locale.getLanguage().toLowerCase() + "/";
        }
        emailPath += mail.name() + ".html";
        if (NotificationHelper.class.getClassLoader().getResource(emailPath) != null) {
            return emailPath;
        } else {
            return null;
        }
    }
}
