package com.gepardec.mega.service.api.projectentry;

import com.gepardec.mega.db.entity.project.ProjectEntry;
import com.gepardec.mega.domain.model.Employee;

import java.util.Optional;

public interface ProjectEntryService {
    Optional<ProjectEntry> findAllProjectEntriesForProjectNameInRange(final Employee employee);
}
