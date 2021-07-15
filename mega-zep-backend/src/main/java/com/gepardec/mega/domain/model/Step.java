
package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.experimental.*;
import lombok.extern.jackson.Jacksonized;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class Step {
    @JsonProperty
    private final long dbId;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final long ordinal;

    @JsonProperty
    private final Role role;
}


