package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.State;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;
import java.util.List;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class ManagementEntry {

    @JsonProperty
    private final Employee employee;

    @JsonProperty
    private final State employeeCheckState;

    @JsonProperty
    private final State customerCheckState;

    @JsonProperty
    private final State internalCheckState;

    @JsonProperty
    private final State projectCheckState;

    @JsonProperty
    @Nullable
    private final List<PmProgress> employeeProgresses;

    @JsonProperty
    private final long totalComments;

    @JsonProperty
    private final long finishedComments;

    @JsonProperty
    private final String entryDate;
}
