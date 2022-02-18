package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class PmProgress {
    @JsonProperty
    @Nullable
    private final String project;

    @JsonProperty
    private final String assigneeEmail;

    @JsonProperty
    @Nullable
    private final String firstname;

    @JsonProperty
    @Nullable
    private final String lastname;

    @JsonProperty
    private final EmployeeState state;

    @JsonProperty
    private final Long stepId;
}