package com.gepardec.mega.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FinishedAndTotalComments {

    public abstract Long finishedComments();
    public abstract Long totalComments();

    public static FinishedAndTotalComments.Builder builder() {
        return new com.gepardec.mega.domain.model.AutoValue_FinishedAndTotalComments.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder finishedComments(Long finishedComments);
        public abstract Builder totalComments(Long totalComments);

        public abstract FinishedAndTotalComments build();
    }
}
