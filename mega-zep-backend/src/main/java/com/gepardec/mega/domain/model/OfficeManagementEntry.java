package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
@JsonSerialize(as = OfficeManagementEntry.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_OfficeManagementEntry.Builder.class)
public abstract class OfficeManagementEntry {

    @JsonProperty
    public abstract Employee employee();

    @JsonProperty
    public abstract List<StepEntry> stepEntries();

    @JsonProperty
    public abstract List<Comment> comments();

    public static OfficeManagementEntry.Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_OfficeManagementEntry.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract OfficeManagementEntry.Builder employee(Employee employee);

        @JsonProperty
        public abstract OfficeManagementEntry.Builder stepEntries(List<StepEntry> stepEntries);

        @JsonProperty
        public abstract OfficeManagementEntry.Builder comments(List<Comment> comments);

        @JsonProperty
        public abstract OfficeManagementEntry build();
    }
}
