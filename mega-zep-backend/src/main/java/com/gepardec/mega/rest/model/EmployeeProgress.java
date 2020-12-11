package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.User;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
@JsonSerialize(as = EmployeeProgress.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_EmployeeProgress.Builder.class)
public abstract class EmployeeProgress {

    @JsonProperty
    @Nullable
    public abstract String project();

    @JsonProperty
    public abstract String assigneeEmail();

    @JsonProperty
    public abstract State state();

    @JsonProperty
    public abstract Long stepId();

    public EmployeeProgress() {
        super();
    }

    public static Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_EmployeeProgress.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty public abstract Builder project(String project);

        @JsonProperty public abstract Builder assigneeEmail(String email);

        @JsonProperty public abstract Builder state(State state);

        @JsonProperty public abstract Builder stepId(Long stepId);

        @JsonProperty public abstract EmployeeProgress build();
    }
}
