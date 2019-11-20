package com.gepardec.mega.monthendreport;

import de.provantis.zep.MitarbeiterType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class MonthendReport {
    @Getter
    private List<TimeWarning> timeWarnings = new ArrayList<>(0);
    @Getter
    private List<JourneyWarning> journeyWarnings = new ArrayList<>(0);
    @Getter
    private MitarbeiterType employee;

    private WarningCalculator warningCalculator;
    private ProjectTimeManager projectTimeManager;

    public MonthendReport(MitarbeiterType employee, ProjectTimeManager projectTimeManager) {
        this.projectTimeManager = projectTimeManager;
        this.employee = employee;
        warningCalculator = new WarningCalculator();
    }

    public void calculateWarnings() {
        timeWarnings = warningCalculator.determineTimeWarnings(projectTimeManager);
        journeyWarnings = warningCalculator.createJourneyWarnings(projectTimeManager);
    }
}


