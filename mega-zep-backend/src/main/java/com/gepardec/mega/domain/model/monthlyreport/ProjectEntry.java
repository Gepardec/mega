package com.gepardec.mega.domain.model.monthlyreport;

import com.gepardec.mega.domain.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ProjectEntry {

    LocalDateTime getFromTime();

    LocalDateTime getToTime();

    Task getTask();

    default double getDurationInHours() {
        return DateUtils.calcDiffInHours(getFromTime(), getToTime());
    }

    default LocalDate getDate() {
        return getFromTime().toLocalDate();
    }
}
