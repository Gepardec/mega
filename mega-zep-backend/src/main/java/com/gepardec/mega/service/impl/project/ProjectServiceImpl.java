package com.gepardec.mega.service.impl.project;

import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.ProjectFilter;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.zep.ZepService;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    private static final String INTERN_PROJECT_PREFIX = "Intern";

    @Inject
    ZepService zepService;

    @Override
    public List<Project> getProjectsForMonthYear(LocalDate monthYear) {
        return this.getProjectsForMonthYear(monthYear, List.of());
    }

    @Override
    public List<Project> getProjectsForMonthYear(final LocalDate monthYear, final List<ProjectFilter> projectFilters) {
        return zepService.getProjectsForMonthYear(monthYear)
                .stream()
                .filter(project -> filterProject(project, Optional.ofNullable(projectFilters).orElse(List.of())))
                .collect(Collectors.toList());
    }

    private boolean filterProject(final Project project, final List<ProjectFilter> projectFilters) {
        return projectFilters.stream()
                .allMatch(projectFilter -> filterProject(project, projectFilter));
    }

    private boolean filterProject(final Project project, final ProjectFilter projectFilter) {
        switch (projectFilter) {
            case IS_LEADS_AVAILABLE:
                return !project.leads().isEmpty();
            case IS_CUSTOMER_PROJECT:
                return !StringUtils.startsWith(project.projectId(), INTERN_PROJECT_PREFIX);
            default:
                throw new IllegalStateException(String.format("projectFilter %s not implemented", projectFilter));
        }
    }
}
