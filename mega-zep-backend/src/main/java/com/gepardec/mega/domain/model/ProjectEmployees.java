package com.gepardec.mega.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ProjectEmployees {

    public abstract String projectId();
    public abstract List<String> employees();

    public static ProjectEmployees.Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_ProjectEmployees.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder projectId(String projectId);
        public abstract Builder employees(List<String> employees);

        public abstract ProjectEmployees build();
    }

}
