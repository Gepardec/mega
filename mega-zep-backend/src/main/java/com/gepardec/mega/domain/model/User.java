package com.gepardec.mega.domain.model;

import com.gepardec.mega.application.security.Role;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

/**
 * Represents the logged user in mega.
 */
@AutoValue
public abstract class User {

    @Nullable public abstract String email();
    @Nullable public abstract String firstname();
    @Nullable public abstract String lastname();
    @Nullable public abstract Role role();
    @Nullable public abstract String pictureUrl();

    public static Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_User.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder email(String email);
        public abstract Builder firstname(String firstname);
        public abstract Builder lastname(String lastname);
        public abstract Builder role(Role role);
        public abstract Builder pictureUrl(String pictureUrl);
        public abstract User build();
    }
}
