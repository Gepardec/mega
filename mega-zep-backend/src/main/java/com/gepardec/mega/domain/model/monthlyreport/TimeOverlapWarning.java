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

public class TimeOverlapWarning implements ProjectEntryWarning {
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    private List<String> warnings = new ArrayList<>(0);

    @JsonIgnore
    private List<TimeOverlapWarningType> warningTypes = new ArrayList<>(0);

    public TimeOverlapWarning() {}

    TimeOverlapWarning(LocalDate date) {
        this.date = date;
    }

    public static TimeOverlapWarning of(LocalDate date) {
        return new TimeOverlapWarning(date);
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

    public List<TimeOverlapWarningType> getWarningTypes() {
        return warningTypes;
    }

    public void setWarningTypes(List<TimeOverlapWarningType> warningTypes) {
        this.warningTypes = warningTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeOverlapWarning warning = (TimeOverlapWarning) o;
        return Objects.equals(date, warning.date) &&
                Objects.equals(warnings, warning.warnings) &&
                Objects.equals(warningTypes, warning.warningTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, warnings, warningTypes);
    }
}
