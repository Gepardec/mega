package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
class LocaleProducerTest {

    private static final Locale DEFAULT_LOCALE = Locale.GERMAN;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ApplicationConfig applicationConfig;

    private LocaleProducer producer;

    @BeforeEach
    void beforeEach() {
        lenient().when(applicationConfig.getLocales()).thenReturn(List.of(Locale.GERMAN, Locale.ENGLISH));
        lenient().when(applicationConfig.getDefaultLocale()).thenReturn(Locale.GERMAN);
        producer = new LocaleProducer(applicationConfig, request);
    }

    @Test
    void init_whenRequestHasNoLocale_thenCurrentLocaleIsDefaultLocale() {
        when(request.getLocale()).thenReturn(null);

        producer.init();
        final Locale actual = producer.getCurrentLocale();

        assertEquals(DEFAULT_LOCALE, actual);
    }

    @Test
    void init_whenRequestHasUnsupportedLocaleFRENCH_thenCurrentLocaleIsDefaultLocale() {
        when(request.getLocale()).thenReturn(Locale.FRENCH);

        producer.init();
        final Locale actual = producer.getCurrentLocale();

        assertEquals(DEFAULT_LOCALE, actual);
    }

    @Test
    void init_whenRequestHasLocaleGERMAN_thenCurrentLocaleIsGERMAN() {
        when(request.getLocale()).thenReturn(Locale.GERMAN);

        producer.init();
        final Locale actual = producer.getCurrentLocale();

        assertEquals(Locale.GERMAN, actual);
    }

    @Test
    void init_whenRequestHasLocaleENGLISH_thenCurrentLocaleIsENGLISH() {
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        producer.init();
        final Locale actual = producer.getCurrentLocale();

        assertEquals(Locale.ENGLISH, actual);
    }
}
