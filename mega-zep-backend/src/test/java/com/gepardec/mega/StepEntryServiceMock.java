package com.gepardec.mega;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Mock
public class StepEntryServiceMock implements StepEntryService {

    StepEntryService delegate;

    @Override
    public State getEmcState(Employee employee) {
        return delegate.getEmcState(employee);
    }
}
