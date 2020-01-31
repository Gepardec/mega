package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public class TimeWarning {

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private Double missingRestTime;
    private Double missingBreakTime;
    private Double excessWorkTime;

    public TimeWarning() {
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
