package com.gepardec.mega.domain.calculation;

import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractTimeWarningCalculationStrategy {

    protected Map<LocalDate, List<ProjectEntry>> groupProjectEntriesByFromDate(final List<ProjectEntry> projectTimeList, List<Predicate<ProjectEntry>> filters) {
        Stream<ProjectEntry> projectEntryStream = projectTimeList.stream();
        if (filters != null && filters.size() > 0) {
            for (final Predicate<ProjectEntry> predicate : filters) {
                projectEntryStream = projectEntryStream.filter(predicate);
            }
        }
        return projectEntryStream.sorted(Comparator.comparing(ProjectEntry::getFromTime).thenComparing(ProjectEntry::getToTime))
                .collect(Collectors.groupingBy(ProjectEntry::getDate, LinkedHashMap::new, Collectors.toUnmodifiableList()));
    }

    protected double calculateWorkingDuration(List<ProjectEntry> entriesPerDay) {
        return entriesPerDay.stream()
                .map(ProjectEntry::getDurationInHours)
                .collect(Collectors.summarizingDouble(Double::doubleValue))
                .getSum();
    }
}
