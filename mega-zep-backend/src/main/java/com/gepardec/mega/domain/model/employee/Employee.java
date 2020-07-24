package com.gepardec.mega.domain.model.employee;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Employee {

    @Nullable public abstract String userId();
    @Nullable public abstract String email();
    @Nullable public abstract String title();
    @Nullable public abstract String firstName();
    @Nullable public abstract String sureName();
    @Nullable public abstract String salutation();
    @Nullable public abstract String releaseDate();
    @Nullable public abstract String workDescription();
    @Nullable public abstract Integer role();
    public abstract boolean active();

    public static Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_Employee.Builder()
                .active(false);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder userId(String userId);
        public abstract Builder email(String email);
        public abstract Builder title(String title);
        public abstract Builder firstName(String firstName);
        public abstract Builder sureName(String sureName);
        public abstract Builder salutation(String salutation);
        public abstract Builder releaseDate(String releaseDate);
        public abstract Builder workDescription(String workDescription);
        public abstract Builder role(Integer role);
        public abstract Builder active(boolean active);
        public abstract Employee build();
    }
}
