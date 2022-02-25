package com.gepardec.mega.service.impl.stepentry;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.db.repository.StepEntryRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.ProjectEmployees;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.api.StepEntryService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        Optional<EmployeeState> states = stepEntryService.findEmployeeCheckState(createEmployee());
        assertThat(states).isPresent();
        assertThat(states).get().isEqualTo(EmployeeState.IN_PROGRESS);
    }

    @Test
    void findEmployeeCheckState_whenNoStepEntries_thenEmpty() {
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString())).thenReturn(List.of());

        Optional<EmployeeState> states = stepEntryService.findEmployeeCheckState(createEmployee());
        assertThat(states).isEmpty();
    }

    @Test
    void areOtherChecksDone_whenAllInProgress_thenFalse() {
        StepEntry stepEntry1 = createStepEntry(1L);
        StepEntry stepEntry2 = createStepEntry(2L);

        List<StepEntry> stepEntries = List.of(stepEntry1, stepEntry2);
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString())).thenReturn(stepEntries);

        boolean areOtherChecksDone = stepEntryService.findAllOwnedAndUnassignedStepEntriesForOtherChecks(createEmployee())
                .stream().allMatch(stepEntry -> stepEntry.getState() == EmployeeState.DONE);
        assertThat(areOtherChecksDone).isFalse();
    }

    @Test
    void areOtherChecksDone_whenNoStepEntries_thenTrue() {
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString())).thenReturn(List.of());

        boolean areOtherChecksDone = stepEntryService.findAllOwnedAndUnassignedStepEntriesForOtherChecks(createEmployee())
                .stream().allMatch(stepEntry -> stepEntry.getState() == EmployeeState.DONE);

        assertThat(areOtherChecksDone).isTrue();
    }

    @Test
    void areOtherChecksDone_whenAllDone_thenTrue() {
        StepEntry stepEntry1 = createStepEntry(1L);
        StepEntry stepEntry2 = createStepEntry(2L);

        stepEntry1.setState(EmployeeState.DONE);
        stepEntry2.setState(EmployeeState.DONE);

        List<StepEntry> stepEntries = List.of(stepEntry1, stepEntry2);
        when(stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString())).thenReturn(stepEntries);

        boolean areOtherChecksDone = stepEntryService.findAllOwnedAndUnassignedStepEntriesForOtherChecks(createEmployee())
                .stream().allMatch(stepEntry -> stepEntry.getState() == EmployeeState.DONE);

        assertThat(areOtherChecksDone).isTrue();
    }

    @Test
    void setOpenAndAssignedStepEntriesDone_when0_thenFalse() {
        when(stepEntryRepository.closeAssigned(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(0);

        Employee employee = createEmployee();
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate()));
        boolean updated = stepEntryService.setOpenAndAssignedStepEntriesDone(employee, 0L, from, to);
        assertThat(updated).isFalse();
    }

    @Test
    void setOpenAndAssignedStepEntriesDone_when1_thenTrue() {
        when(stepEntryRepository.closeAssigned(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(1);

        Employee employee = createEmployee();
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate()));

        boolean updated = stepEntryService.setOpenAndAssignedStepEntriesDone(employee, 1L, from, to);

        assertThat(updated).isTrue();
    }

    @Test
    void findAllStepEntriesForEmployee_whenEmployeeIsNull_thenThrowsException() {
        assertThatThrownBy(() -> stepEntryService.findAllStepEntriesForEmployee(null, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Employee must not be null!");
    }

    @Test
    void findAllStepEntriesForEmployee_whenValidStepEntries_thenListOfEntries() {
        when(stepEntryRepository.findAllOwnedStepEntriesInRange(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString()
        )).thenReturn(List.of(createStepEntry(1L)));

        Employee employee = createEmployee();
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate()));
        List<StepEntry> result = stepEntryService.findAllStepEntriesForEmployee(employee, from, to);
        verify(stepEntryRepository, times(1)).findAllOwnedStepEntriesInRange(
                LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate())),
                LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate())),
                employee.getEmail()
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void findStepEntryForEmployeeAtStep_whenEmployeeIsNull_thenThrowsException() {
        assertThatThrownBy(() -> stepEntryService.findStepEntryForEmployeeAtStep(2L, null, "", ""))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Employee must not be null!");
    }

    @Test
    void findStepEntryForEmployeeAtStep_whenNoStepIsFound_thenThrowsException() {
        when(stepEntryRepository.findStepEntryForEmployeeAtStepInRange(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString()
        )).thenReturn(Optional.empty());

        Employee employee = createEmployee();

        assertThatThrownBy(() -> stepEntryService.findStepEntryForEmployeeAtStep(2L, employee, "", employee.getReleaseDate()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No StepEntries found for Employee ");
    }

    @Test
    void findStepEntryForEmployeeAtStep_whenValid_thenReturnStepEntry() {
        when(stepEntryRepository.findStepEntryForEmployeeAtStepInRange(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString()
        )).thenReturn(Optional.of(createStepEntry(1L)));

        Employee employee = createEmployee();
        StepEntry stepEntry = stepEntryService.findStepEntryForEmployeeAtStep(2L, employee, "", employee.getReleaseDate());
        assertThat(stepEntry).isNotNull();
        assertThat(stepEntry.getId()).isEqualTo(1L);
        assertThat(stepEntry.getProject()).isEqualTo("Liwest-EMS");
        assertThat(stepEntry.getState()).isEqualTo(EmployeeState.IN_PROGRESS);
    }

    @Test
    void getProjectEmployeesForPM_whenValid_thenReturnListOfEntries() {
        when(stepEntryRepository.findAllStepEntriesForPMInRange(
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString())
        ).thenReturn(createStepEntriesForPM());

        List<ProjectEmployees> projectEmployees = stepEntryService.getProjectEmployeesForPM(LocalDate.now(), LocalDate.now(), "no-reply@gepardec.com");

        assertAll(
                () -> assertThat(projectEmployees).isNotNull(),
                () -> assertThat(projectEmployees).hasSize(1),
                () -> assertThat(projectEmployees.get(0).projectId()).isEqualTo("Liwest-EMS"),
                () -> assertThat(projectEmployees.get(0).employees()).containsExactlyInAnyOrder("008", "010", "012", "020")
        );
    }

    private List<StepEntry> createStepEntriesForPM() {
        return List.of(
                createStepEntry(1L, "no-reply@gepardec.com", "008"),
                createStepEntry(2L, "no-reply@gepardec.com", "010"),
                createStepEntry(3L, "no-reply@gepardec.com", "012"),
                createStepEntry(4L, "no-reply@gepardec.com", "020")
        );
    }

    private StepEntry createStepEntry(Long id, String ownerEmail, String ownerZepId) {
        StepEntry entry = createStepEntry(id);
        User owner = new User();
        owner.setEmail(ownerEmail);
        owner.setZepId(ownerZepId);
        entry.setOwner(owner);
        return entry;
    }

    private StepEntry createStepEntry(Long id) {
        StepEntry stepEntry = new StepEntry();
        stepEntry.setId(id);
        stepEntry.setCreationDate(LocalDateTime.now());
        stepEntry.setDate(LocalDate.now());
        stepEntry.setProject("Liwest-EMS");
        stepEntry.setState(EmployeeState.IN_PROGRESS);
        stepEntry.setUpdatedDate(LocalDateTime.now());
        return stepEntry;
    }

    private Employee createEmployee() {
        return Employee.builder()
                .userId("1")
                .email("max.mustermann@gpeardec.com")
                .releaseDate(LocalDate.now().toString())
                .build();
    }
}
