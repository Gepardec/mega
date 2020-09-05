package com.gepardec.mega.domain.model.monthlyreport;

import java.time.LocalDateTime;

public class JourneyTimeEntry implements ProjectEntry {

    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private Task task;
    private JourneyDirection journeyDirection;

    public JourneyTimeEntry(LocalDateTime fromTime, LocalDateTime toTime, Task task, JourneyDirection journeyDirection) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.task = task;
        this.journeyDirection = journeyDirection;
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