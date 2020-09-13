package com.gepardec.mega.application.producer;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@RequestScoped
public class ResourceBundleProducer {

    private final List<Locale> locales;

    private final Locale defaultLocale;

    private final HttpServletRequest request;

    private ResourceBundle resourceBundle;

    @Inject
    public ResourceBundleProducer(
            @ConfigProperty(name = "quarkus.locales") List<Locale> locales,
            @ConfigProperty(name = "quarkus.default-locale") Locale defaultLocale,
            HttpServletRequest request) {
        this.locales = locales;
        this.defaultLocale = defaultLocale;
        this.request = request;
    }

    @PostConstruct
    public void init() {
        resourceBundle = ResourceBundle.getBundle("messages", localeToUse());
    }

    @Produces
    @Dependent
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    private Locale localeToUse() {
        final Locale requestLocale = request.getLocale();
        if (requestLocale != null && locales.contains(requestLocale)) {
            return request.getLocale();
        }
        return defaultLocale;
    }
}
