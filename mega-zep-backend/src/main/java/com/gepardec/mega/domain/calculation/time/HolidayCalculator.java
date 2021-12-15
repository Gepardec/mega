package com.gepardec.mega.domain.calculation.time;

import com.gepardec.mega.domain.calculation.AbstractTimeWarningCalculationStrategy;
import com.gepardec.mega.domain.calculation.WarningCalculationStrategy;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayCalculator extends AbstractTimeWarningCalculationStrategy implements WarningCalculationStrategy<TimeWarning> {
    @Override
    public List<TimeWarning> calculate(List<ProjectEntry> projectEntries) {
        List<TimeWarning> warnings = new ArrayList<>();

        if (projectEntries.isEmpty()) {
            return new ArrayList<>();
        }

        projectEntries.forEach(entry -> {
            if (isHoliday(entry.getDate())) {
                warnings.add(createTimeWarning(entry.getDate()));
            }
        });

        return warnings;
    }

    private boolean isHoliday(LocalDate date) {
        HolidayManager holidayManager = HolidayManager.getInstance(HolidayCalendar.AUSTRIA);
        return holidayManager.isHoliday(date);
    }

    private TimeWarning createTimeWarning(final LocalDate date) {
        TimeWarning timeWarning = new TimeWarning();
        timeWarning.setDate(date);
        timeWarning.getWarningTypes().add(TimeWarningType.HOLIDAY);

        return timeWarning;
    }
}
