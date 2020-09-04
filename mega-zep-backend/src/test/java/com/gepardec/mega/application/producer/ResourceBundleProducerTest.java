package com.gepardec.mega.application.producer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

public class ResourceBundleProducerTest {

    private ResourceBundleProducer producer;

    @BeforeEach
    void beforeEach() {
        producer = new ResourceBundleProducer();
    }

    @Test
    void createMessageResourceBundle_whenCalled_thenReturnsLoadedResourceBundle() {
        final ResourceBundle bundle = producer.createMessageResourceBundle();

        Assertions.assertNotNull(bundle);
    }
}
