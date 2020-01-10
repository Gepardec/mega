package com.gepardec.mega.monthlyreport;

import com.gepardec.mega.monthlyreport.journey.JourneyWarning;
import com.gepardec.mega.monthlyreport.warning.TimeWarning;
import com.gepardec.mega.monthlyreport.warning.WarningCalculator;
import com.gepardec.mega.monthlyreport.warning.WarningConfig;
import com.gepardec.mega.rest.Employee;
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
    private Employee employee;

    private WarningCalculator warningCalculator;

    private ProjectTimeManager projectTimeManager;


    public MonthlyReport(Employee employee, ProjectTimeManager projectTimeManager, WarningConfig warningConfig) {
        this.projectTimeManager = projectTimeManager;
        this.employee = employee;
        warningCalculator = new WarningCalculator(warningConfig);
    }

    public void calculateWarnings() {
        timeWarnings = warningCalculator.determineTimeWarnings(projectTimeManager);
        journeyWarnings = warningCalculator.determineJourneyWarnings(projectTimeManager);
    }
}