package com.gepardec.mega.application.producer;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@RequestScoped
public class LocaleProducer {

    private final List<Locale> locales;

    private final Locale defaultLocale;

    private final HttpServletRequest request;

    private Locale currentLocale;

    public LocaleProducer(
            @ConfigProperty(name = "quarkus.locales") List<Locale> locales,
            @ConfigProperty(name = "quarkus.default-locale") Locale defaultLocale,
            HttpServletRequest request) {
        this.locales = locales;
        this.defaultLocale = defaultLocale;
        this.request = request;
    }

    @PostConstruct
    void init() {
        currentLocale = determineCurrentLocale();
    }

    @Produces
    @Dependent
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    private Locale determineCurrentLocale() {
        final Locale requestLocale = request.getLocale();
        if (requestLocale != null && locales.contains(requestLocale)) {
            return request.getLocale();
        }
        return defaultLocale;
    }
}
