package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gepardec.mega.domain.model.ProjectState;
import com.gepardec.mega.domain.model.ProjectStep;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

// TODO Discuss: Rename all rest pojos to DTO?
@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class ProjectEntryDto {
    @JsonProperty
    private final ProjectState state;

    @JsonProperty
    private final boolean preset;

    @JsonProperty
    private final String projectName;

    @JsonProperty
    private final ProjectStep step;

    @JsonProperty
    private final String currentMonthYear;
}
