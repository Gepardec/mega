package com.gepardec.mega.domain.model;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class UserContext {

    @Nullable
    public abstract User user();

    public abstract boolean loggedIn();

    public static UserContext.Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_UserContext.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder user(User user);

        public abstract Builder loggedIn(boolean loggedIn);

        public abstract UserContext build();
    }
}
