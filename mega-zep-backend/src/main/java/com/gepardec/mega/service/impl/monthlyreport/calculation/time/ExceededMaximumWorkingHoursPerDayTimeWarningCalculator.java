package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gepardec.mega.domain.model.monthlyreport.TimeWarning.MAX_HOURS_A_DAY;

public class ExceededMaximumWorkingHoursPerDayTimeWarningCalculator extends AbstractTimeWarningCalculationStrategy implements TimeWarningCalculationStrategy {

    @Override
    public List<TimeWarning> calculate(List<ProjectTimeEntry> projectTimeEntries) {
        final List<TimeWarning> warnings = new ArrayList<>(0);

        final Map<LocalDate, List<ProjectTimeEntry>> groupedProjectTimeEntries = groupProjectTimeEntriesByFromDate(projectTimeEntries);
        for (final Map.Entry<LocalDate, List<ProjectTimeEntry>> projectTimeEntry : groupedProjectTimeEntries.entrySet()) {
            final List<ProjectTimeEntry> projectEntriesPerDay = projectTimeEntry.getValue();
            final double workDurationOfDay = calculateWorkingDuration(projectEntriesPerDay);
            if (hasExceededMaximumWorkingHoursPerDay(workDurationOfDay)) {
                warnings.add(createTimeWarning(projectTimeEntry.getKey(), workDurationOfDay));
            }
        }
        return warnings;
    }

    private boolean hasExceededMaximumWorkingHoursPerDay(final double workDurationOfDay) {
        return workDurationOfDay > MAX_HOURS_A_DAY;
    }

    private TimeWarning createTimeWarning(final LocalDate date, final double workDurationOfDay) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(date);
        timeWarning.setExcessWorkTime(workDurationOfDay - MAX_HOURS_A_DAY);

        return timeWarning;
    }
}
