package com.gepardec.mega.service.impl.monthlyreport.calculation.time;

import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;

import java.util.List;

public interface TimeWarningCalculationStrategy {

    List<TimeWarning> calculate(final List<ProjectEntry> projectTimeEntries);

}
