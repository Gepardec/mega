package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractTimeWarningCalculationStrategy {

    protected Map<LocalDate, List<ProjectTimeEntry>> groupProjectTimeEntriesByFromDate(final List<ProjectTimeEntry> projectTimeList) {
        return projectTimeList.stream()
                .sorted(Comparator.comparing(ProjectTimeEntry::getFromTime))
                .collect(Collectors.groupingBy(ProjectTimeEntry::getDate, LinkedHashMap::new, Collectors.toUnmodifiableList()));
    }

    protected double calculateWorkingDuration(List<ProjectTimeEntry> entriesPerDay) {
        return entriesPerDay.stream()
                .filter(entry -> Task.isTask(entry.getTask()))
                .map(ProjectTimeEntry::getDurationInHours)
                .collect(Collectors.summarizingDouble(Double::doubleValue))
                .getSum();
    }
}
