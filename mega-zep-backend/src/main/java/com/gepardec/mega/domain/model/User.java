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
import java.time.LocalDate;
import java.util.Set;

/**
 * Represents the logged user in mega.
 */
@AutoValue
@JsonSerialize(as = User.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_User.Builder.class)
public abstract class User {

    public static Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_User.Builder();
    }

    @JsonProperty
    public abstract long dbId();

    @JsonProperty
    public abstract String userId();

    @JsonProperty
    public abstract String email();

    @JsonProperty
    public abstract String firstname();

    @JsonProperty
    public abstract String lastname();

    @JsonProperty
    @Nullable
    public abstract LocalDate releaseDate();

    @JsonProperty
    public abstract Set<Role> roles();

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract Builder dbId(long dbId);

        @JsonProperty
        public abstract Builder userId(String userId);

        @JsonProperty
        public abstract Builder email(String email);

        @JsonProperty
        public abstract Builder firstname(String firstname);

        @JsonProperty
        public abstract Builder lastname(String lastname);

        @JsonProperty
        public abstract Builder releaseDate(LocalDate releaseDate);

        @JsonProperty
        public abstract Builder roles(Set<Role> roles);

        @JsonProperty
        public abstract User build();
    }
}


/*
package com.gepardec.mega.domain.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.*;
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
 */
