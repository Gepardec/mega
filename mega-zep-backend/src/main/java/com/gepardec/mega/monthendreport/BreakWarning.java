package com.gepardec.mega.monthendreport;

import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class BreakWarning {
    static final double MAX_HOURS_A_DAY = 10d;
    static final double MAX_HOURS_OF_DAY_WITHOUT_BREAK = 6d;
    static final double MIN_REQUIRED_BREAK_TIME = 0.5d;
    static final double MIN_REQUIRED_REST_TIME = 11d;

    private LocalDate date;
    private String day;
    private Double tooLessRest;
    private Double tooLessBreak;
    private Double tooMuchWorkTime;
    private String warning;


    public void setWarning(WarningType warningType) {
        warning = warningType.getText();
    }


    public void mergeBreakWarnings(BreakWarning newBreakWarning) {
        if (Objects.nonNull(newBreakWarning.tooLessBreak)) {
            tooLessBreak = newBreakWarning.tooLessBreak;
        }
        if (newBreakWarning.tooLessRest != null) {
            tooLessRest = newBreakWarning.tooLessRest;
        }
        if (newBreakWarning.tooMuchWorkTime != null) {
            tooMuchWorkTime = newBreakWarning.tooMuchWorkTime;
        }
        warning = new StringBuilder(warning)
                .append(System.lineSeparator())
                .append(newBreakWarning.getWarning())
                .toString();
    }
}
