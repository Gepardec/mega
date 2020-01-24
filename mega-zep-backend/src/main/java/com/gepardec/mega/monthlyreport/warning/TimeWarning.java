package com.gepardec.mega.monthlyreport.warning;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeWarning {
    static final double MAX_HOURS_A_DAY = 10d;
    static final double MAX_HOURS_OF_DAY_WITHOUT_BREAK = 6d;
    static final double MIN_REQUIRED_BREAK_TIME = 0.5d;
    static final double MIN_REQUIRED_REST_TIME = 11d;
    static final LocalTime EARLIEST_START_TIME = LocalTime.of(6, 0);
    static final LocalTime LATEST_END_TIME = LocalTime.of(22, 0);

    private LocalDate date;
    private Double missingRestTime;
    private Double missingBreakTime;
    private Double excessWorkTime;

    TimeWarning() {
    }

    TimeWarning(LocalDate date, Double missingRestTime, Double missingBreakTime, Double excessWorkTime) {
        this.date = date;
        this.missingRestTime = missingRestTime;
        this.missingBreakTime = missingBreakTime;
        this.excessWorkTime = excessWorkTime;
    }

    public static TimeWarning of(LocalDate date, Double missingRestTime, Double missingBreakTime, Double excessWorkTime) {
        return new TimeWarning(date, missingRestTime, missingBreakTime, excessWorkTime);
    }

    public void mergeBreakWarnings(TimeWarning newTimeWarning) {
        if (newTimeWarning.missingBreakTime != null) {
            missingBreakTime = newTimeWarning.missingBreakTime;
        }
        if (newTimeWarning.missingRestTime != null) {
            missingRestTime = newTimeWarning.missingRestTime;
        }
        if (newTimeWarning.excessWorkTime != null) {
            excessWorkTime = newTimeWarning.excessWorkTime;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getMissingRestTime() {
        return missingRestTime;
    }

    public void setMissingRestTime(Double missingRestTime) {
        this.missingRestTime = missingRestTime;
    }

    public Double getMissingBreakTime() {
        return missingBreakTime;
    }

    public void setMissingBreakTime(Double missingBreakTime) {
        this.missingBreakTime = missingBreakTime;
    }

    public Double getExcessWorkTime() {
        return excessWorkTime;
    }

    public void setExcessWorkTime(Double excessWorkTime) {
        this.excessWorkTime = excessWorkTime;
    }
}
