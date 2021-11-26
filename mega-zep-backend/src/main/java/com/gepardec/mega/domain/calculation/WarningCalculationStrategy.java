package com.gepardec.mega.domain.calculation;

import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntryWarning;
import de.provantis.zep.FehlzeitType;

import java.util.List;

public interface WarningCalculationStrategy<T extends ProjectEntryWarning> {

    List<T> calculate(final List<ProjectEntry> projectTimeEntries);

}
