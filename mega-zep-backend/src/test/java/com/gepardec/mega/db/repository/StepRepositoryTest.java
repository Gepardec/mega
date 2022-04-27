package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.employee.Step;
import com.gepardec.mega.domain.model.Role;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@Transactional
class StepRepositoryTest {

    @Inject
    StepRepository stepRepository;

    private Step step;

    @BeforeEach
    void init() {
        step = initializeStepObject();
        stepRepository.persist(step);
    }

    @AfterEach
    void tearDown() {
        assertThat(stepRepository.deleteById(step.getId())).isTrue();
    }

    @Test
    void findAllSteps_whenCalled_thenReturnsCorrectAmountOfSteps() {
        List<Step> result = stepRepository.findAllSteps();
        assertThat(result).hasSize(1);
    }

    @Test
    void findAllSteps_whenCalled_thenReturnedListContainsCorrectStep() {
        List<Step> result = stepRepository.findAllSteps();
        assertThat(result).contains(step);
    }

    @Test
    void findAllSteps_whenCalledAndNoStepExists_thenReturnsEmptyListOfSteps() {
        stepRepository.deleteById(step.getId());
        List<Step> result = stepRepository.findAllSteps();
        assertThat(result).isEmpty();
        createStep();
    }

    private void createStep() {
        step = new Step();
        step.setName("TestName");
        step.setRole(Role.EMPLOYEE);
        step.setOrdinal(1);
        step.setStepEntries(null);
        stepRepository.persist(step);
    }

    private Step initializeStepObject() {
        Step initStep = new Step();
        initStep.setName("TestName");
        initStep.setRole(Role.EMPLOYEE);
        initStep.setOrdinal(1);
        initStep.setStepEntries(null);

        return initStep;
    }
}
