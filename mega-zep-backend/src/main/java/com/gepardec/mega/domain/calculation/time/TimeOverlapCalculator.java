package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;

import java.util.ArrayList;
import java.util.List;

public class TimeOverlapCalculator implements WarningCalculationStrategy<TimeWarning> {

    @Override
    public List<TimeWarning> calculate(List<ProjectEntry> projectEntries) {
        List<TimeWarning> warningList = new ArrayList<>();

        for (int i = 0; i < projectEntries.size(); i++) {
            for (int j = i + 1; j < projectEntries.size(); j ++) {
                if (isOverlapping(projectEntries.get(i), projectEntries.get(j))) {
                    List<String> tempWarning = new ArrayList<>();
                    tempWarning.add("Überschneidungen bitte Kontrollieren, bezüglich Ausnahmen bei Reisen bitte Rücksprache mit HR");
                    TimeWarning tempTimeWarning = new TimeWarning();
                    tempTimeWarning.setDate(projectEntries.get(i).getDate());
                    tempTimeWarning.setWarnings(tempWarning);
                    warningList.add(tempTimeWarning);
                }
            }
        }

        return warningList;
    }

    private boolean isOverlapping(ProjectEntry elementOne, ProjectEntry elementTwo) {
        if (elementOne.getToTime().equals(elementTwo.getFromTime()) || elementOne.getFromTime().equals(elementTwo.getToTime())) {
            return false;
        } else {
            return !(elementOne.getToTime().isBefore(elementTwo.getFromTime()) || elementOne.getFromTime().isAfter(elementTwo.getToTime()));
        }
    }
}
