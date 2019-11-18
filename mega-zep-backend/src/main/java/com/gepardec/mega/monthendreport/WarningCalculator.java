package com.gepardec.mega.monthendreport;

import com.gepardec.mega.utils.DateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gepardec.mega.monthendreport.BreakWarning.MAX_HOURS_A_DAY;
import static com.gepardec.mega.monthendreport.BreakWarning.MAX_HOURS_OF_DAY_WITHOUT_BREAK;

class WarningCalculator {

    private List<BreakWarning> breakWarnings = new ArrayList<>(0);

    public List<BreakWarning> createBreakWarnings(ProjectTimeEntries projectTimeEntries) {

        //1. more than 10 hours a day
        projectTimeEntries.getProjectTimeEntries().entrySet()
                .forEach(e -> checkFor10Hours(e.getValue(), e.getKey()));

        //2. no break when more than 6 hours a day


        //3. no rest after the day


        return breakWarnings;
    }

    private void checkFor10Hours(List<ProjectTimeEntry> entriesPerDay, LocalDate date) {
        double workHoursOfDay = calcWorkHoursOfDay(entriesPerDay);
        if(workHoursOfDay > MAX_HOURS_A_DAY) {
            BreakWarning breakWarning = new BreakWarning();
            breakWarning.setDate(date);
            breakWarning.setDay(DateUtils.getDayByDate(date));
            breakWarning.setTooMuchWorkTime(workHoursOfDay - MAX_HOURS_A_DAY);
            breakWarning.addWarning(BreakWarningType.WARNING_MORE_THAN_10_HOURS);
            breakWarnings.add(breakWarning);
        }
    }


    private void checkForBreaksForWorkingDaysWithMoreThan6Hours(List<ProjectTimeEntry> entriesPerDay, LocalDate date) {
       double workHoursOfDay = calcWorkHoursOfDay(entriesPerDay);
       if(workHoursOfDay > MAX_HOURS_OF_DAY_WITHOUT_BREAK) {

       }

    }


    private static double calcWorkHoursOfDay(List<ProjectTimeEntry> entriesPerDay) {
        double duration = 0.0d;
        for(ProjectTimeEntry entry : entriesPerDay) {
            duration += entry.getDurationInHours();
        }
        return duration;
    }



    public List<JourneyWarning> createJoureyWarnings(ProjectTimeEntries projectTimeEntries) {
        return new ArrayList<>(0);
    }
}
