package com.gepardec.mega.db.repository;

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
    private User user;
    private Step step;
    private Project project;

    private final LocalDateTime localDateTime = LocalDateTime.now();

    @BeforeEach
    void init() {
        initializeSetupObjects();
    }

    @AfterEach
    void tearDown() {
        assertThat(tearDownStepEntryEntity()).isTrue();
        assertThat(tearDownStepEntity()).isTrue();
        assertThat(tearDownUserEntity()).isTrue();
    }

    private void persistEntities() {
        stepRepository.persist(step);
        userRepository.persistOrUpdate(user);
        stepEntryRepository.persist(stepEntry);
    }

    @Test
    void findAllOwnedAndAssignedStepEntriesForEmployee_whenNoStepEntriesExist_thenReturnsEmptyObject() {
        Optional<StepEntry> result = stepEntryRepository.findAllOwnedAndAssignedStepEntriesForEmployee(localDateTime.toLocalDate(), EMAIL);
        assertThat(result).isEmpty();
        persistEntities();
    }

    @Test
    void findAllOwnedAndUnassignedStepEntriesForOtherChecks_whenMethodIsCalledAndNoUnassignedStepEntriesExist_thenReturnsEmptyList() {
        persistEntities();
        List<StepEntry> result = stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(localDateTime.toLocalDate(), EMAIL);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllOwnedAndUnassignedStepEntriesForPMProgress_whenMethodIsCalledWithCorrectParameters_thenReturnsEmptyList() {
        persistEntities();
        List<StepEntry> result = stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForPMProgress(localDateTime.toLocalDate(), EMAIL);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllOwnedAndUnassignedStepEntriesForPMProgress_whenMethodIsCalledWithNullLocalDate_thenReturnsEmptyList() {
        persistEntities();
        List<StepEntry> result = stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForPMProgress(null, EMAIL);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllOwnedAndUnassignedStepEntriesForPMProgress_whenMethodIsCalledWithNullEmail_thenReturnsEmptyList() {
        persistEntities();
        List<StepEntry> result = stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForPMProgress(localDateTime.toLocalDate(), null);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllOwnedStepEntriesInRange_whenMethodIsCalledWithCorrectParameters_thenReturnsAllEntriesInRange() {
        persistEntities();
        List<StepEntry> result = stepEntryRepository.findAllOwnedStepEntriesInRange(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL);
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(stepEntry)
        );
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithCorrectParameters_thenClosesAssignedAndReturnsOne() {
        persistEntities();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, stepEntry.getStep().getId());
        assertAll(
            () -> assertThat(result).isEqualTo(1)
        );
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithEmailWithNullValue_thenReturnsZeroIntegerAndDoesntCloseAssigned() {
        persistEntities();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), null, stepEntry.getStep().getId());
        assertThat(result).isZero();
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithCorrectParameters_thenClosesAssignedAndReturnsIntegerOne() {
        persistEntities();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, EMAIL, stepEntry.getStep().getId(), project.getName());
        assertThat(result).isEqualTo(1);
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithEmptyProjectParameter_thenReturnsIntegerOne() {
        persistEntities();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, EMAIL, stepEntry.getStep().getId(), "");
        assertThat(result).isZero();
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithEmptyAssigneParameter_thenReturnsIntegerOne() {
        persistEntities();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, "", stepEntry.getStep().getId(), project.getName());
        assertThat(result).isZero();
    }

    @Test
    void closeAssigned_whenMethodIsCalledWithEmptyOwnerParameter_thenReturnsIntegerOne() {
        persistEntities();
        int result = stepEntryRepository.closeAssigned(localDateTime.toLocalDate(), localDateTime.toLocalDate(), "", EMAIL, stepEntry.getStep().getId(), project.getName());
        assertThat(result).isZero();
    }

    @Test
    void findStepEntryForEmployeeAtStepInRange_whenMethodIsCalledWithCorrectParameters_thenReturnsOptionalOfStepEntry() {
        persistEntities();
        Optional<StepEntry> result = stepEntryRepository.findStepEntryForEmployeeAtStepInRange(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, stepEntry.getStep().getId(), EMAIL);
        assertAll(
            () -> assertThat(result).isPresent(),
            () -> assertThat(result).contains(stepEntry)
        );
    }

    @Test
    void findStepEntryForEmployeeAndProjectAtStepInRange_whenMethodIsCalledWithCorrectParameters_thenReturnsOptionalOfStepEntry() {
        persistEntities();
        Optional<StepEntry> result = stepEntryRepository.findStepEntryForEmployeeAndProjectAtStepInRange(localDateTime.toLocalDate(), localDateTime.toLocalDate(), EMAIL, stepEntry.getStep().getId(), EMAIL, project.getName());
        assertAll(
            () -> assertThat(result).isPresent(),
            () -> assertThat(result).contains(stepEntry)
        );
    }

    private Project initializeProjectObject() {
        Project initProject = new Project();
        initProject.setName("LIW-Allgemein");
        initProject.setStartDate(LocalDate.now());
        initProject.setEndDate(LocalDate.now());

        return initProject;
    }

    private User initializeUserObject() {
        User initUser = new User();
        initUser.setActive(true);
        initUser.setEmail(EMAIL);
        initUser.setFirstname("Max");
        initUser.setLastname("Mustermann");
        initUser.setLocale(Locale.GERMAN);
        initUser.setZepId("026-mmuster");
        initUser.setRoles(Set.of(Role.EMPLOYEE, Role.OFFICE_MANAGEMENT));
        initUser.setCreationDate(LocalDateTime.of(2021, 1, 18, 10, 10));
        initUser.setUpdatedDate(LocalDateTime.now());

        return initUser;
    }

    private Step initializeStepObject() {
        Step initStep = new Step();
        initStep.setName("TestStep");
        initStep.setRole(Role.EMPLOYEE);
        initStep.setOrdinal(1);

        return initStep;
    }

    private StepEntry initializeStepEntryObject() {
        StepEntry initStepEntry = new StepEntry();
        initStepEntry.setCreationDate(localDateTime);
        initStepEntry.setUpdatedDate(localDateTime);
        initStepEntry.setDate(LocalDate.now());
        initStepEntry.setProject(project.getName());
        initStepEntry.setState(EmployeeState.IN_PROGRESS);
        initStepEntry.setOwner(user);
        initStepEntry.setAssignee(user);
        initStepEntry.setStep(step);

        return initStepEntry;
    }

    private boolean tearDownStepEntryEntity() {
        return stepEntryRepository.deleteById(stepEntry.getId());
    }

    private boolean tearDownStepEntity() {
        return stepRepository.deleteById(step.getId());
    }

    private boolean tearDownUserEntity() {
        return userRepository.deleteById(user.getId());
    }

    private void initializeSetupObjects() {
        project = initializeProjectObject();
        user = initializeUserObject();
        step = initializeStepObject();
        stepEntry = initializeStepEntryObject();
    }
}
