package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.domain.model.ProjectState;
import com.gepardec.mega.domain.model.ProjectStep;
import com.google.auto.value.AutoValue;

// TODO Discuss: Rename all rest pojos to DTO?
@AutoValue
@JsonSerialize(as = ProjectEntryDTO.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_ProjectEntryDTO.Builder.class)
public abstract class ProjectEntryDTO {

    public ProjectEntryDTO() {
        super();
    }

    public static Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_ProjectEntryDTO.Builder();
    }

    @JsonProperty
    public abstract ProjectState state();

    @JsonProperty
    public abstract boolean preset();

    @JsonProperty
    public abstract String projectName();

    @JsonProperty
    public abstract ProjectStep step();

    @JsonProperty
    public abstract String currentMonthYear();

    @AutoValue.Builder
    public abstract static class Builder {

        @JsonProperty
        public abstract Builder state(ProjectState state);

        @JsonProperty
        public abstract Builder preset(boolean preset);

        @JsonProperty
        public abstract Builder projectName(String projectName);

        @JsonProperty
        public abstract Builder step(ProjectStep step);

        @JsonProperty
        public abstract Builder currentMonthYear(String currentMonthYear);

        @JsonProperty
        public abstract ProjectEntryDTO build();

    }
}
