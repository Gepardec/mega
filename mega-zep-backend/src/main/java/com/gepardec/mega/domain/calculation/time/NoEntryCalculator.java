package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import de.provantis.zep.FehlzeitType;

import java.time.LocalDateTime;
import java.util.List;

public class NoEntryCalculator extends AbstractTimeWarningCalculationStrategy {
    //TODO
    public List<TimeWarning> calculate(List<ProjectEntry> projectTimeEntries, List<FehlzeitType> fehlZeitTypeList) {
        return null;
    }

    public List<LocalDateTime> getBusinessDaysOfMonth(int month) {
        return null;
    }



}
