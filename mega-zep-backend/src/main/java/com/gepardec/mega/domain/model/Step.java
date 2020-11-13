package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
@JsonSerialize(as = Step.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_Step.Builder.class)
public abstract class Step {

    @JsonProperty
    public abstract long dbId();

    @JsonProperty
    public abstract String name();

    @JsonProperty
    public abstract long ordinal();

    @JsonProperty
    @Nullable
    public abstract String role();

    public static Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_Step.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract com.gepardec.mega.domain.model.Step.Builder dbId(long dbId);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.Step.Builder name(String name);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.Step.Builder ordinal(long ordinal);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.Step.Builder role(String role);

        @JsonProperty
        public abstract Step build();
    }
}