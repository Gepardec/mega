package com.gepardec.mega.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MailProperties {
    @ConfigProperty(name = "mega.mail.firstbusinessday.reminder")
    String MEGA_REMINDER_1_ST_BUSINESS_DAY_CONTENT;

    @ConfigProperty(name = "mega.image.logo.url")
    String MEGA_IMAGE_LOGO_URL;

    @ConfigProperty(name = "mega.wiki.eom.url")
    String MEGA_WIKI_EOM_URL;

    @ConfigProperty(name = "mega.dash.url")
    String MEGA_DASH_URL;


    public String getMEGA_REMINDER_1_ST_BUSINESS_DAY_CONTENT() {
        return MEGA_REMINDER_1_ST_BUSINESS_DAY_CONTENT;
    }

    public String getMEGA_IMAGE_LOGO_URL() {
        return MEGA_IMAGE_LOGO_URL;
    }

    public String getMEGA_WIKI_EOM_URL() {
        return MEGA_WIKI_EOM_URL;
    }

    public String getMEGA_DASH_URL() {
        return MEGA_DASH_URL;
    }
}
