package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.project.Project;
import com.gepardec.mega.db.entity.project.ProjectComment;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
@TestTransaction
class ProjectCommentRepositoryTest {

    private static final String COMMENT = "comment";

    private static final String NEW_COMMENT = "new comment";

    private ProjectComment projectComment;

    @Inject
    ProjectCommentRepository projectCommentRepository;

    @Inject
    ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        projectComment = initializeProjectCommentObject();
    }

    @Test
    void findByProjectNameAndDateBetween() {
        projectRepository.persist(projectComment.getProject());
        projectCommentRepository.save(projectComment);

        List<ProjectComment> projectComments = projectCommentRepository.findByProjectNameAndDateBetween(projectComment.getProject().getName(), projectComment.getDate().minusDays(2), projectComment.getDate().plusDays(2));

        assertAll(
                () -> assertThat(projectComments.isEmpty()).isFalse(),
                () -> assertThat(projectComments.get(0).getDate()).isEqualTo(projectComment.getDate())
        );
    }

    @Test
    void findByProjectNameWithDate() {
        projectRepository.persist(projectComment.getProject());
        projectCommentRepository.save(projectComment);

        List<ProjectComment> projectComments = projectCommentRepository.findByProjectNameWithDate(projectComment.getProject().getName(), projectComment.getDate());

        assertAll(
                () -> assertThat(projectComments.isEmpty()).isFalse(),
                () -> assertThat(projectComments.get(0).getDate()).isEqualTo(projectComment.getDate())
        );
    }

    @Test
    void update() {
        projectRepository.persist(projectComment.getProject());
        projectCommentRepository.save(projectComment);

        List<ProjectComment> projectComments = projectCommentRepository.findByProjectNameAndDateBetween(projectComment.getProject().getName(), LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));
        projectComments.get(0).setComment(NEW_COMMENT);
        projectCommentRepository.update(projectComments.get(0));

        List<ProjectComment> newProjectComments = projectCommentRepository.findByProjectNameAndDateBetween(projectComment.getProject().getName(), LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));

        assertAll(
                () -> assertThat(newProjectComments.isEmpty()).isFalse(),
                () -> assertThat(newProjectComments.get(0).getComment()).isEqualTo(NEW_COMMENT)
        );
    }

    @Test
    void save() {
        projectRepository.persist(projectComment.getProject());
        projectCommentRepository.save(projectComment);

        List<ProjectComment> projectComments = projectCommentRepository.findByProjectNameAndDateBetween(projectComment.getProject().getName(), LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));
        assertThat(projectComments.isEmpty()).isFalse();
    }

    private Project initializeProjectObject() {
        Project initProject = new Project();
        initProject.setName("LIW-Allgemein");
        initProject.setStartDate(LocalDate.now());
        initProject.setEndDate(LocalDate.now());

        return initProject;
    }

    private ProjectComment initializeProjectCommentObject() {
        ProjectComment projectComment = new ProjectComment();
        projectComment.setComment(COMMENT);
        projectComment.setProject(initializeProjectObject());
        projectComment.setCreationDate(LocalDateTime.now());
        projectComment.setUpdatedDate(LocalDateTime.now());
        projectComment.setDate(LocalDate.now());

        return projectComment;
    }
}