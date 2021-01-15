package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.*;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This calculator calculates range between the earliest and latest project entry of a day.
 * Employees can not book times outside a regular working hour time range.
 * {@link com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry} are ignored for now, only {@link ProjectTimeEntry} are considered.
 */
public class CoreWorkingHoursCalculator extends AbstractTimeWarningCalculationStrategy implements WarningCalculationStrategy<TimeWarning> {

    static final int EARLIEST_HOUR = 6;

    static final int LATEST_HOUR = 22;

    @Override
    public List<TimeWarning> calculate(List<ProjectEntry> projectTimeEntries) {
        final List<TimeWarning> warnings = new ArrayList<>();
        Map<LocalDate, List<ProjectEntry>> groupedEntries = groupProjectEntriesByFromDate(projectTimeEntries);

        for (final Map.Entry<LocalDate, List<ProjectEntry>> entry : groupedEntries.entrySet()) {
            final List<ProjectEntry> projectEntries = entry.getValue();
            final List<ProjectEntry> reversedProjectEntries = new ArrayList<>(projectEntries);
            Collections.reverse(reversedProjectEntries);
            final ProjectEntry startEntry = findFirstRelevantEntry(projectEntries);
            final ProjectEntry endEntry = findFirstRelevantEntry(reversedProjectEntries);
            if (isEntryOutOfRange(startEntry, endEntry)) {
                warnings.add(createTimeWarning(startEntry));
            }
        }

        return warnings;
    }

    private ProjectEntry findFirstRelevantEntry(List<ProjectEntry> timeEntries) {
        for (final ProjectEntry timeEntry : timeEntries) {
            if (isRelevantProjectTimeEntry(timeEntry)
                    || isRelevantJourneyTimeEntry(timeEntry)) {
                return timeEntry;
            }
        }

        return null;
    }

    private boolean isRelevantProjectTimeEntry(ProjectEntry projectEntry) {
        return projectEntry instanceof ProjectTimeEntry;
    }

    private boolean isRelevantJourneyTimeEntry(ProjectEntry projectEntry) {
        return projectEntry instanceof JourneyTimeEntry && ((JourneyTimeEntry) projectEntry).getVehicle().activeTraveler;
    }

    private boolean isEntryOutOfRange(final ProjectEntry firstEntry, final ProjectEntry lastEntry) {
        return hasStartedToEarly(firstEntry) || hasFinishedToLate(lastEntry);
    }

    private boolean hasStartedToEarly(ProjectEntry projectTimeEntry) {
        return projectTimeEntry != null && EARLIEST_HOUR > projectTimeEntry.getFromTime().get(ChronoField.HOUR_OF_DAY);
    }

    private boolean hasFinishedToLate(ProjectEntry projectTimeEntry) {
        return projectTimeEntry != null && LATEST_HOUR < projectTimeEntry.getToTime().get(ChronoField.HOUR_OF_DAY);
    }

    private TimeWarning createTimeWarning(final ProjectEntry projectEntry) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(projectEntry.getDate());
        timeWarning.getWarningTypes().add(TimeWarningType.OUTSIDE_CORE_WORKING_TIME);

        return timeWarning;
    }
}
