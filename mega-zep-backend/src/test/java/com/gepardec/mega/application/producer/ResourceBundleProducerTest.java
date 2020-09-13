package com.gepardec.mega.application.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceBundleProducerTest {

    private static final Locale EMPTY_LOCALE = new Locale("", "", "");

    @Mock
    private HttpServletRequest request;

    private ResourceBundleProducer producer;

    @BeforeEach
    void beforeEach() {
        final Locale defaultLocale = Locale.GERMAN;
        Locale.setDefault(defaultLocale);
        producer = new ResourceBundleProducer(List.of(Locale.GERMAN, Locale.ENGLISH), defaultLocale, request);
    }

    @Test
    void init_whenRequestHasNoLocale_thenUsesEmptyLocaleForDefaultBundle() {
        when(request.getLocale()).thenReturn(null);

        producer.init();
        final ResourceBundle actual = producer.getResourceBundle();

        assertEquals(EMPTY_LOCALE, actual.getLocale());
    }

    @Test
    void init_whenRequestHasUnsupportedLocaleFRENCH_thenUsesEmptyLocaleForDefaultBundle() {
        when(request.getLocale()).thenReturn(Locale.FRENCH);

        producer.init();
        final ResourceBundle actual = producer.getResourceBundle();

        assertEquals(EMPTY_LOCALE, actual.getLocale());
    }

    @Test
    void init_whenRequestHasLocaleGERMANIsDefaultBundle_thenUsesEmptyLocaleForDefaultBundle() {
        when(request.getLocale()).thenReturn(Locale.GERMAN);

        producer.init();
        final ResourceBundle actual = producer.getResourceBundle();

        assertEquals(EMPTY_LOCALE, actual.getLocale());
    }

    @Test
    void init_whenRequestHasSupportedLocaleENGLISH_thenUsesProvidedLocale() {
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        producer.init();
        final ResourceBundle actual = producer.getResourceBundle();

        assertEquals(Locale.ENGLISH, actual.getLocale());
    }
}
