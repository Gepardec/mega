package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.journey.InvalidJourneyCalculator;
import com.gepardec.mega.domain.calculation.journey.InvalidWorkingLocationInJourneyCalculator;
import com.gepardec.mega.domain.calculation.time.CoreWorkingHoursCalculator;
import com.gepardec.mega.domain.calculation.time.ExceededMaximumWorkingHoursPerDayCalculator;
import com.gepardec.mega.domain.calculation.time.HolidayCalculator;
import com.gepardec.mega.domain.calculation.time.InsufficientBreakCalculator;
import com.gepardec.mega.domain.calculation.time.InsufficientRestCalculator;
import com.gepardec.mega.domain.calculation.time.NoEntryCalculator;
import com.gepardec.mega.domain.calculation.time.TimeOverlapCalculator;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntryWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.WarningType;
import de.provantis.zep.FehlzeitType;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@ApplicationScoped
public class WarningCalculator {

    private static final List<WarningCalculationStrategy<TimeWarning>> timeWarningCalculators = List.of(
            new ExceededMaximumWorkingHoursPerDayCalculator(),
            new InsufficientBreakCalculator(),
            new InsufficientRestCalculator(),
            new CoreWorkingHoursCalculator(),
            new TimeOverlapCalculator(),
            new HolidayCalculator());

    private static final List<WarningCalculationStrategy<JourneyWarning>> journeyWarningCalculators = List.of(
            new InvalidJourneyCalculator(),
            new InvalidWorkingLocationInJourneyCalculator()
    );

    @Inject
    ResourceBundle messages;

    public List<TimeWarning> determineTimeWarnings(List<ProjectEntry> projectTimeList) {
        final List<TimeWarning> warnings = new ArrayList<>();

        timeWarningCalculators.forEach(calculation -> {
            final List<TimeWarning> calculatedWarnings = calculation.calculate(projectTimeList);
            calculatedWarnings.forEach(warning -> addToTimeWarnings(warnings, warning));
        });

        warnings.sort(Comparator.comparing(ProjectEntryWarning::getDate));
        return warnings;
    }

    public List<JourneyWarning> determineJourneyWarnings(List<ProjectEntry> projectTimeList) {
        final List<JourneyWarning> warnings = new ArrayList<>();

        journeyWarningCalculators.forEach(calculator -> {
            final List<JourneyWarning> calculatedWarnings = calculator.calculate(projectTimeList);
            calculatedWarnings.forEach(warning -> addToJourneyWarnings(warnings, warning));
        });

        warnings.sort(Comparator.comparing(JourneyWarning::getDate));
        return warnings;
    }

    public List<TimeWarning> determineNoTimeEntries(List<ProjectEntry> projectEntries, List<FehlzeitType> absenceEntries) {
        NoEntryCalculator calculator = new NoEntryCalculator();
        final List<TimeWarning> warnings = new ArrayList<>();

        final List<TimeWarning> calculatedWarnings = calculator.calculate(projectEntries, absenceEntries);
        calculatedWarnings.forEach(warning -> addToTimeWarnings(warnings, warning));

        warnings.sort(Comparator.comparing(ProjectEntryWarning::getDate));
        return warnings;
    }

    private void addToTimeWarnings(final List<TimeWarning> warnings, TimeWarning newTimeWarning) {
        // convert enum type to string message
        newTimeWarning.getWarningTypes()
                .forEach(warningType -> newTimeWarning.getWarnings().add(textForWarningType(warningType)));

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
                .forEach(warningType -> newJourneyWarning.getWarnings().add(textForWarningType(warningType)));

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

    private String textForWarningType(WarningType warning) {
        return messages.getString(warning.messageTemplate());
    }
}
