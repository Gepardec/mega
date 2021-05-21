package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.List;

@AutoValue
@JsonSerialize(as = Project.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_Project.Builder.class)
public abstract class Project {

    public static Project.Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_Project.Builder();
    }

    @JsonProperty
    public abstract String projectId();

    @Nullable
    @JsonProperty
    public abstract String description();

    @JsonProperty
    public abstract List<String> employees();

    @JsonProperty
    public abstract List<String> leads();

    @JsonProperty
    public abstract List<String> categories();
    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract Project.Builder projectId(String projectId);

        @JsonProperty
        public abstract Project.Builder description(String description);

        @JsonProperty
        public abstract Project.Builder employees(List<String> employees);

        @JsonProperty
        public abstract Project.Builder leads(List<String> leads);

        @JsonProperty
        public abstract Project.Builder categories(List<String> categories);

        @JsonProperty
        public abstract Project build();
    }
}