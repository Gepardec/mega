package com.gepardec.mega.domain.model;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class UserContext {

    public static UserContext.Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_UserContext.Builder();
    }

    @Nullable
    public abstract User user();
    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder user(User user);

        public abstract UserContext build();
    }
}
