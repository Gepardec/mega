package com.gepardec.mega.service.impl.monthlyreport.calculation;

import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.service.impl.monthlyreport.calculation.journey.JourneyWarningCalculationStrategy;
import com.gepardec.mega.service.impl.monthlyreport.calculation.journey.JourneyWarningCalculator;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.ExceededMaximumWorkingHoursPerDayTimeWarningCalculator;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculator;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.InsufficientRestTimeTimeWarningCalculator;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.TimeWarningCalculationStrategy;

import java.util.*;

public class WarningCalculator {

    private final ResourceBundle messages;

    private static final List<TimeWarningCalculationStrategy> timeWarningCalculators = Arrays.asList(
            new ExceededMaximumWorkingHoursPerDayTimeWarningCalculator(),
            new InsufficientBreakTimeForWorkingDayWithMoreThan6HoursCalculator(),
            new InsufficientRestTimeTimeWarningCalculator());

    private static final List<JourneyWarningCalculationStrategy> journeyWarningCalculators = Arrays.asList(
            new JourneyWarningCalculator()
    );

    public WarningCalculator(ResourceBundle messages) {
        this.messages = messages;
    }

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
            if (calculator instanceof JourneyWarningCalculator) {
                ((JourneyWarningCalculator) calculator).setResourceBundle(messages);
            }
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
        Optional<JourneyWarning> journeyWarning = warnings.stream()
                .filter(warn -> warn.getDate().isEqual(newJourneyWarning.getDate()))
                .findAny();

        if (journeyWarning.isPresent()) {
            journeyWarning.get().getWarnings().addAll(newJourneyWarning.getWarnings());
        } else {
            warnings.add(newJourneyWarning);
        }
    }
}
