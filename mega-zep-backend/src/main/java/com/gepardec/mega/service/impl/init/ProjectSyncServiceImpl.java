package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.db.entity.project.ProjectEntry;
import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.db.entity.project.ProjectStep;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.ProjectFilter;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.init.ProjectSyncService;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.service.api.projectentry.ProjectEntryService;
import com.gepardec.mega.service.api.user.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Dependent
@Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
public class ProjectSyncServiceImpl implements ProjectSyncService {

    @Inject
    Logger logger;

    @Inject
    UserService userService;

    @Inject
    ProjectService projectService;

    @Inject
    ProjectEntryService projectEntryService;

    @Override
    public boolean generateProjects() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        logger.info("Started project generation: {}", Instant.ofEpochMilli(stopWatch.getStartTime()));

        LocalDate date = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        logger.info("Processing date: {}", date);

        List<User> activeUsers = userService.findActiveUsers();
        List<Project> projectsForMonthYear = projectService.getProjectsForMonthYear(date,
                List.of(ProjectFilter.IS_LEADS_AVAILABLE,
                        ProjectFilter.IS_CUSTOMER_PROJECT));

        logger.info("Loaded projects: {}", projectsForMonthYear.size());
        logger.debug("projects are {}", projectsForMonthYear);
        logger.info("Loaded users: {}", activeUsers.size());
        logger.debug("Users are: {}", activeUsers);

        createProjects(activeUsers, projectsForMonthYear, date)
                .forEach(projectService::addProject);

        List<Project> projects = projectService.getProjectsForMonthYear(date);

        stopWatch.stop();

        logger.debug("projects in db are {}", projects);

        logger.info("Project generation took: {}ms", stopWatch.getTime());
        logger.info("Finished project generation: {}", Instant.ofEpochMilli(stopWatch.getStartTime() + stopWatch.getTime()));

        return !projects.isEmpty();
    }

    private List<com.gepardec.mega.db.entity.project.Project> createProjects(List<User> activeUsers, List<Project> projects, LocalDate date) {
        return projects.stream()
                .map(project -> createProjectEntityFromProject(activeUsers, project, date))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<com.gepardec.mega.db.entity.project.Project> createProjectEntityFromProject(List<User> activeUsers, Project project, LocalDate date) {
        com.gepardec.mega.db.entity.project.Project projectEntity = new com.gepardec.mega.db.entity.project.Project();

        List<User> leads = project.leads()
                .stream()
                .filter(Objects::nonNull)
                .filter(userid -> !userid.isBlank())
                .map(userid -> findUserByUserId(activeUsers, userid))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (leads.isEmpty()) {
            return Optional.empty();
        }

        Set<com.gepardec.mega.db.entity.employee.User> mappedLeads = leads.stream()
                .map(this::mapDomainUserToEntity)
                .collect(Collectors.toSet());

        projectEntity.setProjectLeads(mappedLeads);
        projectEntity.setName(project.projectId());
        projectEntity.setStartDate(project.startDate());
        projectEntity.setEndDate(project.endDate());

        Arrays.stream(ProjectStep.values()).forEach(projectStep ->
                projectEntity.addProjectEntry(createProjectEntry(projectEntity, mappedLeads, date, projectStep))
        );

        return Optional.of(projectEntity);
    }

    private Optional<User> findUserByUserId(final List<User> users, final String userId) {
        return users.stream().filter(user -> user.userId().equals(userId)).findFirst();
    }

    private com.gepardec.mega.db.entity.employee.User mapDomainUserToEntity(User user) {
        com.gepardec.mega.db.entity.employee.User u = new com.gepardec.mega.db.entity.employee.User();
        u.setId(user.dbId());
        return u;
    }

    private ProjectEntry createProjectEntry(com.gepardec.mega.db.entity.project.Project project,
                                            Set<com.gepardec.mega.db.entity.employee.User> leads,
                                            LocalDate date, ProjectStep step) {
        ProjectEntry projectEntry = new ProjectEntry();
        projectEntry.setProject(project);
        projectEntry.setName(project.getName());
        projectEntry.setOwner(
                leads.stream()
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Project without project lead found."))
        );
        projectEntry.setAssignee(
                leads.stream()
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Project without project lead found."))
        );
        projectEntry.setDate(date);
        projectEntry.setCreationDate(LocalDateTime.now());
        projectEntry.setUpdatedDate(LocalDateTime.now());
        projectEntry.setState(State.OPEN);
        projectEntry.setStep(step);

        LocalDate from = date.minusMonths(1).withDayOfMonth(1);
        LocalDate to = date.minusMonths(1).withDayOfMonth(date.minusMonths(1).lengthOfMonth());
        Optional<ProjectEntry> projectEntryValue = projectEntryService.findByNameAndDate(project.getName(), from, to)
                .stream()
                .filter(Objects::nonNull)
                .filter(pe -> pe.getStep().getId() == step.getId())
                .findFirst();

        projectEntryValue.ifPresentOrElse(
                p -> projectEntry.setPreset(p.isPreset()),
                () -> projectEntry.setPreset(false)
        );

        return projectEntry;
    }
}
