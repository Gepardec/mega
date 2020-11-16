package com.gepardec.mega.service.api.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.domain.model.StepEntry;

import java.util.List;
import java.util.Optional;

public interface StepEntryService {
    Optional<State> findEmployeeCheckState(final Employee employee);

    boolean areOtherChecksDone(final Employee employee);

    void addStepEntry(final StepEntry stepEntry);

    boolean setOpenAndAssignedStepEntriesDone(Employee employee, Long stepId);

    List<com.gepardec.mega.db.entity.StepEntry> findAllStepEntriesForEmployee(Employee employee);

    com.gepardec.mega.db.entity.StepEntry findStepEntryForEmployeeAtStep(Long stepId, Employee employee);
}
