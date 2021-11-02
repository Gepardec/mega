package com.gepardec.mega.service.impl.projectentry;

import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.db.entity.project.ProjectEntry;
import com.gepardec.mega.db.repository.ProjectEntryRepository;
import com.gepardec.mega.domain.model.ProjectState;
import com.gepardec.mega.domain.model.ProjectStep;
import com.gepardec.mega.rest.model.ProjectEntryDTO;
import com.gepardec.mega.service.api.projectentry.ProjectEntryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class ProjectEntryServiceImpl implements ProjectEntryService {

    @Inject
    ProjectEntryRepository projectEntryRepository;

    @Override
    public List<ProjectEntry> findByNameAndDate(String projectName, LocalDate from, LocalDate to) {
        return projectEntryRepository.findByNameAndDate(projectName, from, to);
    }

    @Override
    public boolean update(ProjectEntryDTO projectEntry) {
        ProjectEntry projectEntity = findByNameAndEntryDateAndStep(projectEntry.projectName(), LocalDate.parse(projectEntry.currentMonthYear()), projectEntry.step());
        projectEntity.setState(mapProjectStateFromModelToDatabase(projectEntry.state()));
        projectEntity.setPreset(projectEntry.preset());
        return projectEntryRepository.updateProjectEntry(projectEntity);
    }

    private ProjectEntry findByNameAndEntryDateAndStep(String projectName, LocalDate entryDate, ProjectStep projectStep) {
        return projectEntryRepository.findByNameAndEntryDateAndStep(projectName, entryDate, mapProjectStepFromModelToDatabase(projectStep));
    }

    private State mapProjectStateFromModelToDatabase(ProjectState state) {
        return Arrays.stream(State.values())
                .filter(ps -> ps.name().equals(state.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ProjectState not supported!"));
    }

    private com.gepardec.mega.db.entity.project.ProjectStep mapProjectStepFromModelToDatabase(ProjectStep step) {
        return Arrays.stream(com.gepardec.mega.db.entity.project.ProjectStep.values())
                .filter(ps -> ps.name().equals(step.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ProjectStep not supported!"));
    }
}
