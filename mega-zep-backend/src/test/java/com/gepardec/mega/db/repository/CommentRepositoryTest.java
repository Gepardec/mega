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

        step = new Step();
        step.setName("TestStep");
        step.setRole(Role.EMPLOYEE);
        step.setOrdinal(1);

        stepEntry = new StepEntry();
        stepEntry.setCreationDate(LOCALDATETIME);
        stepEntry.setUpdatedDate(LOCALDATETIME);
        stepEntry.setDate(LocalDate.now());
        stepEntry.setProject(project.getName());
        stepEntry.setState(EmployeeState.IN_PROGRESS);
        stepEntry.setOwner(user);
        stepEntry.setAssignee(user);

        stepEntry.setStep(step);

        comment = new Comment();
        comment.setState(EmployeeState.OPEN);
        comment.setMessage("Test Message");
        comment.setCreationDate(LocalDateTime.of(2021, 1,18, 10, 10));
        comment.setUpdatedDate(LOCALDATETIME);
        comment.setStepEntry(stepEntry);

        stepRepository.persist(step);
        userRepository.persistOrUpdate(user);
        stepEntryRepository.persist(stepEntry);
        projectRepository.persist(project);
        commentRepository.persist(comment);
    }

    @AfterEach
    void tearDown() {
        projectRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void findAllComments_whenNoCommentsExist_thenReturnsEmptyList() {
        projectRepository.deleteAll();
        commentRepository.deleteAll();
        List<Comment> result = commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(comment.getCreationDate().toLocalDate(), LocalDate.now(), EMAIL);
        assertThat(result).isEmpty();
        setup();
    }

    @Test
    void findAllComments_whenCommentExist_thenReturnsListWithCorrectComment() {
        List<Comment> result = commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(comment.getCreationDate().toLocalDate(), LocalDate.now(), EMAIL);
        assertThat(result).hasSize(1);
    }

    @Test
    void setStatusDone_whenCommentExists_thenSetsStatusToDone() {
        int result = commentRepository.setStatusDone(comment.getId());
        assertThat(result).isEqualTo(1);
    }

    @Test
    void setStatusDone_whenCommentExistsAndHasStatusDone_thenSetsStatusDone() {
        int result = commentRepository.setStatusDone(comment.getId());
        assertThat(result).isEqualTo(1);
    }

    @Test
    void save_whenCorrectComment_thenPersistsComment() {
        commentRepository.deleteById(comment.getId());
        Comment result = commentRepository.save(comment);
        assertThat(result).isEqualTo(comment);
    }

    @Test
    void update_whenCorrectCommentAndIsChanged_thenUpdatesComment() {
        Comment result = commentRepository.update(comment);
        assertThat(result).isEqualTo(comment);
    }

    @Test
    void deleteComment_whenCommentIsPersistedAndDeleteCommentIsCalled_thenDeletesComment() {
        boolean result = commentRepository.deleteComment(comment.getId());
        assertThat(result).isTrue();
    }

    @Test
    void deleteComment_whenCommentIsntPersisted_thenReturnsFalse() {
        boolean result = commentRepository.deleteComment(comment.getId());
        assertThat(result).isTrue();
    }

    private void setup() {
        project = new Project();
        project.setName("LIW-Allgemein");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now());

        projectRepository.persist(project);
    }

    private void deleteAll() {
        stepRepository.deleteAll();
        userRepository.deleteAll();
        stepEntryRepository.deleteAll();
        projectRepository.deleteAll();
        commentRepository.deleteAll();
    }
}
