package com.gepardec.mega.service.impl.monthlyreport.calculation;

import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.Warning;
import com.gepardec.mega.service.impl.monthlyreport.calculation.journey.JourneyWarningCalculationStrategy;
import com.gepardec.mega.service.impl.monthlyreport.calculation.journey.JourneyWarningCalculator;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.ExceededMaximumWorkingHoursPerDayTimeWarningCalculator;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculator;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.InsufficientRestTimeTimeWarningCalculator;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.TimeWarningCalculationStrategy;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class WarningCalculator {

    private static final String MESSAGE_KEY_TEMPLATE = "warning.%s";

    @Inject
    ResourceBundle messages;

    private static final List<TimeWarningCalculationStrategy> timeWarningCalculators = Arrays.asList(
            new ExceededMaximumWorkingHoursPerDayTimeWarningCalculator(),
            new InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculator(),
            new InsufficientRestTimeTimeWarningCalculator());

    private static final List<JourneyWarningCalculationStrategy> journeyWarningCalculators = Arrays.asList(
            new JourneyWarningCalculator()
    );

    public List<TimeWarning> determineTimeWarnings(List<ProjectTimeEntry> projectTimeList) {
        final List<TimeWarning> warnings = new ArrayList<>();
        for (final TimeWarningCalculationStrategy calculation : timeWarningCalculators) {
            final List<TimeWarning> calculatedWarnings = calculation.calculate(projectTimeList);
            calculatedWarnings.forEach(warning -> addToTimeWarnings(warnings, warning));
        }

        warnings.sort(Comparator.comparing(TimeWarning::getDate));
        return warnings;
    }

    public List<JourneyWarning> determineJourneyWarnings(List<ProjectTimeEntry> projectTimeList) {
        final List<JourneyWarning> warnings = new ArrayList<>();
        for (JourneyWarningCalculationStrategy calculator : journeyWarningCalculators) {
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
