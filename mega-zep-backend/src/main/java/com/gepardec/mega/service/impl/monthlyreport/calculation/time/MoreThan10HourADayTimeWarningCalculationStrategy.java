package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gepardec.mega.domain.model.monthlyreport.TimeWarning.MAX_HOURS_A_DAY;

public class MoreThan10HourADayTimeWarningCalculationStrategy extends AbstractTimeWarningCalculationStrategy implements TimeWarningCalculationStrategy {

    @Override
    public List<TimeWarning> calculate(List<ProjectTimeEntry> projectTimeEntries) {
        Map<LocalDate, List<ProjectTimeEntry>> groupedProjectTimeEntries = groupProjectTimeEntriesByFromDate(projectTimeEntries);
        final List<TimeWarning> warnings = new ArrayList<>(0);

        for (final Map.Entry<LocalDate, List<ProjectTimeEntry>> projectTimeEntry : groupedProjectTimeEntries.entrySet()) {
            double workDurationOfDay = calcWorkingDurationOfDay(projectTimeEntries);
            if (workDurationOfDay > MAX_HOURS_A_DAY) {
                TimeWarning timeWarning = new TimeWarning();
                timeWarning.setDate(projectTimeEntry.getKey());
                timeWarning.setExcessWorkTime(workDurationOfDay - MAX_HOURS_A_DAY);
                warnings.add(timeWarning);
            }
        }
        return warnings;
    }
}
