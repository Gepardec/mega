package com.gepardec.mega.service.api.project;

import com.gepardec.mega.rest.model.ProjectCommentDto;

import java.time.LocalDate;
import java.util.List;


public interface ProjectCommentService {

    List<ProjectCommentDto> findProjectCommentsForProjectNameInRange(String projectName, LocalDate from, LocalDate to);

    ProjectCommentDto findProjectCommentForProjectNameWithCurrentYearMonth(String projectName, String currentYearMonth);

    ProjectCommentDto createProjectComment(ProjectCommentDto projectCommentDto);

    boolean updateProjectComment(Long id, String comment);
}
