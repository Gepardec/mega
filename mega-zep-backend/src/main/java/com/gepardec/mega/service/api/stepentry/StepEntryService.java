package com.gepardec.mega.service.api.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;

import java.util.Optional;

public interface StepEntryService {
    Optional<State> findEmployeeCheckState(final Employee employee);

    boolean areOtherChecksDone(final Employee employee);
}
