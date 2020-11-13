package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.domain.Role;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.domain.model.StepEntry;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.service.api.step.StepService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.service.api.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StepEntrySyncServiceImplTest {

    @Mock
    private Logger logger;

    @Mock
    private UserService userService;

    @Mock
    private ProjectService projectService;

    @Mock
    private StepService stepService;

    @Mock
    private StepEntryService stepEntryService;

    @Mock
    private NotificationConfig notificationConfig;

    @InjectMocks
    private StepEntrySyncServiceImpl stepEntrySyncService;

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
                                        .stream().map(User::userId).collect(Collectors.toList()))
                        .employees(
                                List.of(userForProjectLead(1),
                                        userForEmployee(4),
                                        userForEmployee(5),
                                        userForEmployee(6))
                                        .stream().map(User::userId).collect(Collectors.toList()))
                        .build(),
                projectFor(2)
                        .leads(
                                List.of(userForProjectLead(2))
                                        .stream().map(User::userId).collect(Collectors.toList()))
                        .employees(
                                List.of(userForProjectLead(2),
                                        userForEmployee(5),
                                        userForEmployee(6))
                                        .stream().map(User::userId).collect(Collectors.toList()))
                        .build(),
                projectFor(3)
                        .leads(
                                List.of(userForProjectLead(1))
                                        .stream().map(User::userId).collect(Collectors.toList()))
                        .employees(
                                List.of(userForProjectLead(1),
                                        userForEmployee(5),
                                        userForEmployee(6))
                                        .stream().map(User::userId).collect(Collectors.toList()))
                        .build()
        ));
        when(stepService.getSteps()).thenReturn(
                List.of(stepFor(1, "CONTROL_TIMES", null).build(),
                        stepFor(2, "CONTROL_INTERNAL_TIMES", "OFFICE_MANAGEMENT").build(),
                        stepFor(3, "CONTROL_EXTERNAL_TIMES", "OFFICE_MANAGEMENT").build(),
                        stepFor(4, "CONTROL_TIME_EVIDENCES", "PROJECT_LEAD").build(),
                        stepFor(5, "ACCEPT_TIMES", "OFFICE_MANAGEMENT").build()
                ));
        when(notificationConfig.getOmMailAddresses()).thenReturn(List.of(userForOm(3).email()));
    }

    @Nested
    class WithAll {

        @BeforeEach
        void setUp() {

        }

        @Test
        void whenAllDataIsProvided_thenInsertDefinedNumberOfItems() {
            // Given
            // default setup

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            verify(stepEntryService, times(34)).addStepEntry(Mockito.any());
        }
    }

    @Nested
    class WithProjectLead {

        @BeforeEach
        void setUp() {
            when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());
        }

        @Test
        void whenNoProjectsProvided_thenDoNotInsertProjectLead() {
            // Given
            when(projectService.getProjectsForMonthYear(Mockito.any(), Mockito.anyList())).thenReturn(List.of());

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
            verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
            final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

            // Project Lead 1
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 1).count());
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 1).count());

            // Project Lead 2
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 2).count());
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 2).count());

            // Steps
            Assertions.assertEquals(0, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_TIME_EVIDENCES")).count());
        }

        @Test
        void whenProjectLeadIsInactive_thenDoNotInsertProjectLead() {
            // Given
            when(projectService.getProjectsForMonthYear(Mockito.any(), Mockito.anyList())).thenReturn(List.of(projectFor(1)
                    .leads(
                            List.of(userForProjectLead(7))
                                    .stream().map(User::userId).collect(Collectors.toList()))
                    .employees(
                            List.of(userForProjectLead(1),
                                    userForEmployee(4),
                                    userForEmployee(5),
                                    userForEmployee(6))
                                    .stream().map(User::userId).collect(Collectors.toList()))
                    .build()));

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
            verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
            final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

            // Project Lead 1
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 1).count());
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 1).count());

            // Project Lead 2
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 2).count());
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 2).count());

            // Steps
            Assertions.assertEquals(0, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_TIME_EVIDENCES")).count());
        }

        @Test
        void whenDefaultDataIsProvided_thenInsertProjectLead() {
            // Given
            // default setup

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
            verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
            final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

            // Project Lead 1
            Assertions.assertEquals(3, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 1).count());
            Assertions.assertEquals(8, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 1).count());

            // Project Lead 2
            Assertions.assertEquals(2, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 2).count());
            Assertions.assertEquals(4, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 2).count());

            // Steps
            Assertions.assertEquals(10, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_TIME_EVIDENCES")).count());
        }
    }

    @Nested
    class WithOfficeManagment {

        @BeforeEach
        void setUp() {

        }

        @Test
        void whenOfficeManagmentIsInactive_thenDoNotInsertOfficeManagment() {
            // Given
            when(notificationConfig.getOmMailAddresses()).thenReturn(List.of("some.user@gepardec.com"));

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
            verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
            final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

            // Office Managment 3
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 3).count());
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 3).count());

            // Steps
            Assertions.assertEquals(0, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_INTERNAL_TIMES")).count());
            Assertions.assertEquals(0, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_EXTERNAL_TIMES")).count());
            Assertions.assertEquals(0, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("ACCEPT_TIMES")).count());
        }

        @Test
        void whenNoOfficeManagmentIsProvided_thenDoNotInsertOfficeManagment() {
            // Given
            when(notificationConfig.getOmMailAddresses()).thenReturn(List.of());

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
            verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
            final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

            // Office Managment 3
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 3).count());
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 3).count());

            // Steps
            Assertions.assertEquals(0, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_INTERNAL_TIMES")).count());
            Assertions.assertEquals(0, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_EXTERNAL_TIMES")).count());
            Assertions.assertEquals(0, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("ACCEPT_TIMES")).count());
        }

        @Test
        void whenDefaultDataIsProvided_thenInsertOfficeManagment() {
            // Given
            // default setup

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
            verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
            final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

            // Office Managment 3
            Assertions.assertEquals(4, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 3).count());
            Assertions.assertEquals(19, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 3).count());

            // Steps
            Assertions.assertEquals(6, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_INTERNAL_TIMES")).count());
            Assertions.assertEquals(6, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_EXTERNAL_TIMES")).count());
            Assertions.assertEquals(6, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("ACCEPT_TIMES")).count());
        }
    }

    @Nested
    class WithEmployee {

        @BeforeEach
        void setUp() {

        }

        @Test
        void whenNoActiveUsers_thenDoNotInsertEmployees() {
            // Given
            when(userService.findActiveUsers()).thenReturn(List.of());

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            verify(stepEntryService, never()).addStepEntry(Mockito.any());
        }

        @Test
        void whenDefaultDataIsProvided_thenInsertEmployees() {
            // Given
            // default setup

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
            verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
            final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

            // Employee 4
            Assertions.assertEquals(5, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 4).count());
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 4).count());

            // Employee 5
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 5).count());
            Assertions.assertEquals(7, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 5).count());

            // Employee 6
            Assertions.assertEquals(7, stepEntries.stream().filter(stepEntry -> stepEntry.owner().dbId() == 6).count());
            Assertions.assertEquals(1, stepEntries.stream().filter(stepEntry -> stepEntry.assignee().dbId() == 6).count());

            // Steps
            Assertions.assertEquals(6, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_TIMES")).count());
        }
    }

    @Nested
    class WithProject {

        @BeforeEach
        void setUp() {

        }

        @Test
        void whenNoStepsProvided_thenDoNotInsertSteps() {
            // Given
            when(stepService.getSteps()).thenReturn(List.of());

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            verify(stepEntryService, never()).addStepEntry(Mockito.any());
        }

        @Test
        void whenDefaultDataIsProvided_thenInsertSteps() {
            // Given
            // default setup

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
            verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
            final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

            // Project 1
            Assertions.assertEquals(4, stepEntries.stream().filter(stepEntry -> stepEntry.project() != null && stepEntry.project().projectId().equals(String.valueOf(1))).count());

            // Project 2
            Assertions.assertEquals(3, stepEntries.stream().filter(stepEntry -> stepEntry.project() != null && stepEntry.project().projectId().equals(String.valueOf(2))).count());

            // Project 3
            Assertions.assertEquals(3, stepEntries.stream().filter(stepEntry -> stepEntry.project() != null && stepEntry.project().projectId().equals(String.valueOf(3))).count());
        }
    }

    @Nested
    class WithStep {

        @BeforeEach
        void setUp() {

        }

        @Test
        void whenDefaultDataIsProvided_thenInsertSteps() {
            // Given
            // default setup

            // When
            stepEntrySyncService.genereteStepEntries();

            // Then
            final ArgumentCaptor<StepEntry> argumentCaptor = ArgumentCaptor.forClass(StepEntry.class);
            verify(stepEntryService, atLeastOnce()).addStepEntry(argumentCaptor.capture());
            final List<StepEntry> stepEntries = argumentCaptor.getAllValues();

            // Step 1
            Assertions.assertEquals(6, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_TIMES")).count());

            // Step 2
            Assertions.assertEquals(6, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_INTERNAL_TIMES")).count());

            // Step 3
            Assertions.assertEquals(6, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_EXTERNAL_TIMES")).count());

            // Step 4
            Assertions.assertEquals(10, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("CONTROL_TIME_EVIDENCES")).count());

            // Step 5
            Assertions.assertEquals(6, stepEntries.stream().filter(stepEntry -> stepEntry.step().name().equals("ACCEPT_TIMES")).count());
        }
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

    private User.Builder userFor(final int id, final String firstname, final String lastname) {
        return User.builder()
                .dbId(id)
                .userId(id + "-userId")
                .firstname(firstname + id)
                .lastname(lastname)
                .roles(Set.of(Role.EMPLOYEE))
                .email(String.format("%s%s.%s@gepardec.com", firstname, id, lastname));
    }

    private Project.Builder projectFor(final int id) {
        return Project.builder()
                .projectId(String.valueOf(id))
                .description(String.format("Description of Project %s", id));
    }

    private Step.Builder stepFor(final int id, final String name, final String role) {
        return Step.builder()
                .dbId(id)
                .ordinal(id)
                .name(name)
                .role(role);
    }
}