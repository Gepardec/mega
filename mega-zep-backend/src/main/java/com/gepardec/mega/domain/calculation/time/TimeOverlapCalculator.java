package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimeOverlapCalculator extends AbstractTimeWarningCalculationStrategy implements WarningCalculationStrategy<TimeWarning> {

    @Override
    public List<TimeWarning> calculate(List<ProjectEntry> projectEntries) {
        Set<TimeWarning> warningList = new HashSet<>();

        projectEntries.forEach(entry -> {
            projectEntries.forEach(projectEntry -> {
                if (isOverlapping(entry, projectEntry) && entry != projectEntry) {
                    TimeWarning tempTimeOverlapWarning = new TimeWarning();
                    tempTimeOverlapWarning.setDate(entry.getDate());
                    tempTimeOverlapWarning.getWarningTypes().add(TimeWarningType.TIME_OVERLAP);
                    warningList.add(tempTimeOverlapWarning);
                }
            });
        });

        return new ArrayList<>(warningList);
    }

    private boolean isOverlapping(@NotNull ProjectEntry entryOne, @NotNull ProjectEntry entryTwo) {
        if (entryOne.getToTime().equals(entryTwo.getFromTime()) || entryOne.getFromTime().equals(entryTwo.getToTime())) {
            return false;
        } else {
            return !(entryOne.getToTime().isBefore(entryTwo.getFromTime()) || entryOne.getFromTime().isAfter(entryTwo.getToTime()));
        }
    }
}
