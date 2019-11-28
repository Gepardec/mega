package com.gepardec.mega.monthendreport;

import com.gepardec.mega.utils.DateUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.gepardec.mega.monthendreport.TimeWarning.*;

class WarningCalculator {

    private List<TimeWarning> timeWarnings = new ArrayList<>(0);

    private List<JourneyWarning> journeyWarnings = new ArrayList<>(0);


    public List<TimeWarning> determineTimeWarnings(ProjectTimeManager projectTimeManager) {

        Set<Map.Entry<LocalDate, List<ProjectTimeEntry>>> entries = projectTimeManager.getProjectTimes().entrySet();

        //1. more than 10 hours a day
        entries.forEach(e -> checkFor10Hours(e.getValue(), e.getKey()));

        //2. no break when more than 6 hours a day
        entries.forEach(e -> checkForBreaksForWorkingDaysWithMoreThan6Hours(e.getValue(), e.getKey()));

        //3. check earliest start and latest ending
        projectTimeManager.getEntriesAsFlatList().forEach(e -> checkForFlexibleWorkFrame(e));

        //4. check fore enough rest
        checkForRestTime(projectTimeManager);

        timeWarnings.sort(Comparator.comparing(TimeWarning::getDate));
        return timeWarnings;
    }


    private void checkFor10Hours(List<ProjectTimeEntry> entriesPerDay, LocalDate date) {
        double workDurationOfDay = calcWorkingDurationOfDay(entriesPerDay);
        if (workDurationOfDay > MAX_HOURS_A_DAY) {
            TimeWarning timeWarning = new TimeWarning();
            timeWarning.setDate(date);
            timeWarning.setDay(DateUtils.getDayByDate(date));
            timeWarning.setTooMuchWorkTime(workDurationOfDay - MAX_HOURS_A_DAY);
            timeWarning.addWarning(WarningType.WARNING_TIME_MORE_THAN_10_HOURS);
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
                timeWarning.setDay(DateUtils.getDayByDate(date));
                timeWarning.setTooLessBreak(MIN_REQUIRED_BREAK_TIME - breakTime);
                timeWarning.addWarning(WarningType.WARNING_TIME_TOO_LESS_BREAK);
                addToTimeWarnings(timeWarning);
            }
        }
    }

    private void checkForFlexibleWorkFrame(ProjectTimeEntry projectTimeEntry) {
        if (projectTimeEntry.getFromTime().toLocalTime().isBefore(EARLIEST_START_TIME)) {
            TimeWarning timeWarning = new TimeWarning();
            timeWarning.setDate(projectTimeEntry.getDate());
            timeWarning.addWarning(WarningType.WARNING_TIME_TOO_EARLY_START);
            addToTimeWarnings(timeWarning);
        }
        if (projectTimeEntry.getToTime().toLocalTime().isAfter(LATEST_END_TIME)) {
            TimeWarning timeWarning = new TimeWarning();
            timeWarning.setDate(projectTimeEntry.getDate());
            timeWarning.addWarning(WarningType.WARNING_TIME_TOO_LATE_END);
            addToTimeWarnings(timeWarning);
        }
    }

    private void checkForRestTime(ProjectTimeManager projectTimeManager) {

        List<ProjectTimeEntry> entries = projectTimeManager.getProjectTimes().values().stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(entry -> entry.getFromTime()))
                .collect(Collectors.toList());

        for (int i = 0; i < entries.size(); i++) {
            ProjectTimeEntry actualEntry = entries.get(i);
            if (i + 1 < entries.size()) {
                ProjectTimeEntry nextEntry = entries.get(i + 1);
                if (nextEntry.getDate().isEqual(actualEntry.getDate().plusDays(1L))) {
                    double restHours = DateUtils.calcDiffInHours(actualEntry.getToTime(), nextEntry.getFromTime());
                    if (restHours < MIN_REQUIRED_REST_TIME) {
                        TimeWarning timeWarning = new TimeWarning();
                        timeWarning.setDate(nextEntry.getDate());
                        timeWarning.setDay(DateUtils.getDayByDate(nextEntry.getDate()));
                        timeWarning.setTooLessRest(MIN_REQUIRED_REST_TIME - restHours);
                        timeWarning.addWarning(WarningType.WARNING_TIME_TOO_LESS_REST);
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
        double dayWorkingDuration = 0.0d;
        for (ProjectTimeEntry entry : entriesPerDay) {
            if (!Task.isJourney(entry.getTask())) {
                dayWorkingDuration += entry.getDurationInHours();
            }
        }
        return dayWorkingDuration;
    }


    public List<JourneyWarning> determineJourneyWarnings(ProjectTimeManager projectTimeManager) {

        JourneyDirectionHandler journeyDirectionHandler = new JourneyDirectionHandler();
        for (ProjectTimeEntry projectTimeEntry : projectTimeManager.getEntriesAsFlatList()) {

            if (projectTimeEntry instanceof JourneyEntry) {
                JourneyEntry journeyEntry = (JourneyEntry) projectTimeEntry;
                journeyDirectionHandler.moveTo(journeyEntry.getJourneyDirection())
                        .ifPresent(warningType -> addToJourneyWarnings(journeyEntry, warningType));
            }
        }
        return journeyWarnings;
    }


    private void addToJourneyWarnings(JourneyEntry journeyEntry, WarningType warning) {
        JourneyWarning newJourneyWarning = new JourneyWarning();
        newJourneyWarning.setDate(journeyEntry.getDate());
        newJourneyWarning.setDay(DateUtils.getDayByDate(journeyEntry.getDate()));
        newJourneyWarning.getWarnings().add(warning);


        Optional<JourneyWarning> journeyWarning = journeyWarnings.stream()
                .filter(warn -> warn.getDate().isEqual(journeyEntry.getDate()))
                .findAny();

        if (journeyWarning.isPresent()) {
            journeyWarning.get().getWarnings().addAll(newJourneyWarning.getWarnings());
        } else {
            journeyWarnings.add(newJourneyWarning);
        }
    }
}
