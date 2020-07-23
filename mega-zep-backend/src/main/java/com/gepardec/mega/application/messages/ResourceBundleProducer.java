package com.gepardec.mega.application.messages;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.Locale;
import java.util.ResourceBundle;

@ApplicationScoped
public class ResourceBundleProducer {

    private ResourceBundle mailMessages;

    @Produces
    @ApplicationScoped
    ResourceBundle getMessages() {
        return ResourceBundle.getBundle("messages", Locale.getDefault());
    }
}
