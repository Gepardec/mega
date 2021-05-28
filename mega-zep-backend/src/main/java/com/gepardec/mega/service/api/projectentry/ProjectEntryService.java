package com.gepardec.mega.service.api.projectentry;

import com.gepardec.mega.db.entity.project.ProjectEntry;

import java.time.LocalDate;
import java.util.List;

public interface ProjectEntryService {
    List<ProjectEntry> findByNameAndDate(final String projectName, final LocalDate from, final LocalDate to);
}
