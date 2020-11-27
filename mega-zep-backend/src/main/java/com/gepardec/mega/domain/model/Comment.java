package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.State;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
@JsonSerialize(as = Comment.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_Comment.Builder.class)
public abstract class Comment {
    public static Comment.Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_Comment.Builder();
    }

    @JsonProperty
    @Nullable
    public abstract Long id();

    @JsonProperty
    @Nullable
    public abstract String message();

    @JsonProperty
    @Nullable
    public abstract String author();

    @JsonProperty
    @Nullable
    public abstract String updateDate();

    @JsonProperty
    @Nullable
    public abstract State state();

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract Comment.Builder id(Long id);

        @JsonProperty
        public abstract Comment.Builder message(String message);

        @JsonProperty
        public abstract Comment.Builder author(String author);

        @JsonProperty
        public abstract Comment.Builder state(State state);

        @JsonProperty
        public abstract Comment.Builder updateDate(String updateDate);

        @JsonProperty
        public abstract Comment build();
    }
}
