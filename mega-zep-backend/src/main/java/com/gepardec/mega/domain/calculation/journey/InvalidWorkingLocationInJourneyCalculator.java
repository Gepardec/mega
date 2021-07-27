package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarningType;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvalidWorkingLocationInJourneyCalculator implements WarningCalculationStrategy<JourneyWarning> {

    private static final Map<WorkingLocation, List<WorkingLocation>> MAPPED_SUPPORTED_WORKING_LOCATIONS = Map.of(
            WorkingLocation.A,
            List.of(WorkingLocation.A, WorkingLocation.MAIN));

    @Override
    public List<JourneyWarning> calculate(List<ProjectEntry> projectTimeEntries) {
        final List<ProjectEntry> groupedProjectTimeEntries = projectTimeEntries.stream()
                .sorted(Comparator.comparing(ProjectEntry::getFromTime))
                .collect(Collectors.toList());

        final List<JourneyWarning> warnings = new ArrayList<>();

        if (hasJourneyEntries(groupedProjectTimeEntries)) {
            WorkingLocation workingLocation = null;
            JourneyDirection journeyDirection = null;
            for (final ProjectEntry projectEntry : groupedProjectTimeEntries) {
                if (Task.isJourney(projectEntry.getTask())) {
                    workingLocation = projectEntry.getWorkingLocation();
                    journeyDirection = ((JourneyTimeEntry) projectEntry).getJourneyDirection();
                } else {
                    if (journeyDirection != null && journeyDirection.equals(JourneyDirection.BACK)) {
                        workingLocation = WorkingLocation.MAIN;
                    }
                    if (!isProjectEntryValid(projectEntry, workingLocation, journeyDirection)) {
                        warnings.add(createJourneyWarningWithEnumType(projectEntry, JourneyWarningType.INVALID_WORKING_LOCATION));
                    }
                }
            }
        }
        return warnings;
    }

    private boolean isProjectEntryValid(final ProjectEntry projectEntry, final WorkingLocation workingLocation, final JourneyDirection journeyDirection) {
        if (JourneyDirection.BACK.equals(journeyDirection)) {
            return isProjectEntryValidAfterJourneyBack(projectEntry, workingLocation);
        }

        return isProjectEntryValidAfterJourneyToOrFurther(projectEntry, workingLocation);
    }

    private boolean isProjectEntryValidAfterJourneyToOrFurther(final ProjectEntry projectEntry, final WorkingLocation journeyBackWorkingLocation) {
        return journeyBackWorkingLocation == null || projectEntry.getWorkingLocation().equals(journeyBackWorkingLocation);
    }

    private boolean isProjectEntryValidAfterJourneyBack(final ProjectEntry projectEntry, final WorkingLocation journeyBackWorkingLocation) {
        final WorkingLocation workingLocation = projectEntry.getWorkingLocation();
        return (MAPPED_SUPPORTED_WORKING_LOCATIONS.containsKey(journeyBackWorkingLocation))
                ? MAPPED_SUPPORTED_WORKING_LOCATIONS.get(journeyBackWorkingLocation).contains(workingLocation)
                : journeyBackWorkingLocation == null || projectEntry.getWorkingLocation().equals(journeyBackWorkingLocation);
    }

    private boolean hasJourneyEntries(final List<ProjectEntry> projectEntries) {
        return projectEntries.stream().anyMatch(entry -> Task.isTask(entry.getTask()));
    }

    private JourneyWarning createJourneyWarningWithEnumType(ProjectEntry projectEntry, JourneyWarningType warning) {
        JourneyWarning newJourneyWarning = new JourneyWarning();
        newJourneyWarning.setDate(projectEntry.getDate());
        newJourneyWarning.getWarningTypes().add(warning);
        return newJourneyWarning;
    }
}
