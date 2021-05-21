package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.application.constant.DateTimeConstants;
import com.gepardec.mega.application.jackson.serializer.DurationSerializer;
import com.google.auto.value.AutoValue;

import java.time.Duration;
import java.time.LocalDateTime;

@AutoValue
@JsonSerialize(as = ApplicationInfo.class)
@JsonDeserialize(builder = ApplicationInfo.Builder.class)
public abstract class ApplicationInfo {

    public static Builder newBuilder() {
        return new com.gepardec.mega.rest.model.AutoValue_ApplicationInfo.Builder();
    }

    @JsonProperty
    public abstract String version();

    @JsonProperty
    @JsonFormat(pattern = DateTimeConstants.DATE_TIME_PATTERN)
    public abstract LocalDateTime buildDate();

    @JsonProperty
    public abstract Integer buildNumber();

    @JsonProperty
    public abstract String commit();

    @JsonProperty
    public abstract String branch();

    @JsonProperty
    @JsonFormat(pattern = DateTimeConstants.DATE_TIME_PATTERN)
    public abstract LocalDateTime startedAt();

    @JsonProperty
    @JsonSerialize(using = DurationSerializer.class)
    public abstract Duration upTime();
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder version(String version);

        public abstract Builder buildDate(LocalDateTime buildDate);

        public abstract Builder buildNumber(Integer Integer);

        public abstract Builder commit(String commit);

        public abstract Builder branch(String branch);

        public abstract Builder startedAt(LocalDateTime startedAt);

        public abstract Builder upTime(Duration upTime);

        public abstract ApplicationInfo build();
    }
}
