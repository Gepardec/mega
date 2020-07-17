package com.gepardec.mega.domain;

import com.gepardec.mega.application.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectTimeEntry {

    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private Task task;
    private WorkingLocation workingLocation;

    public ProjectTimeEntry(LocalDateTime fromTime, LocalDateTime toTime, Task task, WorkingLocation workingLocation) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.task = task;
        this.workingLocation = workingLocation;
    }

    public double getDurationInHours() {
        return DateUtils.calcDiffInHours(fromTime, toTime);
    }

    public LocalDate getDate() {
        return fromTime.toLocalDate();
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    public Task getTask() {
        return task;
    }

    public WorkingLocation getWorkingLocation() {
        return workingLocation;
    }
}
