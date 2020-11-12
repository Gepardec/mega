package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.db.entity.Role;
import com.gepardec.mega.domain.model.*;
import com.gepardec.mega.service.api.init.StepEntrySyncService;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.service.api.step.StepService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.service.api.user.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
@Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
public class StepEntrySyncServiceImpl implements StepEntrySyncService {

    @Inject
    Logger logger;

    @Inject
    UserService userService;

    @Inject
    ProjectService projectService;

    @Inject
    StepService stepService;

    @Inject
    StepEntryService stepEntryService;

    @Inject
    @ConfigProperty(name = "mega.mail.reminder.om")
    String omMailAddresses;

    @Override
    public void genereteStepEntries() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        logger.info("Started step entry generation: {}", Instant.ofEpochMilli(stopWatch.getStartTime()));

        final LocalDate date = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        logger.info("Processing date: {}", date);

        final List<User> activeUsers = userService.getActiveUsers();
        final List<Project> projectsForMonthYear = projectService.getProjectsForMonthYear(date, List.of(
                ProjectFilter.IS_LEADS_AVAILABLE,
                ProjectFilter.IS_CUSTOMER_PROJECT,
                ProjectFilter.IS_ACTIVE));
        final List<Step> steps = stepService.getSteps();

        final List<User> omUsers = List.of(omMailAddresses.split(","))
                .stream()
                .map(email -> findUserByEmail(activeUsers, email))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        logger.info("Loaded projects: {}", projectsForMonthYear.size());
        logger.debug("projects are {}", projectsForMonthYear);
        logger.info("Loaded users: {}", activeUsers.size());
        logger.debug("Users are: {}", activeUsers);
        logger.info("Loaded steps: {}", steps.size());
        logger.debug("Steps are: {}", steps);
        logger.info("Loaded omUsers: {}", omUsers.size());
        logger.debug("omUsers are: {}", omUsers);

        final List<StepEntry> toBeCreatedStepEntries = new ArrayList<>();

        for (Step step : steps) {// TODO: change to domain model after role refactoring
            if (Role.PROJECT_LEAD.name().equals(step.role())) {
                toBeCreatedStepEntries.addAll(createStepEntriesProjectLeadForUsers(date, step, projectsForMonthYear, activeUsers));
            } else if (Role.OFFICE_MANAGEMENT.name().equals(step.role())) {
                toBeCreatedStepEntries.addAll(createStepEntriesOmForUsers(date, step, omUsers, activeUsers));
            } else {
                toBeCreatedStepEntries.addAll(createStepEntriesForUsers(date, step, activeUsers));
            }
        }

        toBeCreatedStepEntries.forEach(stepEntryService::addStepEntry);

        stopWatch.stop();

        logger.info("Processed step entries: {}", toBeCreatedStepEntries.size());
        logger.info("Step entry generation took: {}ms", stopWatch.getTime());
        logger.info("Finished step entry generation: {}", Instant.ofEpochMilli(stopWatch.getStartTime() + stopWatch.getTime()));
    }

    private List<StepEntry> createStepEntriesProjectLeadForUsers(final LocalDate date, final Step step, final List<Project> projects, final List<User> users) {
        return users.stream()
                .map(owner -> createStepEntriesForOwnerProjects(date, step, projects, users, owner))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<StepEntry> createStepEntriesForOwnerProjects(final LocalDate date, final Step step, final List<Project> projects, final List<User> users, final User owner) {
        return projects.stream()
                .filter(project -> project.employees().contains(owner.userId()))
                .map(project -> createStepEntriesForOwnerProject(date, step, project, users, owner))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<StepEntry> createStepEntriesForOwnerProject(final LocalDate date, final Step step, final Project project, final List<User> users, final User owner) {
        return project.leads()
                .stream()
                .map(lead -> findUserByUserId(users, lead))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(leadUser -> StepEntry.builder()
                        .date(date)
                        .project(project)
                        .state(State.OPEN)
                        .owner(owner)
                        .assignee(leadUser)
                        .step(step)
                        .build())
                .collect(Collectors.toList());
    }

    private List<StepEntry> createStepEntriesOmForUsers(final LocalDate date, final Step step, final List<User> omUsers, final List<User> users) {
        return users.stream()
                .map(owner -> createStepEntriesForOwnerOmUsers(date, step, omUsers, owner))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<StepEntry> createStepEntriesForOwnerOmUsers(final LocalDate date, final Step step, final List<User> omUsers, final User owner) {
        return omUsers.stream()
                .map(leadUser -> StepEntry.builder()
                        .date(date)
                        .project(null)
                        .state(State.OPEN)
                        .owner(owner)
                        .assignee(leadUser)
                        .step(step)
                        .build())
                .collect(Collectors.toList());
    }

    private List<StepEntry> createStepEntriesForUsers(final LocalDate date, final Step step, final List<User> users) {
        return users.stream()
                .map(owner -> StepEntry.builder()
                        .date(date)
                        .project(null)
                        .state(State.OPEN)
                        .owner(owner)
                        .assignee(owner)
                        .step(step)
                        .build())
                .collect(Collectors.toList());
    }

    private Optional<User> findUserByUserId(final List<User> users, final String userId) {
        return users.stream().filter(user -> user.userId().equals(userId)).findFirst();
    }

    private Optional<User> findUserByEmail(final List<User> users, final String email) {
        return users.stream().filter(user -> user.email().equals(email)).findFirst();
    }
}
