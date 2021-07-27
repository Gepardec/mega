package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarningType;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InvalidJourneyCalculator implements WarningCalculationStrategy<JourneyWarning> {

    @Override
    public List<JourneyWarning> calculate(List<ProjectEntry> projectTimeEntries) {
        final List<JourneyTimeEntry> groupedProjectTimeEntries = projectTimeEntries.stream()
                .filter(entry -> Task.isJourney(entry.getTask()))
                .map(JourneyTimeEntry.class::cast)
                .sorted(Comparator.comparing(ProjectEntry::getFromTime))
                .collect(Collectors.toList());

        final List<JourneyWarning> warnings = new ArrayList<>();
        final JourneyDirectionValidator journeyDirectionValidator = new JourneyDirectionValidator();

        for (int i = 0; i < groupedProjectTimeEntries.size(); i++) {
            final JourneyTimeEntry curEntry = groupedProjectTimeEntries.get(i);
            final JourneyDirection nextDirection = tryToGetNextJourneyDirection(groupedProjectTimeEntries, i);
            JourneyWarningType warning = journeyDirectionValidator.validate(curEntry.getJourneyDirection(), nextDirection);
            if (warning != null) {
                warnings.add(createJourneyWarningWithEnumType(curEntry, warning));
            }
        }
        return warnings;
    }

    private JourneyDirection tryToGetNextJourneyDirection(final List<JourneyTimeEntry> journeyTimeEntries, final int idx) {
        return (idx + 1 < journeyTimeEntries.size()) ? journeyTimeEntries.get(idx + 1).getJourneyDirection() : null;
    }

    private JourneyWarning createJourneyWarningWithEnumType(JourneyTimeEntry journeyEntry, JourneyWarningType warning) {
        JourneyWarning newJourneyWarning = new JourneyWarning();
        newJourneyWarning.setDate(journeyEntry.getDate());
        newJourneyWarning.getWarningTypes().add(warning);
        return newJourneyWarning;
    }
}
