package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonSerialize(as = ValidationViolation.class)
public abstract class ValidationViolation {

    @JsonProperty("property")
    public abstract String property();

    @JsonProperty("message")
    public abstract String message();

    @JsonCreator
    public static ValidationViolation of(
            final @JsonProperty("name") String name,
            final @JsonProperty("property") String property) {
        return new com.gepardec.mega.domain.model.AutoValue_ValidationViolation(name, property);
    }
}
