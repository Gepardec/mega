package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.time.LocalDate;

@AutoValue
@JsonSerialize(as = Step.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_StepEntry.Builder.class)
public abstract class StepEntry {

    @JsonProperty
    public abstract LocalDate date();

    @JsonProperty
    @Nullable
    public abstract Project project();

    @JsonProperty
    public abstract State state();

    @JsonProperty
    public abstract User owner();

    @JsonProperty
    public abstract User assignee();

    @JsonProperty
    public abstract Step step();

    public static StepEntry.Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_StepEntry.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.StepEntry.Builder date(LocalDate date);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.StepEntry.Builder project(Project project);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.StepEntry.Builder state(State state);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.StepEntry.Builder owner(User owner);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.StepEntry.Builder assignee(User assignee);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.StepEntry.Builder step(Step step);

        @JsonProperty
        public abstract StepEntry build();
    }
}
