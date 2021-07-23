package com.gepardec.mega.notification.mail;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.application.configuration.NotificationConfig;
import com.google.common.collect.Maps;
import com.google.common.net.MediaType;
import io.quarkus.mailer.Mailer;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ApplicationScoped
public class MailSender {

    private static final String NEW_LINE_STRING = "\n";

    private static final String NEW_LINE_HTML = "<br>";

    @Inject
    NotificationHelper notificationHelper;

    @Inject
    NotificationConfig notificationConfig;

    @Inject
    ApplicationConfig applicationConfig;

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
                put(MailParameter.BUDGET_CALCULATION_EXCEL_URL, applicationConfig.getBudgetCalculationExcelUrlAsString());
            }
        };

        Map<String, String> modifiableMap = Maps.newHashMap(mailParameter);
        if (modifiableMap.containsKey(MailParameter.COMMENT)) {
            modifiableMap.put(
                    MailParameter.COMMENT,
                    StringUtils.replace(modifiableMap.get(MailParameter.COMMENT), NEW_LINE_STRING, NEW_LINE_HTML)
            );
        }

        templateParameters.putAll(modifiableMap);

        // replace mail text parameters and mail template parameters
        templateParameters.put(MailParameter.MAIL_TEXT, replaceTextParameters(templateParameters.get(MailParameter.MAIL_TEXT), templateParameters));
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
