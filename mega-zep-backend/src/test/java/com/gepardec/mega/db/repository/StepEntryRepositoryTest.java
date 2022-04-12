package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.employee.Comment;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.Step;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.db.entity.project.Project;
import com.gepardec.mega.domain.model.Role;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
class StepEntryRepositoryTest {
    private static final String EMAIL = "max.muster@gepardec.com";

    @Inject
    UserRepository userRepository;

    @Inject
    StepRepository stepRepository;

    @Inject
    StepEntryRepository stepEntryRepository;

    private StepEntry stepEntry;
    private Comment comment;
    private User user;
    private Step step;
    private Project project;

    private LocalDateTime localDateTime = LocalDateTime.now();

    @BeforeEach
    void init() {
        comment = new Comment();
        comment.setState(EmployeeState.OPEN);
        comment.setMessage("Test Message");
        comment.setCreationDate(LocalDateTime.of(2021, 1,18, 10, 10));
        comment.setUpdatedDate(localDateTime);

        project = new Project();
        project.setName("LIW-Allgemein");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now());

        user = new User();
        user.setActive(true);
        user.setEmail(EMAIL);
        user.setFirstname("Max");
        user.setLastname("Mustermann");
        user.setLocale(Locale.GERMAN);
        user.setZepId("026-mmuster");
        user.setRoles(Set.of(Role.EMPLOYEE, Role.OFFICE_MANAGEMENT));
        user.setCreationDate(LocalDateTime.of(2021, 1,18, 10, 10));
        user.setUpdatedDate(LocalDateTime.now());

        stepEntry = new StepEntry();
        stepEntry.setCreationDate(localDateTime);
        stepEntry.setUpdatedDate(localDateTime);
        stepEntry.setDate(LocalDate.now());
        stepEntry.setProject(project.getName());
        stepEntry.setState(EmployeeState.IN_PROGRESS);
        stepEntry.setOwner(user);
        stepEntry.setAssignee(user);
        comment.setStepEntry(stepEntry);

        step = new Step();
        step.setName("TestStep");
        step.setRole(Role.EMPLOYEE);
        step.setOrdinal(1);

        stepEntry.setStep(step);
    }

    @AfterEach
    void tearDown() {
        boolean stepEntryDeleted = stepEntryRepository.deleteById(stepEntry.getId());
        assertThat(stepEntryDeleted).isTrue();
        boolean resultStep = stepRepository.deleteById(step.getId());

        boolean resultUser = userRepository.deleteById(user.getId());
    }

    @Test
    void findAllOwnedAndAssignedStepEntriesForEmployee_whenCalled_thenReturnsResultContainingCorrectStepEntry() {
        createStepEntry();
        Optional<StepEntry> result = stepEntryRepository.findAllOwnedAndAssignedStepEntriesForEmployee(localDateTime.toLocalDate(), EMAIL);
        assertThat(result).contains(stepEntry);
    }

    @Test
    void findAllOwnedAndAssignedStepEntriesForEmployee_whenNoStepEntriesExist_thenReturnsEmptyObject() {
        Optional<StepEntry> result = stepEntryRepository.findAllOwnedAndAssignedStepEntriesForEmployee(localDateTime.toLocalDate(), EMAIL);
        assertThat(result).isEmpty();
        createStepEntry();
    }

    @Test
    void findAllOwnedAndUnassignedStepEntriesForOtherChecks_whenMethodIsCalledAndNoUnassignedStepEntriesExist_thenReturnsEmptyList() {
        createStepEntry();
        List<StepEntry> result = stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(localDateTime.toLocalDate(), EMAIL);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllOwnedAndUnassignedStepEntriesForPMProgress_whenMethodIsCalledWithCorrectParameters_thenReturnsEmptyList() {
        createStepEntry();
        List<StepEntry> result = stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForPMProgress(localDateTime.toLocalDate(), EMAIL);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllOwnedAndUnassignedStepEntriesForPMProgress_whenMethodIsCalledWithNullLocalDate_thenReturnsEmptyList() {
        createStepEntry();
        List<StepEntry> result = stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForPMProgress(null, EMAIL);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllOwnedAndUnassignedStepEntriesForPMProgress_whenMethodIsCalledWithNullEmail_thenReturnsEmptyList() {
        createStepEntry();
        List<StepEntry> result = stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForPMProgress(localDateTime.toLocalDate(), null);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllOwnedStepEntriesInRange_whenMethodIsCalledWithCorrectParameters_thenReturnsAllEntriesInRange() {
        createStepEntry();
        List<StepEntry> result = stepEntryRepository.findAllOwnedStepEntriesInRange(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL);
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).contains(stepEntry)
        );
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithCorrectParameters_thenClosesAssignedAndReturnsOne() {
        createStepEntry();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, stepEntry.getStep().getId());
        assertAll(
                () -> assertThat(result).isEqualTo(1)
        );
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithEmailWithNullValue_thenReturnsZeroIntegerAndDoesntCloseAssigned() {
        createStepEntry();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), null, stepEntry.getStep().getId());
        assertThat(result).isZero();
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithCorrectParameters_thenClosesAssignedAndReturnsIntegerOne() {
        createStepEntry();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, EMAIL, stepEntry.getStep().getId(), project.getName());
        assertThat(result).isEqualTo(1);
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithEmptyProjectParameter_thenReturnsIntegerOne() {
        createStepEntry();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, EMAIL, stepEntry.getStep().getId(), "");
        assertThat(result).isZero();
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithEmptyAssigneParameter_thenReturnsIntegerOne() {
        createStepEntry();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, "", stepEntry.getStep().getId(), project.getName());
        assertThat(result).isZero();
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithEmptyOwnerParameter_thenReturnsIntegerOne() {
        createStepEntry();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), "", EMAIL, stepEntry.getStep().getId(), project.getName());
        assertThat(result).isZero();
    }

    @Test
    void findStepEntryForEmployeeAtStepInRange_whenMethodIsCalledWithCorrectParameters_thenReturnsOptionalOfStepEntry() {
        createStepEntry();
        Optional<StepEntry> result = stepEntryRepository.findStepEntryForEmployeeAtStepInRange(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, stepEntry.getStep().getId(), EMAIL);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result).contains(stepEntry)
        );
    }

    @Test
    void findStepEntryForEmployeeAndProjectAtStepInRange_whenMethodIsCalledWithCorrectParameters_thenReturnsOptionalOfStepEntry() {
        createStepEntry();
        Optional<StepEntry> result = stepEntryRepository.findStepEntryForEmployeeAndProjectAtStepInRange(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, stepEntry.getStep().getId(), EMAIL, project.getName());
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result).contains(stepEntry)
        );
    }

    private void createStepEntry() {
        stepRepository.persist(step);
        userRepository.persistOrUpdate(user);
        stepEntryRepository.persist(stepEntry);
    }

    private void createStepEntryWithPM() {
        stepRepository.persist(step);
        step.setRole(Role.OFFICE_MANAGEMENT);
        userRepository.persistOrUpdate(user);
        stepEntryRepository.persist(stepEntry);
    }
}
