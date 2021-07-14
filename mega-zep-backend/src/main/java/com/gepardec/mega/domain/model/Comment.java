package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
@JsonSerialize(as = Comment.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_Comment.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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
    public abstract String authorEmail();

    @JsonProperty
    @Nullable
    public abstract String authorName();

    @JsonProperty
    @Nullable
    public abstract String updateDate();

    @JsonProperty
    @Nullable
    public abstract EmployeeState state();

    @AutoValue.Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class Builder {
        @JsonProperty
        public abstract Comment.Builder id(Long id);

        @JsonProperty
        public abstract Comment.Builder message(String message);

        @JsonProperty
        public abstract Comment.Builder authorEmail(String authorEmail);

        @JsonProperty
        public abstract Comment.Builder authorName(String authorName);

        @JsonProperty
        public abstract Comment.Builder state(EmployeeState employeeState);

        @JsonProperty
        public abstract Comment.Builder updateDate(String updateDate);

        @JsonProperty
        public abstract Comment build();
    }
}
