package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.utils.DateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gepardec.mega.domain.model.monthlyreport.TimeWarning.MAX_HOURS_OF_DAY_WITHOUT_BREAK;
import static com.gepardec.mega.domain.model.monthlyreport.TimeWarning.MIN_REQUIRED_BREAK_TIME;

public class InsufficientBreakTimeForWorkingDayWithMoreThan6Hours extends AbstractTimeWarningCalculationStrategy implements TimeWarningCalculationStrategy {

    @Override
    public List<TimeWarning> calculate(List<ProjectTimeEntry> projectTimes) {
        final List<TimeWarning> warnings = new ArrayList<>();

        final Map<LocalDate, List<ProjectTimeEntry>> groupedProjectTimeEntries = groupProjectTimeEntriesByFromDate(projectTimes);
        for (final Map.Entry<LocalDate, List<ProjectTimeEntry>> projectTimeEntry : groupedProjectTimeEntries.entrySet()) {
            final double workHoursOfDay = calculateWorkingDuration(projectTimeEntry.getValue());
            final List<ProjectTimeEntry> entriesPerDay = projectTimeEntry.getValue();
            if (hasExceededMaximumWorkingHoursWithoutBreak(workHoursOfDay)) {
                final double breakTime = calculateBreakTimeSummaryForAllDayEntries(entriesPerDay);
                if (breakTime < MIN_REQUIRED_BREAK_TIME) {
                    warnings.add(createTimeWarning(projectTimeEntry.getKey(), breakTime));
                }
            }
        }
        return warnings;
    }

    private double calculateBreakTimeSummaryForAllDayEntries(final List<ProjectTimeEntry> entriesPerDay) {
        double breakTime = 0d;
        for (int i = 0; i < entriesPerDay.size(); i++) {
            final ProjectTimeEntry actualEntry = entriesPerDay.get(i);
            final ProjectTimeEntry nextEntry = getNextEntryOrNull(i, entriesPerDay);
            if (nextEntry != null) {
                breakTime += DateUtils.calcDiffInHours(actualEntry.getToTime(), nextEntry.getFromTime());
            }
        }

        return breakTime;
    }

    private ProjectTimeEntry getNextEntryOrNull(final int idx, final List<ProjectTimeEntry> projectTimeEntries) {
        if (idx + 1 < projectTimeEntries.size()) {
            return projectTimeEntries.get(idx + 1);
        }
        return null;
    }

    private boolean hasExceededMaximumWorkingHoursWithoutBreak(final double workHoursOfDay) {
        return workHoursOfDay > MAX_HOURS_OF_DAY_WITHOUT_BREAK;
    }

    private TimeWarning createTimeWarning(final LocalDate date, final double breakTime) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(date);
        timeWarning.setMissingBreakTime(MIN_REQUIRED_BREAK_TIME - breakTime);
        return timeWarning;
    }
}
