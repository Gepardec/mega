package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
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
            final ProjectEntry startEntry = projectEntries.get(0);
            final ProjectEntry endEntry = projectEntries.get(projectEntries.size() - 1);
            if (isAnyEntryOutOfRange(startEntry, endEntry)) {
                warnings.add(createTimeWarning(startEntry));
            }
        }

        return warnings;
    }

    private boolean isAnyEntryOutOfRange(final ProjectEntry firstEntry, final ProjectEntry lastEntry) {
        final int startHour = firstEntry.getFromTime().get(ChronoField.HOUR_OF_DAY);
        final int endHour = lastEntry.getToTime().get(ChronoField.HOUR_OF_DAY);

        return EARLIEST_HOUR > startHour || LATEST_HOUR < endHour;
    }

    private TimeWarning createTimeWarning(final ProjectEntry projectEntry) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(projectEntry.getDate());
        timeWarning.getWarningTypes().add(TimeWarningType.OUTSIDE_CORE_WORKING_TIME);

        return timeWarning;
    }
}
