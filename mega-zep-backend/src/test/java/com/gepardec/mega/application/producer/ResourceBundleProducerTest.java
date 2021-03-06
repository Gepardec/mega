package com.gepardec.mega.application.producer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.ResourceBundle;

@ExtendWith(MockitoExtension.class)
class ResourceBundleProducerTest {

    private ResourceBundleProducer producer;

    @BeforeEach
    void beforeEach() {
        producer = new ResourceBundleProducer();
    }

    @Test
    void init_whenCalled_thenInitializesResourceBundle() {
        final ResourceBundle bundle = producer.getResourceBundle(Locale.GERMAN);

        Assertions.assertNotNull(bundle);
    }
}
