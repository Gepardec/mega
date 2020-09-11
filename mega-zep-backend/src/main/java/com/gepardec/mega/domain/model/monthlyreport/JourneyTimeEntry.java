package com.gepardec.mega.domain.model.monthlyreport;

import java.time.LocalDateTime;

public class JourneyTimeEntry implements ProjectEntry {

    private final LocalDateTime fromTime;
    private final LocalDateTime toTime;
    private final Task task;
    private final JourneyDirection journeyDirection;

    JourneyTimeEntry(LocalDateTime fromTime, LocalDateTime toTime, Task task, JourneyDirection journeyDirection) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.task = task;
        this.journeyDirection = journeyDirection;
    }

    public static JourneyTimeEntry of(LocalDateTime fromTime, LocalDateTime toTime, Task task, JourneyDirection journeyDirection) {
        return new JourneyTimeEntry(fromTime, toTime, task, journeyDirection);
    }


    @Override
    public LocalDateTime getFromTime() {
        return fromTime;
    }

    @Override
    public LocalDateTime getToTime() {
        return toTime;
    }

    @Override
    public Task getTask() {
        return task;
    }

    public JourneyDirection getJourneyDirection() {
        return journeyDirection;
    }
}
