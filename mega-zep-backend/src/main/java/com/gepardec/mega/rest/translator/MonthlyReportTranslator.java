package com.gepardec.mega.rest.translator;

import com.gepardec.mega.rest.model.Employee;
import com.gepardec.mega.rest.model.JourneyWarning;
import com.gepardec.mega.rest.model.MonthlyReport;
import com.gepardec.mega.rest.model.TimeWarning;

import java.util.List;
import java.util.stream.Collectors;

public class MonthlyReportTranslator {

    public static MonthlyReport to(final List<com.gepardec.mega.monthlyreport.warning.TimeWarning> timeWarnings,
            final List<com.gepardec.mega.monthlyreport.journey.JourneyWarning> journeyWarnings,
            final Employee employee) {
        final MonthlyReport report = new MonthlyReport();
        report.setEmployee(employee);
        report.setTimeWarnings(toTimeWarnings(timeWarnings));
        report.setJourneyWarnings(toJourneyWarnings(journeyWarnings));

        return report;
    }

    public static List<TimeWarning> toTimeWarnings(final List<com.gepardec.mega.monthlyreport.warning.TimeWarning> timeWarnings) {
        return timeWarnings.stream().map(MonthlyReportTranslator::toTimeWarning).collect(Collectors.toList());
    }

    public static TimeWarning toTimeWarning(com.gepardec.mega.monthlyreport.warning.TimeWarning timeWarning) {
        final TimeWarning warning = new TimeWarning();
        warning.setDate(timeWarning.getDate());
        warning.setExcessWorkTime(timeWarning.getExcessWorkTime());
        warning.setMissingBreakTime(timeWarning.getMissingBreakTime());
        warning.setMissingRestTime(timeWarning.getMissingRestTime());

        return warning;
    }

    public static List<JourneyWarning> toJourneyWarnings(final List<com.gepardec.mega.monthlyreport.journey.JourneyWarning> journeyWarnings) {
        return journeyWarnings.stream().map(MonthlyReportTranslator::toJourneyWarning).collect(Collectors.toList());
    }

    public static JourneyWarning toJourneyWarning(com.gepardec.mega.monthlyreport.journey.JourneyWarning journeyWarning) {
        final JourneyWarning warning = new JourneyWarning();
        warning.setDate(journeyWarning.getDate());
        warning.setWarnings(journeyWarning.getWarnings());

        return warning;
    }
}
