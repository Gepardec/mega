package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.project.State;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
@JsonSerialize(as = ProjectManagementEntry.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_ProjectManagementEntry.Builder.class)
public abstract class ProjectManagementEntry {

    public static ProjectManagementEntry.Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_ProjectManagementEntry.Builder();
    }

    @JsonProperty
    public abstract String projectName();

    @JsonProperty
    public abstract State controlProjectState();

    @JsonProperty
    public abstract List<ManagementEntry> entries();

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract ProjectManagementEntry.Builder projectName(String projectName);

        @JsonProperty
        public abstract ProjectManagementEntry.Builder entries(List<ManagementEntry> entries);

        @JsonProperty
        public abstract ProjectManagementEntry build();
    }

}
