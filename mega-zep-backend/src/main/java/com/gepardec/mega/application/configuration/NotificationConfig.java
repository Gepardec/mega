package com.gepardec.mega.application.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationConfig {

    @ConfigProperty(name = "mega.mail.subject-prefix")
    String subjectPrefix;

    @ConfigProperty(name = "mega.logo-path")
    String megaImageLogoUrl;

    @ConfigProperty(name = "mega.wiki.eom-url")
    String megaWikiEomUrl;

    @ConfigProperty(name = "mega.dash-url")
    String megaDashUrl;

    public String getSubjectPrefix() {
        return subjectPrefix;
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
}
