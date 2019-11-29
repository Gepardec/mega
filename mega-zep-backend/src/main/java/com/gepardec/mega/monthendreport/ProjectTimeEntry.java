package com.gepardec.mega.monthendreport;

import com.gepardec.mega.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectTimeEntry {

    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private Task task;
    private WorkingLocation workingLocation;

    public double getDurationInHours() {
        return DateUtils.calcDiffInHours(fromTime, toTime);
    }

    public LocalDate getDate() {
        return fromTime.toLocalDate();
    }
}
