package com.gepardec.mega.application.producer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ResourceBundleProducerTest {

    @Inject
    ResourceBundleProducer producer;

    @Test
    void init_whenCalled_thenInitializesResourceBundle() {
        final ResourceBundle bundle = producer.getResourceBundle(Locale.GERMAN);

        assertThat(bundle).isNotNull();
    }
}
