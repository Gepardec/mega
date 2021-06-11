package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonSerialize(as = Step.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_Step.Builder.class)
public abstract class Step {

    public static Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_Step.Builder();
    }

    @JsonProperty
    public abstract long dbId();

    @JsonProperty
    public abstract String name();

    @JsonProperty
    public abstract long ordinal();

    @JsonProperty
    public abstract Role role();

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract com.gepardec.mega.domain.model.Step.Builder dbId(long dbId);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.Step.Builder name(String name);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.Step.Builder ordinal(long ordinal);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.Step.Builder role(Role role);

        @JsonProperty
        public abstract Step build();
    }
}
