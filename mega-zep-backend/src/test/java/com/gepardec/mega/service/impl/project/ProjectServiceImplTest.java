package com.gepardec.mega.service.impl.project;

import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.ProjectFilter;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.impl.ProjectServiceImpl;
import com.gepardec.mega.zep.ZepService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
class ProjectServiceImplTest {

    @InjectMock
    ZepService zepService;

    @Inject
    ProjectServiceImpl projectService;

    private Project.ProjectBuilder projectFor(final String id) {
        return Project.builder()
                .projectId(id)
                .description(String.format("Description of Project %s", id));
    }

    @Test
    void whenResultIsEmpty_thenReturnEmptyProjectList() {
        // Given
        when(zepService.getProjectsForMonthYear(Mockito.any())).thenReturn(List.of());

        // When
        final List<Project> projectsForMonthYear = projectService.getProjectsForMonthYear(LocalDate.now());

        // Then
        assertThat(projectsForMonthYear).isEmpty();
    }

    @Test
    void whenResult_thenReturnProjectList() {
        // Given
        when(zepService.getProjectsForMonthYear(Mockito.any())).thenReturn(List.of(projectFor("1")
                .leads(List.of())
                .employees(List.of())
                .categories(List.of())
                .startDate(LocalDate.now())
                .build()));

        // When
        final List<Project> projectsForMonthYear = projectService.getProjectsForMonthYear(LocalDate.now());

        // Then
        assertThat(projectsForMonthYear).isNotEmpty();
    }

    @Test
    void whenFilterCustomer_thenReturnProjectListWithCustomerProjects() {
        // Given
        when(zepService.getProjectsForMonthYear(Mockito.any())).thenReturn(List.of(
                projectFor("Intern")
                        .leads(List.of())
                        .employees(List.of())
                        .categories(List.of("INT"))
                        .startDate(LocalDate.now())
                        .build(),
                projectFor("Kunde")
                        .leads(List.of())
                        .employees(List.of())
                        .categories(List.of())
                        .startDate(LocalDate.now())
                        .build()));

        // When
        final List<Project> projectsForMonthYear = projectService.getProjectsForMonthYear(LocalDate.now(), List.of(ProjectFilter.IS_CUSTOMER_PROJECT));

        // Then
        assertThat(projectsForMonthYear).hasSize(1);
        assertThat(projectsForMonthYear.get(0).getProjectId()).isEqualTo("Kunde");
    }

    @Test
    void whenFilterLeads_thenReturnProjectListWithLeads() {
        // Given
        when(zepService.getProjectsForMonthYear(Mockito.any())).thenReturn(List.of(
                projectFor("1")
                        .leads(List.of(User.builder()
                                        .dbId(1)
                                        .userId("userId")
                                        .firstname("Gepard")
                                        .lastname("Gepardec")
                                        .email(String.format("%s%s.%s@gepardec.com", "Gepard", 1, "Gepardec"))
                                        .roles(Set.of(Role.EMPLOYEE)).build())
                                .stream().map(User::getUserId).collect(Collectors.toList()))
                        .employees(List.of())
                        .categories(List.of())
                        .startDate(LocalDate.now())
                        .build(),
                projectFor("2")
                        .leads(List.of())
                        .employees(List.of())
                        .categories(List.of())
                        .startDate(LocalDate.now())
                        .build()));

        // When
        final List<Project> projectsForMonthYear = projectService.getProjectsForMonthYear(LocalDate.now(), List.of(ProjectFilter.IS_LEADS_AVAILABLE));

        // Then
        assertThat(projectsForMonthYear).hasSize(1);
        assertThat(projectsForMonthYear.get(0).getProjectId()).isEqualTo("1");
    }
}
