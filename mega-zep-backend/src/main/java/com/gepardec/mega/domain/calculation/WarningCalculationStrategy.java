package com.gepardec.mega.domain.calculation;

import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntryWarning;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;

import java.util.List;

public interface WarningCalculationStrategy<T extends ProjectEntryWarning> {

    List<T> calculate(final List<ProjectEntry> projectTimeEntries);

}
