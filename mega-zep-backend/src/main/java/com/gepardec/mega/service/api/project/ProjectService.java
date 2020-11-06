package com.gepardec.mega.service.api.project;

import com.gepardec.mega.domain.model.Project;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    List<Project> getProjectsForYear(final LocalDate monthYear);
}
