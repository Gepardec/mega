package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class Project {
    @JsonProperty
    private final String projectId;

    @Nullable
    @JsonProperty
    private final String description;

    @JsonProperty
    private final LocalDate startDate;

    @JsonProperty
    @Nullable
    private final LocalDate endDate;

    @JsonProperty
    private final List<String> employees;

    @JsonProperty
    private final List<String> leads;

    @JsonProperty
    private final List<String> categories;
}