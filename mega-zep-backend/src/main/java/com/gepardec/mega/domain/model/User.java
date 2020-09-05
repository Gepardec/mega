package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

/**
 * Represents the logged user in mega.
 */
@AutoValue
@JsonSerialize(as = User.class)
@JsonDeserialize(builder = com.gepardec.mega.domain.model.AutoValue_User.Builder.class)
public abstract class User {

    @JsonProperty public abstract String userId();
    @JsonProperty public abstract String email();
    @JsonProperty public abstract String firstname();
    @JsonProperty public abstract String lastname();
    @Nullable @JsonProperty public abstract Role role();
    @Nullable @JsonProperty public abstract String pictureUrl();

    public static Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_User.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty public abstract Builder userId(String userId);
        @JsonProperty public abstract Builder email(String email);
        @JsonProperty public abstract Builder firstname(String firstname);
        @JsonProperty public abstract Builder lastname(String lastname);
        @JsonProperty public abstract Builder role(Role role);
        @JsonProperty public abstract Builder pictureUrl(String pictureUrl);
        @JsonProperty public abstract User build();
    }
}
