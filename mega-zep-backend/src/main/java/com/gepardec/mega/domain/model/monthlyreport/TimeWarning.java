package com.gepardec.mega.domain.model.monthlyreport;

import java.time.LocalDate;
import java.util.Objects;

public class TimeWarning implements ProjectEntryWarning {

    private LocalDate date;

    private Double missingRestTime;

    private Double missingBreakTime;

    private Double excessWorkTime;

    public TimeWarning() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeWarning that = (TimeWarning) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(missingRestTime, that.missingRestTime) &&
                Objects.equals(missingBreakTime, that.missingBreakTime) &&
                Objects.equals(excessWorkTime, that.excessWorkTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, missingRestTime, missingBreakTime, excessWorkTime);
    }
}
