package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class Comment {
    @JsonProperty
    @Nullable
    private final Long id;

    @JsonProperty
    @Nullable
    private final String message;

    @JsonProperty
    @Nullable
    private final String authorEmail;

    @JsonProperty
    @Nullable
    private final String authorName;

    @JsonProperty
    @Nullable
    private final String updateDate;

    @JsonProperty
    @Nullable
    private final EmployeeState state;
}
