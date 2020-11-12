package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This calculator calculates the 'rest time' between two working days.
 * At least n hours 'rest time' are necessary between two working days.
 * {@link com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry} are ignored for now, only {@link ProjectTimeEntry} are considered.
 */
public class InsufficientRestCalculator extends AbstractTimeWarningCalculationStrategy implements WarningCalculationStrategy<TimeWarning> {

    static final double MIN_REQUIRED_REST_TIME = 11d;

    @Override
    public List<TimeWarning> calculate(List<ProjectEntry> projectTimeEntries) {
        final List<TimeWarning> warnings = new ArrayList<>();
        List<ProjectEntry> filteredProjectTimeEntries = filterForTaskAndSortByFromDate(projectTimeEntries);

        for (int i = 0; i < filteredProjectTimeEntries.size(); i++) {
            final ProjectEntry currentEntry = filteredProjectTimeEntries.get(i);
            final ProjectEntry nextEntry = getNextEntryOrNull(i, filteredProjectTimeEntries);
            if (isNextProjectTimePresentAndOfNextDay(currentEntry, nextEntry)) {
                final double restHours = BigDecimal.valueOf(Duration.between(currentEntry.getToTime(), nextEntry.getFromTime()).toMinutes())
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .divide(BigDecimal.valueOf(60), RoundingMode.HALF_EVEN)
                        .doubleValue();
                if (hasInsufficientRestTime(restHours)) {
                    warnings.add(createTimeWarning(nextEntry, restHours));
                }
            }
        }

        return warnings;
    }

    private List<ProjectEntry> filterForTaskAndSortByFromDate(final List<ProjectEntry> projectTimeEntries) {
        return projectTimeEntries.stream()
                .filter(entry -> Task.isTask(entry.getTask()))
                .sorted(Comparator.comparing(ProjectEntry::getFromTime))
                .collect(Collectors.toList());
    }

    private ProjectEntry getNextEntryOrNull(final int idx, final List<ProjectEntry> projectTimeEntries) {
        if (idx + 1 < projectTimeEntries.size()) {
            return projectTimeEntries.get(idx + 1);
        }
        return null;
    }

    private boolean isNextProjectTimePresentAndOfNextDay(final ProjectEntry currentEntry, final ProjectEntry nextEntry) {
        return (nextEntry != null) && nextEntry.getDate().isEqual(currentEntry.getDate().plusDays(1L));
    }

    private boolean hasInsufficientRestTime(final double restHours) {
        return restHours < MIN_REQUIRED_REST_TIME;
    }

    private TimeWarning createTimeWarning(final ProjectEntry nextEntry, final double restHours) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(nextEntry.getDate());
        timeWarning.setMissingRestTime(
                BigDecimal.valueOf(MIN_REQUIRED_REST_TIME).subtract(BigDecimal.valueOf(restHours)).setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        return timeWarning;
    }

}
