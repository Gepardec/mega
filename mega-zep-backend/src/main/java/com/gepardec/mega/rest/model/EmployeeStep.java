package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.domain.model.Employee;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonSerialize(as = EmployeeStep.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_EmployeeStep.Builder.class)
public abstract class EmployeeStep {

    public EmployeeStep() {
        super();
    }

    public static Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_EmployeeStep.Builder();
    }

    @JsonProperty
    public abstract Long stepId();

    @JsonProperty
    public abstract Employee employee();

    @JsonProperty
    public abstract String currentMonthYear();

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract Builder stepId(Long stepId);

        @JsonProperty
        public abstract Builder employee(Employee employee);

        @JsonProperty
        public abstract Builder currentMonthYear(String currentMonthYear);

        @JsonProperty
        public abstract EmployeeStep build();

    }

}
