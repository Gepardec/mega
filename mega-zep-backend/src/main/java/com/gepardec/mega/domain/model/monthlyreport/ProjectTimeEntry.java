package com.gepardec.mega.domain.model.monthlyreport;

import java.time.LocalDateTime;

public class ProjectTimeEntry implements ProjectEntry {

    private final LocalDateTime fromTime;
    private final LocalDateTime toTime;
    private final Task task;

    ProjectTimeEntry(LocalDateTime fromTime, LocalDateTime toTime, Task task) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.task = task;
    }

    public static ProjectTimeEntry of(LocalDateTime fromTime, LocalDateTime toTime, Task task) {
        return new ProjectTimeEntry(fromTime, toTime, task);
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
}
