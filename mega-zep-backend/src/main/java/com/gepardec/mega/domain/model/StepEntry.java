package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class StepEntry {
    @JsonProperty
    private final LocalDate date;

    @JsonProperty
    @Nullable
    private final Project project;

    @JsonProperty
    private final State state;

    @JsonProperty
    private final User owner;

    @JsonProperty
    private final User assignee;

    @JsonProperty
    private final Step step;
}
