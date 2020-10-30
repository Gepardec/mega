package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.db.entity.Role;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.domain.model.User;
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

        logger.info("started generation at {}", Instant.ofEpochMilli(stopWatch.getStartTime()));

        final LocalDate date = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        logger.info("running step entry generation for {}", date);

        final List<User> activeUsers = userService.getActiveUsers();
        final List<Project> projectsForYear = projectService.getProjectsForYear(date);
        final List<Step> steps = stepService.getSteps();

        final List<User> omUsers = List.of(omMailAddresses.split(","))
                .stream()
                .map(email -> findUserByEmail(activeUsers, email))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        logger.info("active users are {}", activeUsers);
        logger.info("projects are {}", projectsForYear);
        logger.info("steps are {}", steps);
        logger.info("omUsers are {}", omUsers);

        activeUsers.forEach(user ->
                steps.forEach(step -> {
                    if (Role.PROJECT_LEAD.name().equals(step.role())) {
                        projectsForYear.stream()
                                .filter(project -> project.employees().contains(user.userId()))
                                .filter(project -> !project.leads().isEmpty())
                                .forEach(project -> project.leads()
                                        .forEach(lead -> findUserByUserId(activeUsers, lead).ifPresent(leadUser -> stepEntryService.addStepEntry(user, date, project, step, leadUser))));
                    } else if (Role.OFFICE_MANAGEMENT.name().equals(step.role())) {
                        omUsers.forEach(omUser -> stepEntryService.addStepEntry(user, date, null, step, omUser));
                    } else {
                        stepEntryService.addStepEntry(user, date, null, step, user);
                    }
                })
        );

        stopWatch.stop();

        logger.info("generation took {}ms", stopWatch.getTime());
        logger.info("finished generation at {}", Instant.ofEpochMilli(stopWatch.getStartTime() + stopWatch.getTime()));
    }

    private Optional<User> findUserByUserId(final List<User> users, final String userId) {
        return users.stream().filter(user -> user.userId().equals(userId)).findFirst();
    }

    private Optional<User> findUserByEmail(final List<User> users, final String email) {
        return users.stream().filter(user -> user.email().equals(email)).findFirst();
    }
}
