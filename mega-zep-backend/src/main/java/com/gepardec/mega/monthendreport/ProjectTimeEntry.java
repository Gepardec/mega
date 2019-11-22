package com.gepardec.mega.monthendreport;

import com.gepardec.mega.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProjectTimeEntry {

    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private Task task;


    public double getDurationInHours() {
        return DateUtils.calcDiffInHours(fromTime, toTime);
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
