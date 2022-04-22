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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class CommentRepositoryTest {

    private static final String EMAIL = "max.muster@gepardec.com";

    private final LocalDateTime LOCALDATETIME = LocalDateTime.now();

    @Inject
    CommentRepository commentRepository;

    @Inject
    StepEntryRepository stepEntryRepository;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    StepRepository stepRepository;

    private Comment comment;

    private Project project;

    private StepEntry stepEntry;

    private Step step;

    private User user;

    @BeforeEach
    void init() {
        initializeSetupObjects();
    }

    private void persistEntities() {
        stepRepository.persist(step);
        userRepository.persistOrUpdate(user);
        stepEntryRepository.persist(stepEntry);
        projectRepository.persist(project);
        commentRepository.persist(comment);
    }

    @AfterEach
    void tearDown() {
        tearDownCommentEntity();
        assertThat(tearDownProjectEntity()).isTrue();
        assertThat(tearDownStepEntryEntity()).isTrue();
        assertThat(tearDownUserEntity()).isTrue();
        assertThat(tearDownStepEntity()).isTrue();
    }

    @Test
    void findAllComments_whenNoCommentsExist_thenReturnsEmptyList() {
        persistEntities();
        commentRepository.deleteAll();
        assertThat(commentRepository.findAll().list()).isEmpty();

        List<Comment> result = commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(comment.getCreationDate().toLocalDate(), LocalDate.now(), EMAIL);

        assertThat(result).isEmpty();
    }

    @Test
    void findAllComments_whenCommentExist_thenReturnsListWithCorrectComment() {
        persistEntities();

        List<Comment> result = commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(comment.getCreationDate().toLocalDate(), LocalDate.now(), EMAIL);

        assertThat(result).hasSize(1);
    }

    @Test
    void setStatusDone_whenCommentExists_thenSetsStatusToDone() {
        persistEntities();

        int result = commentRepository.setStatusDone(comment.getId());

        assertThat(result).isEqualTo(1);
    }

    @Test
    void setStatusDone_whenCommentExistsAndHasStatusDone_thenSetsStatusDone() {
        persistEntities();

        int result = commentRepository.setStatusDone(comment.getId());

        assertThat(result).isEqualTo(1);
    }

    @Test
    void update_whenCorrectCommentAndIsChanged_thenUpdatesComment() {
        persistEntities();

        Comment result = commentRepository.update(comment);

        assertThat(result).isEqualTo(comment);
    }

    @Test
    void deleteComment_whenCommentIsPersistedAndDeleteCommentIsCalled_thenDeletesComment() {
        persistEntities();

        boolean result = commentRepository.deleteComment(comment.getId());

        assertThat(result).isTrue();
    }

    @Test
    void deleteComment_whenCommentIsntPersisted_thenReturnsFalse() {
        persistEntities();

        boolean result = commentRepository.deleteComment(comment.getId());

        assertThat(result).isTrue();
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

    private StepEntry initializeStepEntryObject() {
        StepEntry initStepEntry = new StepEntry();
        initStepEntry.setCreationDate(LOCALDATETIME);
        initStepEntry.setUpdatedDate(LOCALDATETIME);
        initStepEntry.setDate(LocalDate.now());
        initStepEntry.setProject(project.getName());
        initStepEntry.setState(EmployeeState.IN_PROGRESS);
        initStepEntry.setOwner(user);
        initStepEntry.setAssignee(user);
        initStepEntry.setStep(step);

        return initStepEntry;
    }

    private Comment initializeCommentObject() {
        Comment initComment = new Comment();
        initComment.setState(EmployeeState.OPEN);
        initComment.setMessage("Test Message");
        initComment.setCreationDate(LocalDateTime.of(2021, 1, 18, 10, 10));
        initComment.setUpdatedDate(LOCALDATETIME);
        initComment.setStepEntry(stepEntry);

        return initComment;
    }

    private Step initializeStepObject() {
        Step initStep = new Step();
        initStep.setName("TestStep");
        initStep.setRole(Role.EMPLOYEE);
        initStep.setOrdinal(1);

        return initStep;
    }

    private void initializeSetupObjects() {
        project = initializeProjectObject();
        user = initializeUserObject();
        step = initializeStepObject();
        stepEntry = initializeStepEntryObject();
        comment = initializeCommentObject();
    }

    private void tearDownCommentEntity() {
        commentRepository.deleteAll();
    }

    private boolean tearDownProjectEntity() {
        return projectRepository.deleteById(project.getId());
    }

    private boolean tearDownStepEntryEntity() {
        return stepEntryRepository.deleteById(stepEntry.getId());
    }

    private boolean tearDownUserEntity() {
        return userRepository.deleteById(user.getId());
    }

    private boolean tearDownStepEntity() {
        return stepRepository.deleteById(step.getId());
    }
}
