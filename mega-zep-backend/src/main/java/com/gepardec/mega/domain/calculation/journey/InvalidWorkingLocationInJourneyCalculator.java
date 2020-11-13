package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvalidWorkingLocationInJourneyCalculator implements WarningCalculationStrategy<JourneyWarning> {

    @Override
    public List<JourneyWarning> calculate(List<ProjectEntry> projectTimeEntries) {
        final Map<LocalDate, List<ProjectEntry>> groupedProjectTimeEntries = projectTimeEntries.stream()
                .sorted(Comparator.comparing(ProjectEntry::getFromTime))
                .collect(Collectors.groupingBy(ProjectEntry::getDate));

        final List<JourneyWarning> warnings = new ArrayList<>();

        for (final Map.Entry<LocalDate, List<ProjectEntry>> entriesPerDay : groupedProjectTimeEntries.entrySet()) {
            if (hasJourneyEntries(entriesPerDay.getValue())) {
                final List<ProjectEntry> projectEntriesOfDay = entriesPerDay.getValue();
                WorkingLocation workingLocation = null;
                for (final ProjectEntry projectEntry : projectEntriesOfDay) {
                    if (Task.isJourney(projectEntry.getTask())) {
                        workingLocation = projectEntry.getWorkingLocation();
                    } else if (workingLocation != null && !workingLocation.equals(projectEntry.getWorkingLocation())) {
                        warnings.add(createJourneyWarningWithEnumType(projectEntry, Warning.JOURNEY_INVALID_WORKING_LOCATION));
                        break;
                    }
                }
            }
        }

        return warnings;
    }

    private boolean hasJourneyEntries(final List<ProjectEntry> projectEntries) {
        return projectEntries.stream().anyMatch(entry -> Task.isTask(entry.getTask()));
    }

    private JourneyWarning createJourneyWarningWithEnumType(ProjectEntry projectEntry, Warning warning) {
        JourneyWarning newJourneyWarning = new JourneyWarning();
        newJourneyWarning.setDate(projectEntry.getDate());
        newJourneyWarning.getWarningTypes().add(warning);
        return newJourneyWarning;
    }
}
