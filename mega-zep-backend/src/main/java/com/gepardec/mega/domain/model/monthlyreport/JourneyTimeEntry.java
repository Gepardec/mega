package com.gepardec.mega.domain.model.monthlyreport;

import com.google.auto.value.AutoValue;

import java.time.LocalDateTime;

@AutoValue
public abstract class JourneyTimeEntry implements ProjectEntry {

    /**
     * @deprecated Us {@link #of(LocalDateTime, LocalDateTime, Task, JourneyDirection, WorkingLocation)} instead
     */
    @Deprecated(forRemoval = true)
    public static JourneyTimeEntry of(LocalDateTime fromTime, LocalDateTime toTime, Task task, JourneyDirection journeyDirection) {
        return of(fromTime, toTime, task, journeyDirection, WorkingLocation.MAIN);
    }

    public static JourneyTimeEntry of(LocalDateTime fromTime, LocalDateTime toTime, Task task, JourneyDirection journeyDirection,
            WorkingLocation workingLocation) {
        return newBuilder().fromTime(fromTime)
                .toTime(toTime)
                .task(task)
                .journeyDirection(journeyDirection)
                .workingLocation(workingLocation)
                .build();
    }

    public static Builder newBuilder() {
        return new com.gepardec.mega.domain.model.monthlyreport.AutoValue_JourneyTimeEntry.Builder();
    }

    @Override
    public abstract LocalDateTime getFromTime();

    @Override
    public abstract LocalDateTime getToTime();

    @Override
    public abstract Task getTask();

    @Override
    public abstract WorkingLocation getWorkingLocation();

    public abstract JourneyDirection getJourneyDirection();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder fromTime(LocalDateTime fromTime);

        public abstract Builder toTime(LocalDateTime toTime);

        public abstract Builder task(Task task);

        public abstract Builder workingLocation(WorkingLocation workingLocation);

        public abstract Builder journeyDirection(JourneyDirection journeyDirection);

        public abstract JourneyTimeEntry build();
    }
}
