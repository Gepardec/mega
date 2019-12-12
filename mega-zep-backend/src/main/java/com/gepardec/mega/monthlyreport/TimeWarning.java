package com.gepardec.mega.monthlyreport;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimeWarning {
    static final double MAX_HOURS_A_DAY = 10d;
    static final double MAX_HOURS_OF_DAY_WITHOUT_BREAK = 6d;
    static final double MIN_REQUIRED_BREAK_TIME = 0.5d;
    static final double MIN_REQUIRED_REST_TIME = 11d;
    static final LocalTime EARLIEST_START_TIME = LocalTime.of(06, 0);
    static final LocalTime LATEST_END_TIME = LocalTime.of(22, 0);


    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private Double missingRestTime;
    private Double missingBreakTime;
    private Double excessWorkTime;


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
}
