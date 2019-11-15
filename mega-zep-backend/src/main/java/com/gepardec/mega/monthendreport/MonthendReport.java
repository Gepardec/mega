package com.gepardec.mega.monthendreport;

import de.provantis.zep.MitarbeiterType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MonthendReport {
    private List<BreakWarning> breakWarnings = new ArrayList<>(0);
    private List<JourneyWarning> journeyWarnings = new ArrayList<>(0);
    private MitarbeiterType employee;

    public MonthendReport(MitarbeiterType employee) {
        this.employee = employee;
    }
}
