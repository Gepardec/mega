package com.gepardec.mega.service.api.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.domain.model.User;

import java.time.LocalDate;
import java.util.Optional;

public interface StepEntryService {
    Optional<State> findEmployeeCheckState(final Employee employee);

    boolean areOtherChecksDone(final Employee employee);

    void addStepEntry(User owner, LocalDate date, String project, Step step, User assignee);
}
