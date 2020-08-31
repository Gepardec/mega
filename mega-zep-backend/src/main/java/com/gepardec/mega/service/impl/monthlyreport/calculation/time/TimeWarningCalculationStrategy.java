package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;

import java.time.LocalDate;
import java.util.List;

public interface TimeWarningCalculationStrategy {

    List<TimeWarning> calculate(final List<ProjectTimeEntry> projectTimeEntries);

}
