package com.gepardec.mega.service.api.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;

public interface StepEntryService {
    State getEmcState(Employee employee);
}
