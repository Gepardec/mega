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

public class ToLittleRestTimeForWorkingDayWithMoreThan6Hours extends AbstractTimeWarningCalculationStrategy implements TimeWarningCalculationStrategy {

    @Override
    public List<TimeWarning> calculate(List<ProjectTimeEntry> projectTimeEntries) {
        Map<LocalDate, List<ProjectTimeEntry>> groupedProjectTimeEntries = groupProjectTimeEntriesByFromDate(projectTimeEntries);

        final List<TimeWarning> warnings = new ArrayList<>();
        for (final Map.Entry<LocalDate, List<ProjectTimeEntry>> projectTimeEntry : groupedProjectTimeEntries.entrySet()) {
            double workHoursOfDay = calcWorkingDurationOfDay(projectTimeEntries);
            if (workHoursOfDay > MAX_HOURS_OF_DAY_WITHOUT_BREAK) {
                double breakTime = 0d;
                for (int i = 0; i < projectTimeEntries.size(); i++) {
                    ProjectTimeEntry actualEntry = projectTimeEntries.get(i);
                    if (i + 1 < projectTimeEntries.size()) {
                        ProjectTimeEntry nextEntry = projectTimeEntries.get(i + 1);
                        breakTime += DateUtils.calcDiffInHours(actualEntry.getToTime(), nextEntry.getFromTime());

                    }
                }
                if (breakTime < MIN_REQUIRED_BREAK_TIME) {
                    TimeWarning timeWarning = new TimeWarning();
                    timeWarning.setDate(projectTimeEntry.getKey());
                    timeWarning.setMissingBreakTime(MIN_REQUIRED_BREAK_TIME - breakTime);
                    warnings.add(timeWarning);
                }
            }
        }
        return warnings;
    }
}
