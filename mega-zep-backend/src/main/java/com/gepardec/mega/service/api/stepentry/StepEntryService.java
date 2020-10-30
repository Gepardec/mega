package com.gepardec.mega.service.api.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.domain.model.User;

import java.time.LocalDate;
import java.util.Optional;

public interface StepEntryService {
    Optional<State> findEmployeeCheckState(final Employee employee);

    boolean areOtherChecksDone(final Employee employee);

    void addStepEntry(final User owner, final LocalDate date, final Project project, final Step step, final User assignee);
}
