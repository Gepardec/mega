package com.gepardec.mega.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MonthendReport {
    private List<BreakWarning> breakWarnings = new ArrayList<>(0);
    private List<JourneyWarning> journeyWarnings = new ArrayList<>(0);


}
