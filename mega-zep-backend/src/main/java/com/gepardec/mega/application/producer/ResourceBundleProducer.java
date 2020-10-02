package com.gepardec.mega.application.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import java.util.Locale;
import java.util.ResourceBundle;

@ApplicationScoped
public class ResourceBundleProducer {

    @Produces
    @Dependent
    public ResourceBundle getResourceBundle(final Locale locale) {
        return ResourceBundle.getBundle("messages",
                locale,
                // Otherwise it will fallback to english depending on the current jvm language settings,
                // but we always want to fallback to the base bundle which is german.
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
}
