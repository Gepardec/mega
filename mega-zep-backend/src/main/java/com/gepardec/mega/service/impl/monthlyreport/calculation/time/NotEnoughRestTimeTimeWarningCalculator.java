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

public class NotEnoughRestTimeTimeWarningCalculator extends AbstractTimeWarningCalculationStrategy implements TimeWarningCalculationStrategy {

    @Override
    public List<TimeWarning> calculate(List<ProjectTimeEntry> projectTimeEntries) {
        final List<TimeWarning> warnings = new ArrayList<>();
        List<ProjectTimeEntry> filteredEntries = filterForTaskAndSortByFromDate(projectTimeEntries);

        for (int i = 0; i < filteredEntries.size(); i++) {
            ProjectTimeEntry actualEntry = filteredEntries.get(i);
            if (i + 1 < filteredEntries.size()) {
                ProjectTimeEntry nextEntry = filteredEntries.get(i + 1);
                if (nextEntry.getDate().isEqual(actualEntry.getDate().plusDays(1L))) {
                    double restHours = DateUtils.calcDiffInHours(actualEntry.getToTime(), nextEntry.getFromTime());
                    if (restHours < MIN_REQUIRED_REST_TIME) {
                        TimeWarning timeWarning = new TimeWarning();
                        timeWarning.setDate(nextEntry.getDate());
                        timeWarning.setMissingRestTime(MIN_REQUIRED_REST_TIME - restHours);
                        warnings.add(timeWarning);
                    }
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
}
