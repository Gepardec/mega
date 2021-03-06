package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.project.Project;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class ProjectRepositoryTest {

    @Inject
    ProjectRepository projectRepository;

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setName("LIW-Allgemein");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now());

        projectRepository.persist(project);
    }

    @Test
    void givenFindById_whenFound_thenSuccess() {
        Project p = projectRepository.findById(project.getId());

        Assertions.assertAll(
                () -> assertNotNull(p),
                () -> assertEquals(project.getName(), p.getName())
        );
    }
}