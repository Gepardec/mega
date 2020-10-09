package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
@JsonSerialize(as = Employee.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_Employee.Builder.class)
public abstract class Employee {

    @Nullable @JsonProperty public abstract String userId();
    @Nullable @JsonProperty public abstract String email();
    @Nullable @JsonProperty public abstract String title();
    @Nullable @JsonProperty public abstract String firstName();
    @Nullable @JsonProperty public abstract String sureName();
    @Nullable @JsonProperty public abstract String salutation();
    @Nullable @JsonProperty public abstract String releaseDate();
    @Nullable @JsonProperty public abstract String workDescription();
    @Nullable @JsonProperty public abstract Integer role();
    @JsonProperty public abstract boolean active();

    public static Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_Employee.Builder().active(false);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty public abstract Builder userId(String userId);
        @JsonProperty public abstract Builder email(String email);
        @JsonProperty public abstract Builder title(String title);
        @JsonProperty public abstract Builder firstName(String firstName);
        @JsonProperty public abstract Builder sureName(String sureName);
        @JsonProperty public abstract Builder salutation(String salutation);
        @JsonProperty public abstract Builder releaseDate(String releaseDate);
        @JsonProperty public abstract Builder workDescription(String workDescription);
        @JsonProperty public abstract Builder role(Integer role);
        @JsonProperty public abstract Builder active(boolean active);
        @JsonProperty public abstract Employee build();
    }
}
