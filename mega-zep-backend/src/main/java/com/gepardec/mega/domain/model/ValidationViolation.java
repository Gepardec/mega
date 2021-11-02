package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode
@Jacksonized
@Accessors(fluent = true)
@Getter
@Builder(builderClassName = "Builder")
public class ValidationViolation {

    @JsonProperty("property")
    private final String property;

    @JsonProperty("message")
    private final String message;
}
