package com.gepardec.mega.service.api.project;

import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.ProjectFilter;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    List<Project> getProjectsForMonthYear(final LocalDate monthYear);

    List<Project> getProjectsForMonthYear(final LocalDate monthYear, final List<ProjectFilter> projectFilters);

    void addProject(final com.gepardec.mega.db.entity.project.Project project);
}
