package com.gepardec.mega.domain.model;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class SecurityContext {

    public static SecurityContext.Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_SecurityContext.Builder();
    }

    @Nullable
    public abstract String email();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract SecurityContext.Builder email(String email);

        public abstract SecurityContext build();
    }
}
