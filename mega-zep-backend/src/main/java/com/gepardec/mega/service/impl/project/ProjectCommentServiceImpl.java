package com.gepardec.mega.service.impl.project;

import com.gepardec.mega.db.entity.project.Project;
import com.gepardec.mega.db.entity.project.ProjectComment;
import com.gepardec.mega.db.repository.ProjectCommentRepository;
import com.gepardec.mega.db.repository.ProjectRepository;
import com.gepardec.mega.domain.mapper.ProjectCommentMapper;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.rest.model.ProjectCommentDto;
import com.gepardec.mega.service.api.project.ProjectCommentService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectCommentServiceImpl implements ProjectCommentService {

    @Inject
    ProjectCommentRepository projectCommentRepository;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    ProjectCommentMapper projectCommentMapper;

    @Override
    public List<ProjectCommentDto> findForProjectNameInRange(String projectName, LocalDate from, LocalDate to) {
        List<ProjectComment> entities = projectCommentRepository.findByProjectNameAndDateBetween(projectName, from, to);
        return entities.stream().map(e -> projectCommentMapper.mapToDto(e)).collect(Collectors.toList());
    }

    @Override
    public ProjectCommentDto findForProjectNameWithCurrentYearMonth(String projectName, String currentYearMonth) {
        LocalDate from = DateUtils.getFirstDayOfCurrentMonth(currentYearMonth);
        LocalDate to = DateUtils.getLastDayOfCurrentMonth(currentYearMonth);
        List<ProjectCommentDto> projectComments = this.findForProjectNameInRange(projectName, from, to);
        return projectComments.isEmpty() ? null : projectComments.get(0);
    }

    @Override
    public ProjectCommentDto create(ProjectCommentDto dto) {
        Objects.requireNonNull(dto);

        Project project = projectRepository.findByName(dto.getProjectName());
        Objects.requireNonNull(project);

        List<ProjectComment> existingProjectComments = projectCommentRepository.findByProjectNameWithDate(project.getName(), dto.getDate());

        if (existingProjectComments.isEmpty()) {
            ProjectComment newProjectComment = new ProjectComment();
            newProjectComment.setComment(dto.getComment());
            newProjectComment.setProject(project);
            newProjectComment.setDate(dto.getDate());

            projectCommentRepository.save(newProjectComment);

            return projectCommentMapper.mapToDto(newProjectComment);
        } else {
            // update comment of existing project comment
            ProjectComment projectCommentToUpdate = existingProjectComments.get(0);
            projectCommentToUpdate.setComment(dto.getComment());
            projectCommentRepository.update(projectCommentToUpdate);

            return projectCommentMapper.mapToDto(projectCommentToUpdate);
        }

    }

    @Override
    public boolean update(Long id, String comment) {
        ProjectComment entity = projectCommentRepository.findById(id);
        if (entity == null) {
            throw new EntityNotFoundException(String.format("No entity found for id = %d", id));
        }
        entity.setComment(comment);
        return projectCommentRepository.update(entity);
    }
}
