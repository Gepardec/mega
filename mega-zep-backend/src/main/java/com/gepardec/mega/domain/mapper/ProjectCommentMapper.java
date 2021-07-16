package com.gepardec.mega.domain.mapper;

import com.gepardec.mega.db.entity.project.ProjectComment;
import com.gepardec.mega.rest.model.ProjectCommentDto;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectCommentMapper {

    public ProjectCommentDto mapToDto(ProjectComment entity) {
        return ProjectCommentDto.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .comment(entity.getComment())
                .projectName(entity.getProject().getName())
                .build();
    }
}
