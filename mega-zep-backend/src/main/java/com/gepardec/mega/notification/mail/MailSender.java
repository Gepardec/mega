package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.google.common.net.MediaType;
import io.quarkus.mailer.Mailer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ApplicationScoped
public class MailSender {

    @Inject
    NotificationHelper notificationHelper;

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    Mailer mailer;

    public void send(Mail mail, String eMail, String firstName, Locale locale) {
        send(mail, eMail, firstName, locale, Map.of(), List.of());
    }

    public void send(Mail mail, String eMail, String firstName, Locale locale, Map<String, String> mailParameter, List<String> subjectParameter) {
        String subject = notificationHelper.subjectForMail(mail, locale, subjectParameter);
        String text = notificationHelper.readEmailTemplateResourceFromStream(notificationHelper.templatePathForMail(mail, locale));
        final String mailTemplateText = (mail.getTemplate() != null) ? notificationHelper.readEmailTemplateResourceFromStream(mail.getTemplate()) : text;
        final Map<String, String> templateParameters = new HashMap<>() {

            {
                put(MailParameter.FIRST_NAME, firstName);
                put(MailParameter.MAIL_TEXT, text);
                put(MailParameter.WIKI_EOM_URL, notificationConfig.getMegaWikiEomUrl());
                put(MailParameter.MEGA_DASH, notificationConfig.getMegaDashUrl());
            }
        };
        templateParameters.putAll(mailParameter);
        String replacedContent = replaceTextParameters(mailTemplateText, templateParameters);

        mailer.send(io.quarkus.mailer.Mail.withHtml(eMail, subject, replacedContent)
                .addInlineAttachment("logo.png", notificationHelper.readLogo(), MediaType.PNG.type(), "<LogoMEGAdash@gepardec.com>"));
    }

    private String replaceTextParameters(final String text, final Map<String, String> parameters) {
        String replacedText = text;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            replacedText = replacedText.replace(entry.getKey(), entry.getValue());
        }

        return replacedText;
    }


}
