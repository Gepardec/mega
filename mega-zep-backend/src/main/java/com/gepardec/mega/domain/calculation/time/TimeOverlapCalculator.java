package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeOverlapWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeOverlapWarningType;

import java.util.ArrayList;
import java.util.List;

public class TimeOverlapCalculator extends AbstractTimeWarningCalculationStrategy implements WarningCalculationStrategy<TimeOverlapWarning> {

    @Override
    public List<TimeOverlapWarning> calculate(List<ProjectEntry> projectEntries) {
        List<TimeOverlapWarning> warningList = new ArrayList<>();

        for (int i = 0; i < projectEntries.size(); i++) {
            for (int j = i + 1; j < projectEntries.size(); j ++) {
                if (isOverlapping(projectEntries.get(i), projectEntries.get(j))) {
                    List<String> tempWarning = new ArrayList<>();
                    TimeOverlapWarning tempTimeOverlapWarning = new TimeOverlapWarning();
                    tempTimeOverlapWarning.setDate(projectEntries.get(i).getDate());
                    tempTimeOverlapWarning.getWarningTypes().add(TimeOverlapWarningType.TIME_OVERLAP);
                    warningList.add(tempTimeOverlapWarning);
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
