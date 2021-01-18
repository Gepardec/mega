package com.gepardec.mega.domain.model.monthlyreport;

import com.google.auto.value.AutoValue;

import java.time.LocalDateTime;

@AutoValue
public abstract class JourneyTimeEntry implements ProjectEntry {

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

    public abstract Vehicle getVehicle();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder fromTime(LocalDateTime fromTime);

        public abstract Builder toTime(LocalDateTime toTime);

        public abstract Builder task(Task task);

        public abstract Builder workingLocation(WorkingLocation workingLocation);

        public abstract Builder journeyDirection(JourneyDirection journeyDirection);

        public abstract Builder vehicle(Vehicle vehicle);

        public abstract JourneyTimeEntry build();
    }
}
