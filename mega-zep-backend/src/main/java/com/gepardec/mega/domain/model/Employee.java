package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
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
public class Employee {


    @Nullable
    @JsonProperty
    private final String userId;

    @Nullable
    @JsonProperty
    private final String email;

    @Nullable
    @JsonProperty
    private final String title;

    @Nullable
    @JsonProperty
    private final String firstname;

    @Nullable
    @JsonProperty
    private final String lastname;

    @Nullable
    @JsonProperty
    private final String salutation;

    @Nullable
    @JsonProperty
    private final String releaseDate;

    @Nullable
    @JsonProperty
    private final String workDescription;

    @Nullable
    @JsonProperty
    private final String language;

    @JsonProperty
    private final boolean active;
}
