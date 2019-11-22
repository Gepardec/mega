package com.gepardec.mega.monthendreport;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class JourneyWarning {
    private LocalDate date;
    private String day;
    private String warningText;
}
