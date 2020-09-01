package com.gepardec.mega.service.impl.monthlyreport.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class JourneyWarningCalculator implements JourneyWarningCalculationStrategy {

    private static final String MESSAGE_KEY_TEMPLATE = "warning.%s";

    private ResourceBundle messages;

    public void setResourceBundle(ResourceBundle messages) {
        this.messages = messages;
    }

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
                        .ifPresent(warning -> warnings.add(createJourneyWarning(journeyEntry, warning)));
                lastJourneyEntry = Optional.of(journeyEntry);
            }

            if (i == projectTimeEntries.size() - 1 && !journeyDirectionHandler.isJourneyFinished()) {
                lastJourneyEntry.ifPresent(journeyEntry -> journeyDirectionHandler.moveTo(JourneyDirection.INVALIDATE)
                        .ifPresent(warning -> warnings.add(createJourneyWarning(journeyEntry, warning))));
            }
        }
        return warnings;
    }

    private JourneyWarning createJourneyWarning(JourneyEntry journeyEntry, Warning warning) {
        JourneyWarning newJourneyWarning = new JourneyWarning();
        newJourneyWarning.setDate(journeyEntry.getDate());
        newJourneyWarning.getWarnings().add(getTextByWarning(warning));
        return newJourneyWarning;
    }

    private String getTextByWarning(Warning warning) {
        return messages.getString(String.format(MESSAGE_KEY_TEMPLATE, warning.name()));
    }
}
