package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gepardec.mega.domain.model.ProjectState;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class ProjectManagementEntry {
    @JsonProperty
    private final String projectName;

    @JsonProperty
    @Nullable
    private final ProjectState controlProjectState;

    @JsonProperty
    @Nullable
    private final ProjectState controlBillingState;

    @JsonProperty
    @Nullable
    private final Boolean presetControlProjectState;

    @JsonProperty
    @Nullable
    private final Boolean presetControlBillingState;

    @JsonProperty
    private final List<ManagementEntry> entries;

    @JsonProperty
    private final Duration aggregatedBillableWorkTimeInSeconds;

    @JsonProperty
    private final Duration aggregatedNonBillableWorkTimeInSeconds;


}
