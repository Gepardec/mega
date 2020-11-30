package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.journey.InvalidJourneyCalculator;
import com.gepardec.mega.domain.calculation.journey.InvalidWorkingLocationInJourneyCalculator;
import com.gepardec.mega.domain.calculation.time.ExceededMaximumWorkingHoursPerDayCalculator;
import com.gepardec.mega.domain.calculation.time.InsufficientBreakCalculator;
import com.gepardec.mega.domain.calculation.time.InsufficientRestCalculator;
import com.gepardec.mega.domain.model.monthlyreport.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class WarningCalculator {

    private static final String MESSAGE_KEY_TEMPLATE = "warning.%s";

    @Inject
    ResourceBundle messages;

    private static final List<WarningCalculationStrategy<TimeWarning>> timeWarningCalculators = List.of(
            new ExceededMaximumWorkingHoursPerDayCalculator(),
            new InsufficientBreakCalculator(),
            new InsufficientRestCalculator());

    private static final List<WarningCalculationStrategy<JourneyWarning>> journeyWarningCalculators = List.of(
            new InvalidJourneyCalculator(),
            new InvalidWorkingLocationInJourneyCalculator()
    );

    public List<TimeWarning> determineTimeWarnings(List<ProjectEntry> projectTimeList) {
        final List<TimeWarning> warnings = new ArrayList<>();
        for (final WarningCalculationStrategy<TimeWarning> calculation : timeWarningCalculators) {
            final List<TimeWarning> calculatedWarnings = calculation.calculate(projectTimeList);
            calculatedWarnings.forEach(warning -> addToTimeWarnings(warnings, warning));
        }

        warnings.sort(Comparator.comparing(ProjectEntryWarning::getDate));
        return warnings;
    }

    public List<JourneyWarning> determineJourneyWarnings(List<ProjectEntry> projectTimeList) {
        final List<JourneyWarning> warnings = new ArrayList<>();
        for (WarningCalculationStrategy<JourneyWarning> calculator : journeyWarningCalculators) {
            final List<JourneyWarning> calculatedWarnings = calculator.calculate(projectTimeList);
            calculatedWarnings.forEach(warning -> addToJourneyWarnings(warnings, warning));
        }
        warnings.sort(Comparator.comparing(JourneyWarning::getDate));
        return warnings;
    }

    private void addToTimeWarnings(final List<TimeWarning> warnings, TimeWarning newTimeWarning) {
        Optional<TimeWarning> breakWarning = warnings.stream()
                .filter(bw -> bw.getDate().isEqual(newTimeWarning.getDate()))
                .findAny();

        if (breakWarning.isPresent()) {
            breakWarning.get().mergeBreakWarnings(newTimeWarning);
        } else {
            warnings.add(newTimeWarning);
        }
    }

    private void addToJourneyWarnings(final List<JourneyWarning> warnings, JourneyWarning newJourneyWarning) {

        // convert enum type to string message
        newJourneyWarning.getWarningTypes()
                .forEach(warningType -> newJourneyWarning.getWarnings().add(getTextByWarning(warningType)));

        Optional<JourneyWarning> journeyWarning = warnings.stream()
                .filter(warn -> warn.getDate().isEqual(newJourneyWarning.getDate()))
                .findAny();

        if (journeyWarning.isPresent()) {
            journeyWarning.get().getWarnings().addAll(newJourneyWarning.getWarnings());
            journeyWarning.get().getWarningTypes().addAll(newJourneyWarning.getWarningTypes());
        } else {
            warnings.add(newJourneyWarning);
        }
    }

    private String getTextByWarning(Warning warning) {
        return messages.getString(String.format(MESSAGE_KEY_TEMPLATE, warning.name()));
    }
}
