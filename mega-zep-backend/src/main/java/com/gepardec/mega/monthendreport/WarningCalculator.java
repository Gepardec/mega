package com.gepardec.mega.monthendreport;

import com.gepardec.mega.utils.DateUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.gepardec.mega.monthendreport.BreakWarning.*;

class WarningCalculator {

    private List<BreakWarning> breakWarnings = new ArrayList<>(0);


    public List<BreakWarning> createBreakWarnings(ProjectTimeEntries projectTimeEntries) {

        //1. more than 10 hours a day
        projectTimeEntries.getProjectTimeEntries().entrySet()
                .forEach(e -> checkFor10Hours(e.getValue(), e.getKey()));

        //2. no break when more than 6 hours a day
        projectTimeEntries.getProjectTimeEntries().entrySet()
                .forEach(e -> checkForBreaksForWorkingDaysWithMoreThan6Hours(e.getValue(), e.getKey()));

        checkForRestTime(projectTimeEntries);

        breakWarnings.sort(Comparator.comparing(BreakWarning::getDate));
        return breakWarnings;
    }


    private void checkFor10Hours(List<ProjectTimeEntry> entriesPerDay, LocalDate date) {
        double workDurationOfDay = calcWorkingDurationOfDay(entriesPerDay);
        if (workDurationOfDay > MAX_HOURS_A_DAY) {
            BreakWarning breakWarning = new BreakWarning();
            breakWarning.setDate(date);
            breakWarning.setDay(DateUtils.getDayByDate(date));
            breakWarning.setTooMuchWorkTime(workDurationOfDay - MAX_HOURS_A_DAY);
            breakWarning.setWarning(WarningType.WARNING_MORE_THAN_10_HOURS);
            mergeIntoWarnings(breakWarning);
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
                BreakWarning breakWarning = new BreakWarning();
                breakWarning.setDate(date);
                breakWarning.setDay(DateUtils.getDayByDate(date));
                breakWarning.setTooLessBreak(MIN_REQUIRED_BREAK_TIME - breakTime);
                breakWarning.setWarning(WarningType.WARNING_TOO_LESS_BREAK);
                mergeIntoWarnings(breakWarning);
            }
        }
    }

    private void checkForRestTime(ProjectTimeEntries projectTimeEntries) {

        List<ProjectTimeEntry> entries = projectTimeEntries.getProjectTimeEntries().values().stream()
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
                        BreakWarning breakWarning = new BreakWarning();
                        breakWarning.setDate(nextEntry.getDate());
                        breakWarning.setDay(DateUtils.getDayByDate(nextEntry.getDate()));
                        breakWarning.setTooLessRest(MIN_REQUIRED_REST_TIME - restHours);
                        breakWarning.setWarning(WarningType.WARNING_TOO_LESS_REST);
                        mergeIntoWarnings(breakWarning);
                    }
                }
            }
        }
    }


    private void mergeIntoWarnings(BreakWarning newBreakWarning) {
        Optional<BreakWarning> breakWarning = breakWarnings.stream()
                .filter(bw -> bw.getDate().isEqual(newBreakWarning.getDate()))
                .findFirst();

        if (breakWarning.isPresent()) {
            breakWarning.get().mergeBreakWarnings(newBreakWarning);
        } else {
            breakWarnings.add(newBreakWarning);
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


    public List<JourneyWarning> createJourneyWarnings(ProjectTimeEntries projectTimeEntries) {
        return new ArrayList<>(0);
    }
}
