package com.gepardec.mega.service.impl.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.db.repository.StepEntryRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@QuarkusTest
class StepEntryServiceImplTest {

    @Inject
    StepEntryService stepEntryService;

    @InjectMock
    private StepEntryRepository stepEntryRepository;

    @Test
    void findEmployeeCheckState_whenValidStepEntries_thenValidState() {
        StepEntry stepEntry1 = createStepEntry(1L);
        StepEntry stepEntry2 = createStepEntry(2L);

        List<StepEntry> stepEntries = List.of(stepEntry1, stepEntry2);
        when(stepEntryRepository.findAllOwnedAndAssignedStepEntriesInRange(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString())).thenReturn(stepEntries);

        Optional<State> states = stepEntryService.findEmployeeCheckState(createEmployee());
        Assertions.assertTrue(states.isPresent());
        Assertions.assertEquals(State.IN_PROGRESS, states.get());
    }

    @Test
    void findEmployeeCheckState_whenNoStepEntries_thenEmpty() {
        when(stepEntryRepository.findAllOwnedAndAssignedStepEntriesInRange(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString())).thenReturn(List.of());

        Optional<State> states = stepEntryService.findEmployeeCheckState(createEmployee());
        Assertions.assertTrue(states.isEmpty());
    }

    @Test
    void areOtherChecksDone_whenAllInProgress_thenFalse() {
        StepEntry stepEntry1 = createStepEntry(1L);
        StepEntry stepEntry2 = createStepEntry(2L);

        List<StepEntry> stepEntries = List.of(stepEntry1, stepEntry2);
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesInRange(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString())).thenReturn(stepEntries);

        boolean areOtherChecksDone = stepEntryService.areOtherChecksDone(createEmployee());
        Assertions.assertFalse(areOtherChecksDone);
    }

    @Test
    void areOtherChecksDone_whenNoStepEntries_thenTrue() {
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesInRange(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString())).thenReturn(List.of());

        boolean areOtherChecksDone = stepEntryService.areOtherChecksDone(createEmployee());
        Assertions.assertTrue(areOtherChecksDone);
    }

    @Test
    void areOtherChecksDone_whenAllDone_thenTrue() {
        StepEntry stepEntry1 = createStepEntry(1L);
        StepEntry stepEntry2 = createStepEntry(2L);

        stepEntry1.setState(State.DONE);
        stepEntry2.setState(State.DONE);

        List<StepEntry> stepEntries = List.of(stepEntry1, stepEntry2);
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesInRange(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString())).thenReturn(stepEntries);

        boolean areOtherChecksDone = stepEntryService.areOtherChecksDone(createEmployee());
        Assertions.assertTrue(areOtherChecksDone);
    }

    @Test
    void setOpenAndAssignedStepEntriesDone_when0_thenFalse() {
        when(stepEntryRepository.closeAssigned(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(0);

        boolean updated = stepEntryService.setOpenAndAssignedStepEntriesDone(createEmployee(), 0L);
        Assertions.assertFalse(updated);
    }

    @Test
    void setOpenAndAssignedStepEntriesDone_when1_thenTrue() {
        when(stepEntryRepository.closeAssigned(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(1);

        boolean updated = stepEntryService.setOpenAndAssignedStepEntriesDone(createEmployee(), 1L);
        Assertions.assertTrue(updated);
    }

    private StepEntry createStepEntry(Long id) {
        StepEntry stepEntry = new StepEntry();
        stepEntry.setId(id);
        stepEntry.setCreationDate(LocalDateTime.now());
        stepEntry.setDate(LocalDate.now());
        stepEntry.setProject("Liwest-EMS");
        stepEntry.setState(State.IN_PROGRESS);
        stepEntry.setUpdatedDate(LocalDateTime.now());
        return stepEntry;
    }

    private Employee createEmployee() {
        return Employee.builder()
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .releaseDate(LocalDate.now().toString())
                .build();
    }
}
