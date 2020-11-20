package com.gepardec.mega.rest.model;

import com.gepardec.mega.domain.model.State;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.domain.model.Employee;

import com.google.auto.value.AutoValue;

@AutoValue
@JsonSerialize(as = ManagementEntry.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_OfficeManagementEntry.Builder.class)
public abstract class ManagementEntry {

    @JsonProperty
    public abstract Employee employee();

    @JsonProperty
    public abstract State employeeCheckState();

    @JsonProperty
    public abstract State customerCheckState();

    @JsonProperty
    public abstract State internalCheckState();

    @JsonProperty
    public abstract State projectCheckState();

    @JsonProperty
    public abstract long totalComments();

    @JsonProperty
    public abstract long finishedComments();

    public static Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_ManagementEntry.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty public abstract Builder employee(Employee employee);

        @JsonProperty public abstract Builder employeeCheckState(State state);

        @JsonProperty public abstract Builder customerCheckState(State state);

        @JsonProperty public abstract Builder internalCheckState(State state);

        @JsonProperty public abstract Builder projectCheckState(State state);

        @JsonProperty public abstract Builder totalComments(long totalComments);

        @JsonProperty public abstract Builder finishedComments(long totalComments);

        @JsonProperty public abstract ManagementEntry build();
    }
}
