package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.project.ProjectStep;
import com.google.auto.value.AutoValue;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class ProjectEntry {


    @JsonProperty
    private final LocalDate date;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final ProjectStep step;

    private final User assignee;

    private final User owner;

    private final Project project;
}
