package com.gepardec.mega.domain.model;

import com.gepardec.mega.application.security.Role;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

/**
 * Represents the logged user in mega.
 */
@AutoValue
public abstract class User {

    public abstract String email();
    public abstract String firstname();
    public abstract String lastname();
    public abstract Role role();
    public abstract String pictureUrl();

    public static Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_User.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @Nullable public abstract Builder email(String email);
        @Nullable public abstract Builder firstname(String firstname);
        @Nullable public abstract Builder lastname(String lastname);
        @Nullable public abstract Builder role(Role role);
        @Nullable public abstract Builder pictureUrl(String pictureUrl);
        public abstract User build();
    }
}
