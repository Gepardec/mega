package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.configuration.OAuthConfig;
import com.gepardec.mega.application.producer.GoogleVerifierProducer;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoogleVerifierProducerTest {

    @Mock
    private OAuthConfig oAuthConfig;

    @InjectMocks
    private GoogleVerifierProducer producer;

    @Test
    void createHttpTransport_whenCalled_thenReturnsHttpTransportInstance() {
        final HttpTransport transport = producer.createHttpTransport();

        Assertions.assertNotNull(transport);
    }

    @Test
    void createHttpTransport_whenCalledMultipleTimes_thenReturnsAlwaysNewHttpTransportInstance() {
        final HttpTransport transport1 = producer.createHttpTransport();
        final HttpTransport transport2 = producer.createHttpTransport();

        Assertions.assertNotEquals(transport1, transport2);
    }

    @Test
    void createJsonFactory_whenCalled_thenReturnsJacksonJsonFactory() {
        final JsonFactory factory = producer.createJsonFactory();

        Assertions.assertNotNull(factory);
    }

    @Test
    void createJsonFactory_whenCalledMultipleTimes_thenReturnsAlwaysNewJacksonJsonFactory() {
        final JsonFactory factory1 = producer.createJsonFactory();
        final JsonFactory factory2 = producer.createJsonFactory();

        Assertions.assertNotEquals(factory1, factory2);
    }

    @Test
    void createGoogleTokenVerifier_whenCalled_thenReturnsCreatedGoogleTokenVerifier() {
        when(oAuthConfig.getClientId()).thenReturn("client-id");
        when(oAuthConfig.getIssuer()).thenReturn("issuer");
        final HttpTransport expectedHttpTransport = new NetHttpTransport();
        final JsonFactory expectedJsonFactory = new JacksonFactory();

        final GoogleIdTokenVerifier verifier = producer.createGoogleTokenVerifier(expectedHttpTransport, expectedJsonFactory);

        Assertions.assertEquals("client-id", verifier.getAudience().iterator().next());
        Assertions.assertEquals("issuer", verifier.getIssuer());
        Assertions.assertEquals(expectedHttpTransport, verifier.getTransport());
        Assertions.assertEquals(expectedJsonFactory, verifier.getJsonFactory());
    }
}
