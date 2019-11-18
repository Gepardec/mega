package com.gepardec.mega.monthendreport;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProjectTimeEntry {

    private LocalDateTime fromTime;
    private LocalDateTime toTime;

    @Setter(AccessLevel.NONE)
    private Duration duration;
    private Task task;


    public Duration getDuration() {
        return Duration.between(fromTime, toTime);
    }

    public static ProjectTimeEntry of(LocalDateTime fromTime, LocalDateTime toTime, Task task) {
        ProjectTimeEntry entry = new ProjectTimeEntry();
        entry.setFromTime(fromTime);
        entry.setToTime(toTime);
        entry.setTask(task);
        return  entry;
    }

    public LocalDate getDate() {
        return fromTime.toLocalDate();
    }

}
