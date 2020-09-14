package com.gepardec.mega.application.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DurationSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private DurationSerializer serializer;

    @BeforeEach
    void beforeEach() {
        serializer = new DurationSerializer();
    }

    @Test
    void serialize_whenNull_thenDoesNotCallJsonGenerator() throws Exception {
        serializer.serialize(null, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(0)).writeString(anyString());
    }

    @Test
    void serialize_whenDuration_thenDoesCallJsonGeneratorWithSerializedString() throws Exception {
        final Duration duration = Duration.ofDays(1).plusHours(1).plusMinutes(1).plusSeconds(1);
        final String expected = "1d 1h 1m 1s";

        serializer.serialize(duration, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeString(eq(expected));
    }
}
