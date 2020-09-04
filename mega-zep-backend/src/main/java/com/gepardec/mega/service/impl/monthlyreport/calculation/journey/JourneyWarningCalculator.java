package com.gepardec.mega.service.impl.monthlyreport.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JourneyWarningCalculator implements JourneyWarningCalculationStrategy {

    @Override
    public List<JourneyWarning> calculate(List<ProjectTimeEntry> projectTimeEntries) {
        final JourneyDirectionHandler journeyDirectionHandler = new JourneyDirectionHandler();
        final List<JourneyWarning> warnings = new ArrayList<>();
        Optional<JourneyEntry> lastJourneyEntry = Optional.empty();

        for (int i = 0; i < projectTimeEntries.size(); i++) {
            ProjectTimeEntry projectTimeEntry = projectTimeEntries.get(i);

            if (projectTimeEntry instanceof JourneyEntry) {
                JourneyEntry journeyEntry = (JourneyEntry) projectTimeEntry;
                journeyDirectionHandler.moveTo(journeyEntry.getJourneyDirection())
                        .ifPresent(warning -> warnings.add(createJourneyWarningWithEnumType(journeyEntry, warning)));
                lastJourneyEntry = Optional.of(journeyEntry);
            }

            if (i == projectTimeEntries.size() - 1 && !journeyDirectionHandler.isJourneyFinished()) {
                lastJourneyEntry.ifPresent(journeyEntry -> journeyDirectionHandler.moveTo(JourneyDirection.INVALIDATE)
                        .ifPresent(warning -> warnings.add(createJourneyWarningWithEnumType(journeyEntry, warning))));
            }
        }
        return warnings;
    }

    private JourneyWarning createJourneyWarningWithEnumType(JourneyEntry journeyEntry, Warning warning) {
        JourneyWarning newJourneyWarning = new JourneyWarning();
        newJourneyWarning.setDate(journeyEntry.getDate());
        newJourneyWarning.getWarningTypes().add(warning);
        return newJourneyWarning;
    }
}
