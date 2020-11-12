package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JourneyWarningCalculator implements WarningCalculationStrategy<JourneyWarning> {

    @Override
    public List<JourneyWarning> calculate(List<ProjectEntry> projectTimeEntries) {
        final JourneyDirectionValidator journeyDirectionValidator = new JourneyDirectionValidator();
        final List<JourneyWarning> warnings = new ArrayList<>();

        final List<JourneyTimeEntry> filteredProjectEntries = projectTimeEntries.stream()
                .filter(entry -> Task.isJourney(entry.getTask()))
                .map(JourneyTimeEntry.class::cast)
                .sorted(Comparator.comparing(JourneyTimeEntry::getFromTime))
                .collect(Collectors.toList());
        for (int i = 0; i < filteredProjectEntries.size(); i++) {
            final JourneyTimeEntry current = filteredProjectEntries.get(i);
            final JourneyDirection nextDirection = tryToGetNextJourneyDirection(filteredProjectEntries, i);
            Warning warning = journeyDirectionValidator.validate(current.getJourneyDirection(), nextDirection);
            if (warning != null) {
                warnings.add(createJourneyWarningWithEnumType(current, warning));
            }
        }
        return warnings;
    }

    private JourneyDirection tryToGetNextJourneyDirection(final List<JourneyTimeEntry> journeyTimeEntries, final int idx) {
        return (idx + 1 < journeyTimeEntries.size()) ? journeyTimeEntries.get(idx + 1).getJourneyDirection() : null;
    }

    private JourneyWarning createJourneyWarningWithEnumType(JourneyTimeEntry journeyEntry, Warning warning) {
        JourneyWarning newJourneyWarning = new JourneyWarning();
        newJourneyWarning.setDate(journeyEntry.getDate());
        newJourneyWarning.getWarningTypes().add(warning);
        return newJourneyWarning;
    }
}
