package com.gepardec.mega.application.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.time.Duration;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DurationSerializerTest {

    private JsonGenerator jsonGeneratorMock;

    private SerializerProvider serializerProviderMock;

    private DurationSerializer serializer;

    @BeforeEach
    void beforeEach() throws IOException {
        serializer = new DurationSerializer();

        jsonGeneratorMock = spy(JsonGenerator.class);
        doNothing().when(jsonGeneratorMock).writeString(ArgumentMatchers.anyString());

        serializerProviderMock = mock(SerializerProvider.class);
    }

    @Test
    void serialize_whenNull_thenDoesNotCallJsonGenerator() throws Exception {
        serializer.serialize(null, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock, times(0)).writeString(anyString());
    }

    @Test
    void serialize_whenDuration_thenDoesCallJsonGeneratorWithSerializedString() throws Exception {
        final Duration duration = Duration.ofDays(1).plusHours(1).plusMinutes(1).plusSeconds(1);
        final String expected = "1d 1h 1m 1s";

        serializer.serialize(duration, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock, times(1)).writeString(expected);
    }
}
