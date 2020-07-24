package com.gepardec.mega.domain.model.monthlyreport;

import com.gepardec.mega.domain.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectTimeEntry {

    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private Task task;

    public ProjectTimeEntry(LocalDateTime fromTime, LocalDateTime toTime, Task task) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.task = task;
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
}
