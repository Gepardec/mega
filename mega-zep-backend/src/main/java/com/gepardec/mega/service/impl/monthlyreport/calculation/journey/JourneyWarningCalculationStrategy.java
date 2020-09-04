package com.gepardec.mega.service.impl.monthlyreport.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;

import java.util.List;

public interface JourneyWarningCalculationStrategy {

    List<JourneyWarning> calculate(final List<ProjectTimeEntry> projectTimeEntries);

}
