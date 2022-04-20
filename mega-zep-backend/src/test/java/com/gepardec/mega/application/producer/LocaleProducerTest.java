package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@QuarkusTest
class LocaleProducerTest {

    private static final Locale DEFAULT_LOCALE = Locale.GERMAN;

    @Inject
    LocaleProducer producer;

    private HttpServletRequest requestSpy;

    @BeforeEach
    void beforeEach() {
        requestSpy = spy(HttpServletRequest.class);
        ApplicationConfig applicationConfigSpy = spy(ApplicationConfig.class);

        lenient().when(applicationConfigSpy.getLocales()).thenReturn(List.of(Locale.GERMAN, Locale.ENGLISH));
        lenient().when(applicationConfigSpy.getDefaultLocale()).thenReturn(Locale.GERMAN);

        producer = new LocaleProducer(applicationConfigSpy, requestSpy);
    }

    @Test
    void init_whenRequestHasNoLocale_thenCurrentLocaleIsDefaultLocale() {
        when(requestSpy.getLocale()).thenReturn(null);

        producer.init();
        final Locale actual = producer.getCurrentLocale();

        assertThat(actual).isEqualTo(DEFAULT_LOCALE);
    }

    @Test
    void init_whenRequestHasUnsupportedLocaleFRENCH_thenCurrentLocaleIsDefaultLocale() {
        when(requestSpy.getLocale()).thenReturn(Locale.FRENCH);

        producer.init();
        final Locale actual = producer.getCurrentLocale();

        assertThat(actual).isEqualTo(DEFAULT_LOCALE);
    }

    @Test
    void init_whenRequestHasLocaleGERMAN_thenCurrentLocaleIsGERMAN() {
        when(requestSpy.getLocale()).thenReturn(Locale.GERMAN);

        producer.init();
        final Locale actual = producer.getCurrentLocale();

        assertThat(actual).isEqualTo(Locale.GERMAN);
    }

    @Test
    void init_whenRequestHasLocaleENGLISH_thenCurrentLocaleIsENGLISH() {
        when(requestSpy.getLocale()).thenReturn(Locale.ENGLISH);

        producer.init();
        final Locale actual = producer.getCurrentLocale();

        assertThat(actual).isEqualTo(Locale.ENGLISH);
    }
}
