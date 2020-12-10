package com.gepardec.mega.domain.model.monthlyreport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimeWarning implements ProjectEntryWarning {

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    private Double missingRestTime;

    private Double missingBreakTime;

    private Double excessWorkTime;

    private List<String> warnings = new ArrayList<>(0);

    @JsonIgnore
    private List<TimeWarningType> warningTypes = new ArrayList<>(0);

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

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<TimeWarningType> getWarningTypes() {
        return warningTypes;
    }

    public void setWarningTypes(List<TimeWarningType> warningTypes) {
        this.warningTypes = warningTypes;
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

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TimeWarning warning = (TimeWarning) o;
        return Objects.equals(date, warning.date) &&
                Objects.equals(missingRestTime, warning.missingRestTime) &&
                Objects.equals(missingBreakTime, warning.missingBreakTime) &&
                Objects.equals(excessWorkTime, warning.excessWorkTime) &&
                Objects.equals(warnings, warning.warnings) &&
                Objects.equals(warningTypes, warning.warningTypes);
    }

    @Override public int hashCode() {
        return Objects.hash(date, missingRestTime, missingBreakTime, excessWorkTime, warnings, warningTypes);
    }
}
