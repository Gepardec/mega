package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvalidJourneyCalculator implements WarningCalculationStrategy<JourneyWarning> {

    @Override
    public List<JourneyWarning> calculate(List<ProjectEntry> projectTimeEntries) {
        final Map<LocalDate, List<JourneyTimeEntry>> groupedProjectTimeEntries = projectTimeEntries.stream()
                .filter(entry -> Task.isJourney(entry.getTask()))
                .map(JourneyTimeEntry.class::cast)
                .sorted(Comparator.comparing(ProjectEntry::getFromTime))
                .collect(Collectors.groupingBy(ProjectEntry::getDate));

        final List<JourneyWarning> warnings = new ArrayList<>();
        final JourneyDirectionValidator journeyDirectionValidator = new JourneyDirectionValidator();

        for (final Map.Entry<LocalDate, List<JourneyTimeEntry>> entriesPerDay : groupedProjectTimeEntries.entrySet()) {
            final List<JourneyTimeEntry> entries = entriesPerDay.getValue();
            for (int i = 0; i < entries.size(); i++) {
                final JourneyTimeEntry current = entries.get(i);
                final JourneyDirection nextDirection = tryToGetNextJourneyDirection(entries, i);
                JourneyWarningType warning = journeyDirectionValidator.validate(current.getJourneyDirection(), nextDirection);
                if (warning != null) {
                    warnings.add(createJourneyWarningWithEnumType(current, warning));
                }
            }
            return warnings;
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
