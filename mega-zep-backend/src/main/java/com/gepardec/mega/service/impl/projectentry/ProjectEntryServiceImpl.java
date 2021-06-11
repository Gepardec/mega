package com.gepardec.mega.service.impl.projectentry;

import com.gepardec.mega.db.entity.project.Project;
import com.gepardec.mega.db.entity.project.ProjectEntry;
import com.gepardec.mega.db.repository.ProjectEntryRepository;
import com.gepardec.mega.service.api.projectentry.ProjectEntryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ProjectEntryServiceImpl implements ProjectEntryService {

    @Inject
    ProjectEntryRepository projectEntryRepository;

    @Override
    public List<ProjectEntry> findByNameAndDate(String projectName, LocalDate from, LocalDate to) {
        return projectEntryRepository.findByNameAndDate(projectName, from, to);
    }
}
