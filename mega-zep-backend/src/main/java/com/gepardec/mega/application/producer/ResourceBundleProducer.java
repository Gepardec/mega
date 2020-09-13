package com.gepardec.mega.application.producer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Locale;
import java.util.ResourceBundle;

@RequestScoped
public class ResourceBundleProducer {

    private final Locale currentLocale;

    private ResourceBundle resourceBundle;

    @Inject
    public ResourceBundleProducer(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

    @PostConstruct
    public void init() {
        resourceBundle = ResourceBundle.getBundle("messages",
                currentLocale,
                // Otherwise it will fallback to english depending on the current jvm language settings,
                // but we always want to fallback to the base bundle which is german.
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }

    @Produces
    @Dependent
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
