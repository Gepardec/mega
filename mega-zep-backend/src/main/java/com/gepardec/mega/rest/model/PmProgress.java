package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.State;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import javax.validation.constraints.Null;

@AutoValue
@JsonSerialize(as = PmProgress.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_PmProgress.Builder.class)
public abstract class PmProgress {

    @JsonProperty
    @Nullable
    public abstract String project();

    @JsonProperty
    public abstract String assigneeEmail();

    @JsonProperty
    @Nullable
    public abstract String firstname();

    @JsonProperty
    @Nullable
    public abstract String lastname();

    @JsonProperty
    public abstract State state();

    @JsonProperty
    public abstract Long stepId();

    public PmProgress() {
        super();
    }

    public static Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_PmProgress.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty public abstract Builder project(String project);

        @JsonProperty public abstract Builder assigneeEmail(String email);

        @JsonProperty public abstract Builder state(State state);

        @JsonProperty public abstract Builder stepId(Long stepId);

        @JsonProperty public abstract Builder firstname(String firstname);

        @JsonProperty public abstract Builder lastname(String lastname);

        @JsonProperty public abstract PmProgress build();
    }
}
