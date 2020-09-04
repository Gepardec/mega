package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.utils.DateUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.gepardec.mega.domain.model.monthlyreport.TimeWarning.MIN_REQUIRED_REST_TIME;

public class InsufficientRestTimeTimeWarningCalculator extends AbstractTimeWarningCalculationStrategy implements TimeWarningCalculationStrategy {

    @Override
    public List<TimeWarning> calculate(List<ProjectTimeEntry> projectTimeEntries) {
        final List<TimeWarning> warnings = new ArrayList<>();
        List<ProjectTimeEntry> filteredProjectTimeEntries = filterForTaskAndSortByFromDate(projectTimeEntries);

        for (int i = 0; i < filteredProjectTimeEntries.size(); i++) {
            final ProjectTimeEntry currentEntry = filteredProjectTimeEntries.get(i);
            final ProjectTimeEntry nextEntry = getNextEntryOrNull(i, filteredProjectTimeEntries);
            if (isNextProjectTimeEntryPresentAndOfNextDay(currentEntry, nextEntry)) {
                final double restHours = DateUtils.calcDiffInHours(currentEntry.getToTime(), nextEntry.getFromTime());
                if (hasInsufficientRestTime(restHours)) {
                    warnings.add(createTimeWarning(nextEntry, restHours));
                }
            }
        }

        return warnings;
    }

    private List<ProjectTimeEntry> filterForTaskAndSortByFromDate(final List<ProjectTimeEntry> projectTimeEntries) {
        return projectTimeEntries.stream()
                .filter(entry -> Task.isTask(entry.getTask()))
                .sorted(Comparator.comparing(ProjectTimeEntry::getFromTime))
                .collect(Collectors.toList());
    }

    private ProjectTimeEntry getNextEntryOrNull(final int idx, final List<ProjectTimeEntry> projectTimeEntries) {
        if (idx + 1 < projectTimeEntries.size()) {
            return projectTimeEntries.get(idx + 1);
        }
        return null;
    }

    private boolean isNextProjectTimeEntryPresentAndOfNextDay(final ProjectTimeEntry currentEntry, final ProjectTimeEntry nextEntry) {
        return (nextEntry != null) && nextEntry.getDate().isEqual(currentEntry.getDate().plusDays(1L));
    }

    private boolean hasInsufficientRestTime(final double restHours) {
        return restHours < MIN_REQUIRED_REST_TIME;
    }

    private TimeWarning createTimeWarning(final ProjectTimeEntry nextEntry, final double restHours) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(nextEntry.getDate());
        timeWarning.setMissingRestTime(MIN_REQUIRED_REST_TIME - restHours);

        return timeWarning;
    }

}
