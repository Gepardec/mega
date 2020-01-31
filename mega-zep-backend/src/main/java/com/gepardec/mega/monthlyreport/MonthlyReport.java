package com.gepardec.mega.monthlyreport;

import com.gepardec.mega.monthlyreport.journey.JourneyWarning;
import com.gepardec.mega.monthlyreport.warning.TimeWarning;
import com.gepardec.mega.monthlyreport.warning.WarningCalculator;
import com.gepardec.mega.monthlyreport.warning.WarningConfig;
import com.gepardec.mega.rest.model.Employee;
import de.provantis.zep.MitarbeiterType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class MonthlyReport {

    private final List<TimeWarning> timeWarnings;
    private final List<JourneyWarning> journeyWarnings;
    private final MitarbeiterType mitarbeiter;

    public MonthlyReport(List<TimeWarning> timeWarnings, List<JourneyWarning> journeyWarnings, MitarbeiterType mitarbeiter) {
        this.timeWarnings = timeWarnings;
        this.journeyWarnings = journeyWarnings;
        this.mitarbeiter = mitarbeiter;
    }

    public List<TimeWarning> getTimeWarnings() {
        return timeWarnings;
    }

    public List<JourneyWarning> getJourneyWarnings() {
        return journeyWarnings;
    }

    public MitarbeiterType getMitarbeiter() {
        return mitarbeiter;
    }
}