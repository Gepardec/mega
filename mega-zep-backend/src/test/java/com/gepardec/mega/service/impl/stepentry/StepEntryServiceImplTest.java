package com.gepardec.mega.service.impl.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.db.repository.StepEntryRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class StepEntryServiceImplTest {

    @Inject
    StepEntryService stepEntryService;

    @InjectMock
    private StepEntryRepository stepEntryRepository;

    @Test
    void findEmployeeCheckState_whenValidStepEntries_thenValidState() {
        StepEntry stepEntry = createStepEntry(1L);

        Optional<StepEntry> stepEntries = Optional.of(stepEntry);
        when(stepEntryRepository.findAllOwnedAndAssignedStepEntriesForEmployee(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString())).thenReturn(stepEntries);

        Optional<State> states = stepEntryService.findEmployeeCheckState(createEmployee());
        assertTrue(states.isPresent());
        Assertions.assertEquals(State.IN_PROGRESS, states.get());
    }

    @Test
    void findEmployeeCheckState_whenNoStepEntries_thenEmpty() {
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString())).thenReturn(List.of());

        Optional<State> states = stepEntryService.findEmployeeCheckState(createEmployee());
        assertTrue(states.isEmpty());
    }

    @Test
    void areOtherChecksDone_whenAllInProgress_thenFalse() {
        StepEntry stepEntry1 = createStepEntry(1L);
        StepEntry stepEntry2 = createStepEntry(2L);

        List<StepEntry> stepEntries = List.of(stepEntry1, stepEntry2);
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString())).thenReturn(stepEntries);

        boolean areOtherChecksDone = stepEntryService.areOtherChecksDone(createEmployee());
        Assertions.assertFalse(areOtherChecksDone);
    }

    @Test
    void areOtherChecksDone_whenNoStepEntries_thenTrue() {
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString())).thenReturn(List.of());

        boolean areOtherChecksDone = stepEntryService.areOtherChecksDone(createEmployee());
        assertTrue(areOtherChecksDone);
    }

    @Test
    void areOtherChecksDone_whenAllDone_thenTrue() {
        StepEntry stepEntry1 = createStepEntry(1L);
        StepEntry stepEntry2 = createStepEntry(2L);

        stepEntry1.setState(State.DONE);
        stepEntry2.setState(State.DONE);

        List<StepEntry> stepEntries = List.of(stepEntry1, stepEntry2);
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString())).thenReturn(stepEntries);

        boolean areOtherChecksDone = stepEntryService.areOtherChecksDone(createEmployee());
        assertTrue(areOtherChecksDone);
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
        assertTrue(updated);
    }

    @Test
    void findAllStepEntriesForEmployee_whenEmployeeIsNull_thenThrowsException() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> stepEntryService.findAllStepEntriesForEmployee(null),
                "Expected NullpointerException was not thrown!"
        );

        assertEquals("Employee must not be null!", thrown.getMessage());
    }

    @Test
    void findAllStepEntriesForEmployee_whenValidStepEntries_thenListOfEntries() {
        when(stepEntryRepository.findAllOwnedStepEntriesInRange(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString()
        )).thenReturn(List.of(createStepEntry(1L)));

        Employee empl = createEmployee();
        List<StepEntry> result = stepEntryService.findAllStepEntriesForEmployee(empl);
        verify(stepEntryRepository, times(1)).findAllOwnedStepEntriesInRange(
                LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(empl.releaseDate())),
                LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(empl.releaseDate())),
                empl.email()
        );

        Assertions.assertEquals(1L, result.size());
        StepEntry entry = result.get(0);
        Assertions.assertEquals(1L, entry.getId());
    }

    @Test
    void findStepEntryForEmployeeAtStep_whenEmployeeIsNull_thenThrowsException() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> stepEntryService.findStepEntryForEmployeeAtStep(2L, null),
                "Expected NullpointerException was not thrown!"
        );

        assertEquals("Employee must not be null!", thrown.getMessage());
    }

    @Test
    void findStepEntryForEmployeeAtStep_whenNoStepIsFound_thenThrowsException() {
        when(stepEntryRepository.findStepEntryForEmployeeAtStepInRange(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong()
        )).thenReturn(Optional.empty());

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> stepEntryService.findStepEntryForEmployeeAtStep(2L, createEmployee()),
                "Expected IllegalStateException was not thrown!"
        );

        assertTrue(thrown.getMessage().contains("No StepEntries found for Employee "));
    }

    @Test
    void findStepEntryForEmployeeAtStep_whenValid_thenReturnStepEntry() {
        when(stepEntryRepository.findStepEntryForEmployeeAtStepInRange(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong()
        )).thenReturn(Optional.of(createStepEntry(1L)));

        StepEntry stepEntry = stepEntryService.findStepEntryForEmployeeAtStep(2L, createEmployee());
        assertNotNull(stepEntry);
        assertEquals(1L, stepEntry.getId());
        assertEquals("Liwest-EMS", stepEntry.getProject());
        assertEquals(State.IN_PROGRESS, stepEntry.getState());
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
