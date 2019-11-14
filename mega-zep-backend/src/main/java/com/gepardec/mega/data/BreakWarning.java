package com.gepardec.mega.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BreakWarning {
    private LocalDate date;
    private String day;
    private Double tooLessRest;
    private Double tooLessBreak;
    private Double tooMuchWorkTime;
}
