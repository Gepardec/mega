package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.project.ProjectStep;
import com.google.auto.value.AutoValue;

import java.time.LocalDate;

@AutoValue
@JsonSerialize(as = ProjectEntry.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_ProjectEntry.Builder.class)
public abstract class ProjectEntry {

    public static Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_ProjectEntry.Builder();
    }

    // id is set in entity class
    // creation_date is set in entity class
    // entry_date
    @JsonProperty
    public abstract LocalDate date();

    // name
    @JsonProperty
    public abstract String name();

    // TODO: preset unknown where to set
    // step
    @JsonProperty
    public abstract ProjectStep step();

    // update_date set in entity class
    public abstract User assignee();

    public abstract User owner();

    public abstract Project project();

    @AutoValue.Builder
    public abstract static class Builder {

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.ProjectEntry.Builder date(LocalDate date);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.ProjectEntry.Builder name(String name);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.ProjectEntry.Builder step(ProjectStep step);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.ProjectEntry.Builder assignee(User assignee);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.ProjectEntry.Builder owner(User owner);

        @JsonProperty
        public abstract com.gepardec.mega.domain.model.ProjectEntry.Builder project(Project project);

        @JsonProperty
        public abstract ProjectEntry build();
    }
}
