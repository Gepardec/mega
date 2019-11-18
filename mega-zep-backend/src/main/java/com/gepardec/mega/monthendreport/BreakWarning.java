package com.gepardec.mega.monthendreport;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class BreakWarning {
    static final double MAX_HOURS_A_DAY = 10d;
    static final double MAX_HOURS_OF_DAY_WITHOUT_BREAK = 6d;
    static final double MIN_REQUIRED_BREAK = 0.5d;

    private LocalDate date;
    private String day;
    private Double tooLessRest;
    private Double tooLessBreak;
    private Double tooMuchWorkTime;
    @Getter(AccessLevel.NONE)
    private StringBuilder warningText = new StringBuilder();


    public void addWarning(BreakWarningType warningType) {
        if(warningText.length() > 0) {
            warningText.append(System.lineSeparator());
        }
        warningText.append(warningType.getText());
    }
}
