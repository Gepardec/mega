package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.application.constant.DateTimeConstants;
import com.gepardec.mega.application.jackson.serializer.DurationSerializer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class ApplicationInfo {
    @JsonProperty
    private final String version;

    @JsonProperty
    @JsonFormat(pattern = DateTimeConstants.DATE_TIME_PATTERN)
    private final LocalDateTime buildDate;

    @JsonProperty
    private final Integer buildNumber;

    @JsonProperty
    private final String commit;

    @JsonProperty
    private final String branch;

    @JsonProperty
    @JsonFormat(pattern = DateTimeConstants.DATE_TIME_PATTERN)
    private final LocalDateTime startedAt;

    @JsonProperty
    @JsonSerialize(using = DurationSerializer.class)
    private final Duration upTime;
}