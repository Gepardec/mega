package com.gepardec.mega.application.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class DurationSerializer extends JsonSerializer<Duration> {

    @Override
    public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            final String duration = String.format("%sd %sh %sm %ss",
                    value.toDaysPart(),
                    value.toHoursPart(),
                    value.toMinutesPart(),
                    value.toSecondsPart());
            gen.writeString(duration);
        }
    }
}
