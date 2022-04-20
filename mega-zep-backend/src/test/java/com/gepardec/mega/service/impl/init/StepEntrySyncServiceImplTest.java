package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.domain.model.StepEntry;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.ProjectService;
import com.gepardec.mega.service.api.StepEntryService;
import com.gepardec.mega.service.api.StepService;
import com.gepardec.mega.service.api.UserService;
import com.gepardec.mega.service.impl.StepEntrySyncServiceImpl;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class StepEntrySyncServiceImplTest {

    @InjectMock
    UserService userService;

    @InjectMock
    ProjectService projectService;

    @InjectMock
    StepService stepService;

    @InjectMock
    StepEntryService stepEntryService;

    @InjectMock
    NotificationConfig notificationConfig;

    @Inject
    Logger logger;

    @Inject
    StepEntrySyncServiceImpl stepEntrySyncService;

    @BeforeEach
    void setUp() {
        when(userService.findActiveUsers()).thenReturn(List.of(
                userForProjectLead(1),
                userForProjectLead(2),
                userForOm(3),
                userForEmployee(4),
                userForEmployee(5),
                userForEmployee(6)
        ));
        when(projectService.getProjectsForMonthYear(Mockito.any(), Mockito.anyList())).thenReturn(List.of(
                projectFor(1)
                        .leads(
                                List.of(userForProjectLead(1))
                                        .stream().map(User::getUserId).collect(Collectors.toList()))
                        .employees(
                                List.of(userForProjectLead(1),
                                                userForEmployee(4),
                                                userForEmployee(5),
                                                userForEmployee(6))
                                        .stream().map(User::getUserId).collect(Collectors.toList()))
                        .startDate(LocalDate.now())
                        .build(),
                projectFor(2)
                        .leads(
                                List.of(userForProjectLead(2))
                                        .stream().map(User::getUserId).collect(Collectors.toList()))
                        .employees(
                                List.of(userForProjectLead(2),
                                                userForEmployee(5),
                                                userForEmployee(6))
                                        .stream().map(User::getUserId).collect(Collectors.toList()))
                        .startDate(LocalDate.now())
                        .build(),
                projectFor(3)
                        .leads(
                                List.of(userForProjectLead(1))
                                        .stream().map(User::getUserId).collect(Collectors.toList()))
                        .employees(
                                List.of(userForProjectLead(1),
                                                userForEmployee(5),
                                                userForEmployee(6))
                                        .stream().map(User::getUserId).collect(Collectors.toList()))
                        .startDate(LocalDate.now())
                        .build()
        ));
        when(stepService.getSteps()).thenReturn(
                List.of(stepFor(1, "CONTROL_TIMES", Role.EMPLOYEE).build(),
                        stepFor(2, "CONTROL_INTERNAL_TIMES", Role.OFFICE_MANAGEMENT).build(),
                        stepFor(3, "CONTROL_EXTERNAL_TIMES", Role.OFFICE_MANAGEMENT).build(),
                        stepFor(4, "CONTROL_TIME_EVIDENCES", Role.PROJECT_LEAD).build(),
                        stepFor(5, "ACCEPT_TIMES", Role.OFFICE_MANAGEMENT).build()
                ));
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of(userForOm(3).getEmail()));
    }

    private User userForProjectLead(final int id) {
        return userFor(id, "ProjectLeadGepard", "Gepardec")
                .build();
    }

    private User userForOm(final int id) {
        return userFor(id, "OfficeManagmentGepardin", "Gepardec")
                .build();
    }

    private User userForEmployee(final int id) {
        return userFor(id, "Gepard", "Gepardec")
                .build();
    }

    private User.UserBuilder userFor(final int id, final String firstname, final String lastname) {
        return User.builder()
                .dbId(id)
                .userId(id + "-userId")
                .firstname(firstname + id)
                .lastname(lastname)
                .roles(Set.of(Role.EMPLOYEE))
                .email(String.format("%s%s.%s@gepardec.com", firstname, id, lastname));
    }

    private Project.ProjectBuilder projectFor(final int id) {
        return Project.builder()
                .projectId(String.valueOf(id))
                .description(String.format("Description of Project %s", id))
                .categories(List.of());
    }

    private Step.StepBuilder stepFor(final int id, final String name, final Role role) {
        return Step.builder()
                .dbId(id)
                .ordinal(id)
                .name(name)
                .role(role);
    }

    @Test
    void whenAllDataIsProvided_thenInsertDefinedNumberOfItems() {
        // Given
        // default setup

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        verify(stepEntryService, times(34)).addStepEntry(Mockito.any());
    }

    @Test
    void whenNoProjectsProvided_thenDoNotInsertProjectLead() {
        // Given
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        when(projectService.getProjectsForMonthYear(Mockito.any(), Mockito.anyList())).thenReturn(List.of());

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
        verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
        final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

        // Project Lead 1
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 1).count()).isEqualTo(1);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 1).count()).isEqualTo(1);

        // Project Lead 2
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 2).count()).isEqualTo(1);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 2).count()).isEqualTo(1);

        // Steps
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_TIME_EVIDENCES")).count()).isZero();
    }

    @Test
    void whenProjectLeadIsInactive_thenDoNotInsertProjectLead() {
        // Given
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        when(projectService.getProjectsForMonthYear(Mockito.any(), Mockito.anyList())).thenReturn(List.of(projectFor(1)
                .leads(
                        List.of(userForProjectLead(7))
                                .stream().map(User::getUserId).collect(Collectors.toList()))
                .employees(
                        List.of(userForProjectLead(1),
                                        userForEmployee(4),
                                        userForEmployee(5),
                                        userForEmployee(6))
                                .stream().map(User::getUserId).collect(Collectors.toList()))
                .startDate(LocalDate.now())
                .build()));

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
        verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
        final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

        // Project Lead 1
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 1).count()).isEqualTo(1);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 1).count()).isEqualTo(1);

        // Project Lead 2
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 2).count()).isEqualTo(1);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 2).count()).isEqualTo(1);

        // Steps
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_TIME_EVIDENCES")).count()).isZero();
    }

    @Test
    void whenDefaultDataIsProvided_thenInsertProjectLead() {
        // Given
        // default setup
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
        verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
        final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

        // Project Lead 1
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 1).count()).isEqualTo(3);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 1).count()).isEqualTo(8);

        // Project Lead 2
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 2).count()).isEqualTo(2);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 2).count()).isEqualTo(4);

        // Steps
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_TIME_EVIDENCES")).count()).isEqualTo(10);
    }

    @Test
    void whenOfficeManagmentIsInactive_thenDoNotInsertOfficeManagment() {
        // Given
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of("some.user@gepardec.com"));

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
        verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
        final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

        // Office Managment 3
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 3).count()).isEqualTo(1);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 3).count()).isEqualTo(1);

        // Steps
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_INTERNAL_TIMES")).count()).isZero();
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_EXTERNAL_TIMES")).count()).isZero();
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("ACCEPT_TIMES")).count()).isEqualTo(0);
    }

    @Test
    void whenNoOfficeManagmentIsProvided_thenDoNotInsertOfficeManagment() {
        // Given
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
        verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
        final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

        // Office Managment 3
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 3).count()).isEqualTo(1);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 3).count()).isEqualTo(1);

        // Steps
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_INTERNAL_TIMES")).count()).isZero();
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_EXTERNAL_TIMES")).count()).isZero();
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("ACCEPT_TIMES")).count()).isZero();
    }

    @Test
    void whenDefaultDataIsProvided_thenInsertOfficeManagment() {
        // Given
        // default setup

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
        verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
        final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

        // Office Managment 3
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 3).count()).isEqualTo(4);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 3).count()).isEqualTo(19);

        // Steps
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_INTERNAL_TIMES")).count()).isEqualTo(6);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_EXTERNAL_TIMES")).count()).isEqualTo(6);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("ACCEPT_TIMES")).count()).isEqualTo(6);
    }

    @Test
    void whenNoActiveUsers_thenDoNotInsertEmployees() {
        // Given
        when(userService.findActiveUsers()).thenReturn(List.of());

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        verify(stepEntryService, never()).addStepEntry(Mockito.any());
    }

    @Test
    void whenDefaultDataIsProvided_thenInsertEmployees() {
        // Given
        // default setup

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
        verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
        final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

        // Employee 4
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 4).count()).isEqualTo(5);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 4).count()).isEqualTo(1);

        // Employee 5
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 5).count()).isEqualTo(1);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 5).count()).isEqualTo(7);

        // Employee 6
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getOwner().getDbId() == 6).count()).isEqualTo(7);
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getAssignee().getDbId() == 6).count()).isEqualTo(1);

        // Steps
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_TIMES")).count()).isEqualTo(6);
    }

    @Test
    void whenNoStepsProvided_thenDoNotInsertSteps() {
        // Given
        when(stepService.getSteps()).thenReturn(List.of());

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        verify(stepEntryService, never()).addStepEntry(Mockito.any());
    }

    @Test
    void whenDefaultDataIsProvided_thenInsertSteps() {
        // Given
        // default setup

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
        verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
        final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

        // Project 1
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getProject() != null && stepEntry.getProject().getProjectId().equals(String.valueOf(1))).count()).isEqualTo(4);

        // Project 2
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getProject() != null && stepEntry.getProject().getProjectId().equals(String.valueOf(2))).count()).isEqualTo(3);

        // Project 3
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getProject() != null && stepEntry.getProject().getProjectId().equals(String.valueOf(3))).count()).isEqualTo(3);
    }

    @Test
    void whenDefaultDataIsProvided_thenInsertSteps2() {
        // Given
        // default setup

        // When
        stepEntrySyncService.generateStepEntriesFromEndpoint();

        // Then
        final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
        verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
        final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

        // Step 1
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_TIMES")).count()).isEqualTo(6);

        // Step 2
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_INTERNAL_TIMES")).count()).isEqualTo(6);

        // Step 3
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_EXTERNAL_TIMES")).count()).isEqualTo(6);

        // Step 4
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("CONTROL_TIME_EVIDENCES")).count()).isEqualTo(10);

        // Step 5
        assertThat(stepEntries.stream().filter(stepEntry -> stepEntry.getStep().getName().equals("ACCEPT_TIMES")).count()).isEqualTo(6);
    }
}
