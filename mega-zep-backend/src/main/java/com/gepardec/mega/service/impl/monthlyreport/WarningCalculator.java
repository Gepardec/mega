package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.model.monthlyreport.*;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.MoreThan10HourADayTimeWarningCalculationStrategy;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.NotEnoughRestTimeTimeWarningCalculator;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.TimeWarningCalculationStrategy;
import com.gepardec.mega.service.impl.monthlyreport.calculation.time.ToLittleRestTimeForWorkingDayWithMoreThan6Hours;

import java.util.*;

public class WarningCalculator {

    private static final String MESSAGE_KEY_TEMPLATE = "warning.%s";

    private final List<JourneyWarning> journeyWarnings = new ArrayList<>(0);
    private final ResourceBundle messages;

    private static final List<TimeWarningCalculationStrategy> timeWarningCalculators = Arrays.asList(
            new MoreThan10HourADayTimeWarningCalculationStrategy(),
            new ToLittleRestTimeForWorkingDayWithMoreThan6Hours(),
            new NotEnoughRestTimeTimeWarningCalculator());

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

    public List<JourneyWarning> determineJourneyWarnings(List<ProjectTimeEntry> projectTimeEntryList) {
        JourneyDirectionHandler journeyDirectionHandler = new JourneyDirectionHandler();
        Optional<JourneyEntry> lastJourneyEntry = Optional.empty();

        for (int i = 0; i < projectTimeEntryList.size(); i++) {
            ProjectTimeEntry projectTimeEntry = projectTimeEntryList.get(i);

            if (projectTimeEntry instanceof JourneyEntry) {
                JourneyEntry journeyEntry = (JourneyEntry) projectTimeEntry;
                journeyDirectionHandler.moveTo(journeyEntry.getJourneyDirection())
                        .ifPresent(warning -> addToJourneyWarnings(journeyEntry, warning));
                lastJourneyEntry = Optional.of(journeyEntry);
            }

            if (i == projectTimeEntryList.size() - 1 && !journeyDirectionHandler.isJourneyFinished()) {
                lastJourneyEntry.ifPresent(journeyEntry -> journeyDirectionHandler.moveTo(JourneyDirection.INVALIDATE)
                        .ifPresent(warning -> addToJourneyWarnings(journeyEntry, warning)));
            }
        }
        return journeyWarnings;
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


    private void addToJourneyWarnings(JourneyEntry journeyEntry, Warning warning) {
        JourneyWarning newJourneyWarning = new JourneyWarning();
        newJourneyWarning.setDate(journeyEntry.getDate());
        newJourneyWarning.getWarnings().add(getTextByWarning(warning));


        Optional<JourneyWarning> journeyWarning = journeyWarnings.stream()
                .filter(warn -> warn.getDate().isEqual(journeyEntry.getDate()))
                .findAny();

        if (journeyWarning.isPresent()) {
            journeyWarning.get().getWarnings().addAll(newJourneyWarning.getWarnings());
        } else {
            journeyWarnings.add(newJourneyWarning);
        }
    }


    public String getTextByWarning(Warning warning) {
        return messages.getString(String.format(MESSAGE_KEY_TEMPLATE, warning.name()));
    }
}
