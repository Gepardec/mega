package com.gepardec.mega.application.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.Locale;
import java.util.ResourceBundle;

@ApplicationScoped
public class ResourceBundleProducer {

    @Produces
    @ApplicationScoped
    ResourceBundle createMessageResourceBundle() {
        return ResourceBundle.getBundle("messages", Locale.getDefault());
    }
}
