package com.gepardec.mega.monthlyreport;

import com.gepardec.mega.monthlyreport.journey.JourneyWarning;
import com.gepardec.mega.monthlyreport.warning.TimeWarning;
import com.gepardec.mega.monthlyreport.warning.WarningCalculator;
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