package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.domain.model.Employee;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonSerialize(as = NewCommentEntry.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_NewCommentEntry.Builder.class)
public abstract class NewCommentEntry {

    @JsonProperty
    public abstract Long stepId();

    @JsonProperty
    public abstract Employee employee();

    @JsonProperty
    public abstract String comment();

    @JsonProperty
    public abstract String assigneeEmail();

    public NewCommentEntry() {
        super();
    }

    public static NewCommentEntry.Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_NewCommentEntry.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty public abstract NewCommentEntry.Builder stepId(Long stepId);

        @JsonProperty public abstract NewCommentEntry.Builder employee(Employee employee);

        @JsonProperty public abstract NewCommentEntry.Builder comment(String comment);

        @JsonProperty public abstract NewCommentEntry.Builder assigneeEmail(String assigneeEmail);

        @JsonProperty public abstract NewCommentEntry build();

    }


}
