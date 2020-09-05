package com.gepardec.mega.service.impl.monthlyreport.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.Warning;

import java.util.ArrayList;
import java.util.List;

public class JourneyWarningCalculator implements JourneyWarningCalculationStrategy {

    @Override
    public List<JourneyWarning> calculate(List<JourneyTimeEntry> projectTimeEntries) {
        final JourneyDirectionValidator journeyDirectionValidator = new JourneyDirectionValidator();
        final List<JourneyWarning> warnings = new ArrayList<>();

        for (int i = 0; i < projectTimeEntries.size(); i++) {
            final JourneyTimeEntry current = projectTimeEntries.get(i);
            final JourneyDirection nextDirection = tryToGetNextJourneyDirection(projectTimeEntries, i);
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
