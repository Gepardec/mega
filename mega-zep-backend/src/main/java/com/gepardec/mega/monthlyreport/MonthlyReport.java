package com.gepardec.mega.monthlyreport;

import de.provantis.zep.MitarbeiterType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class MonthlyReport {
    @Getter
    private List<TimeWarning> timeWarnings = new ArrayList<>(0);
    @Getter
    private List<JourneyWarning> journeyWarnings = new ArrayList<>(0);
    @Getter
    private MitarbeiterType employee;

    private WarningCalculator warningCalculator;

    private ProjectTimeManager projectTimeManager;

    public MonthlyReport(MitarbeiterType employee, ProjectTimeManager projectTimeManager) {
        this.projectTimeManager = projectTimeManager;
        this.employee = employee;
        warningCalculator = new WarningCalculator();
    }

    public void calculateWarnings() {
        timeWarnings = warningCalculator.determineTimeWarnings(projectTimeManager);
        journeyWarnings = warningCalculator.determineJourneyWarnings(projectTimeManager);
    }
}