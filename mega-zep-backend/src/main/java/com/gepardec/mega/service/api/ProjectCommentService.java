package com.gepardec.mega.service.api;

import com.gepardec.mega.rest.model.ProjectCommentDto;

import java.time.LocalDate;
import java.util.List;

public interface ProjectCommentService {

    List<ProjectCommentDto> findForProjectNameInRange(String projectName, LocalDate from, LocalDate to);

    ProjectCommentDto findForProjectNameWithCurrentYearMonth(String projectName, String currentYearMonth);

    ProjectCommentDto create(ProjectCommentDto projectCommentDto);

    boolean update(Long id, String comment);
}
