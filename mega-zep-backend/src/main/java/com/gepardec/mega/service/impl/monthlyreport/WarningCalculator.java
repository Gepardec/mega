package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.model.monthlyreport.*;
import com.gepardec.mega.domain.utils.DateUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.gepardec.mega.domain.model.monthlyreport.TimeWarning.*;

public class WarningCalculator {

    private static final String MESSAGE_KEY_TEMPLATE = "warning.%s";

    private final List<TimeWarning> timeWarnings = new ArrayList<>(0);
    private final List<JourneyWarning> journeyWarnings = new ArrayList<>(0);
    private final ResourceBundle messages;

    public WarningCalculator(ResourceBundle messages) {
        this.messages = messages;
    }

    public List<TimeWarning> determineTimeWarnings(List<ProjectTimeEntry> projectTimeList) {
        Set<Map.Entry<LocalDate, List<ProjectTimeEntry>>> entrySet = projectTimeList.stream()
                .sorted(Comparator.comparing(ProjectTimeEntry::getFromTime))
                .collect(Collectors.groupingBy(ProjectTimeEntry::getDate, LinkedHashMap::new, Collectors.toUnmodifiableList())).entrySet();

        //1. more than 10 hours a day
        entrySet.forEach(e -> checkFor10Hours(e.getValue(), e.getKey()));

        //2. no break when more than 6 hours a day
        entrySet.forEach(e -> checkForBreaksForWorkingDaysWithMoreThan6Hours(e.getValue(), e.getKey()));

        //3. check earliest start and latest ending
//        not activated now
//        projectTimeManager.getEntriesAsFlatList().forEach(e -> checkForFlexibleWorkFrame(e));

        //4. check fore enough rest
        checkForRestTime(projectTimeList);

        timeWarnings.sort(Comparator.comparing(TimeWarning::getDate));
        return timeWarnings;
    }


    private void checkFor10Hours(List<ProjectTimeEntry> entriesPerDay, LocalDate date) {
        double workDurationOfDay = calcWorkingDurationOfDay(entriesPerDay);
        if (workDurationOfDay > MAX_HOURS_A_DAY) {
            TimeWarning timeWarning = new TimeWarning();
            timeWarning.setDate(date);
            timeWarning.setExcessWorkTime(workDurationOfDay - MAX_HOURS_A_DAY);
            addToTimeWarnings(timeWarning);
        }
    }


    private void checkForBreaksForWorkingDaysWithMoreThan6Hours(List<ProjectTimeEntry> entriesPerDay, LocalDate date) {
        double workHoursOfDay = calcWorkingDurationOfDay(entriesPerDay);
        if (workHoursOfDay > MAX_HOURS_OF_DAY_WITHOUT_BREAK) {
            double breakTime = 0d;
            for (int i = 0; i < entriesPerDay.size(); i++) {
                ProjectTimeEntry actualEntry = entriesPerDay.get(i);
                if (i + 1 < entriesPerDay.size()) {
                    ProjectTimeEntry nextEntry = entriesPerDay.get(i + 1);
                    breakTime += DateUtils.calcDiffInHours(actualEntry.getToTime(), nextEntry.getFromTime());

                }
            }
            if (breakTime < MIN_REQUIRED_BREAK_TIME) {
                TimeWarning timeWarning = new TimeWarning();
                timeWarning.setDate(date);
                timeWarning.setMissingBreakTime(MIN_REQUIRED_BREAK_TIME - breakTime);
                addToTimeWarnings(timeWarning);
            }
        }
    }

    // FIXME GAJ: not used at the moment may still be useful later -> remove?!?
    private void checkForFlexibleWorkFrame(ProjectTimeEntry projectTimeEntry) {
        if (projectTimeEntry.getFromTime().toLocalTime().isBefore(EARLIEST_START_TIME)) {
            TimeWarning timeWarning = new TimeWarning();
            timeWarning.setDate(projectTimeEntry.getDate());
            addToTimeWarnings(timeWarning);
        }
        if (projectTimeEntry.getToTime().toLocalTime().isAfter(LATEST_END_TIME)) {
            TimeWarning timeWarning = new TimeWarning();
            timeWarning.setDate(projectTimeEntry.getDate());
            addToTimeWarnings(timeWarning);
        }
    }

    private void checkForRestTime(List<ProjectTimeEntry> projectTimeEntryList) {

        List<ProjectTimeEntry> filteredEntries = projectTimeEntryList.stream()
                .filter(entry -> Task.isTask(entry.getTask()))
                .sorted(Comparator.comparing(ProjectTimeEntry::getFromTime))
                .collect(Collectors.toList());

        for (int i = 0; i < filteredEntries.size(); i++) {
            ProjectTimeEntry actualEntry = filteredEntries.get(i);
            if (i + 1 < filteredEntries.size()) {
                ProjectTimeEntry nextEntry = filteredEntries.get(i + 1);
                if (nextEntry.getDate().isEqual(actualEntry.getDate().plusDays(1L))) {
                    double restHours = DateUtils.calcDiffInHours(actualEntry.getToTime(), nextEntry.getFromTime());
                    if (restHours < MIN_REQUIRED_REST_TIME) {
                        TimeWarning timeWarning = new TimeWarning();
                        timeWarning.setDate(nextEntry.getDate());
                        timeWarning.setMissingRestTime(MIN_REQUIRED_REST_TIME - restHours);
                        addToTimeWarnings(timeWarning);
                    }
                }
            }
        }
    }


    private void addToTimeWarnings(TimeWarning newTimeWarning) {
        Optional<TimeWarning> breakWarning = timeWarnings.stream()
                .filter(bw -> bw.getDate().isEqual(newTimeWarning.getDate()))
                .findAny();

        if (breakWarning.isPresent()) {
            breakWarning.get().mergeBreakWarnings(newTimeWarning);
        } else {
            timeWarnings.add(newTimeWarning);
        }
    }

    private static double calcWorkingDurationOfDay(List<ProjectTimeEntry> entriesPerDay) {
        return entriesPerDay.stream()
                .filter(entry -> Task.isTask(entry.getTask()))
                .map(ProjectTimeEntry::getDurationInHours)
                .collect(Collectors.summarizingDouble(Double::doubleValue))
                .getSum();
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
