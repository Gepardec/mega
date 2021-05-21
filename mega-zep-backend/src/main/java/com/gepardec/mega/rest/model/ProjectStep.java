package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.domain.model.Employee;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonSerialize(as = ProjectStep.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_ProjectStep.Builder.class)
public abstract class ProjectStep {

    public static ProjectStep.Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_ProjectStep.Builder();
    }

    @JsonProperty
    public abstract Long stepId();

    @JsonProperty
    public abstract Employee employee();

    @JsonProperty
    public abstract String projectName();

    @JsonProperty
    public abstract String currentMonthYear();
    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract ProjectStep.Builder stepId(Long stepId);

        @JsonProperty
        public abstract ProjectStep.Builder employee(Employee employee);

        @JsonProperty
        public abstract ProjectStep.Builder projectName(String projectName);

        @JsonProperty
        public abstract ProjectStep.Builder currentMonthYear(String currentMonthYear);

        @JsonProperty
        public abstract ProjectStep build();

    }
}
