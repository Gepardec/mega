package com.gepardec.mega.service.impl.init;

import com.cronutils.utils.StringUtils;
import com.gepardec.mega.db.entity.Role;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.init.StepEntrySyncService;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.service.api.step.StepService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.service.api.user.UserService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Dependent
@Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
public class StepEntrySyncServiceImpl implements StepEntrySyncService {

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
        final LocalDate date = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());

        final List<User> activeUsers = userService.getActiveUsers();
        final Map<User, List<String>> projectsForUsers = projectService.getProjectsForUsersAndYear(date, activeUsers);
        final Map<String, List<User>> projectLoadsForProjects = projectService.getProjectLeadsForProjectsAndYear(date, activeUsers);
        final List<Step> steps = stepService.getSteps();
        final List<User> omUsers = List.of(omMailAddresses.split(",")).stream().map(email -> activeUsers.stream().filter(u -> u.email().equals(email)).findFirst().orElse(null)).collect(Collectors.toList());

        projectsForUsers.forEach((user, projects) -> steps.forEach(step -> {
            // TODO: after refactoring roles, remove DB model reference
            if (Role.PROJECT_LEAD.name().equals(step.role())) {
                projects.forEach(project -> {
                    if (projectLoadsForProjects.containsKey(project)) {
                        projectLoadsForProjects.get(project).forEach(lead -> {
                            stepEntryService.addStepEntry(user, date, project, step, lead);
                        });
                    }
                });
            } else if (Role.OFFICE_MANAGEMENT.name().equals(step.role())) {
                omUsers.forEach(omUser -> {
                    stepEntryService.addStepEntry(user, date, StringUtils.EMPTY, step, omUser);
                });
            } else {
                stepEntryService.addStepEntry(user, date, StringUtils.EMPTY, step, user);
            }
        }));
    }
}
