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
import java.util.Set;

// Represents the logged user in mega.

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class User {
    @JsonProperty
    private final long dbId;

    @JsonProperty
    private final String userId;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final String firstname;

    @JsonProperty
    private final String lastname;

    @JsonProperty
    @Nullable
    private final LocalDate releaseDate;

    @JsonProperty
    private final Set<Role> roles;
}
