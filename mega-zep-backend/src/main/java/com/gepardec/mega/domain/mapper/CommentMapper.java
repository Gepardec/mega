package com.gepardec.mega.domain.mapper;

import com.gepardec.mega.domain.model.Comment;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommentMapper {

    public Comment mapDbCommentToDomainComment(com.gepardec.mega.db.entity.Comment dbComment) {
        Comment domainComment = Comment
                .builder()
                .id(dbComment.getId())
                .author(dbComment.getStepEntry().getAssignee().getEmail())
                .updateDate(dbComment.getUpdatedDate().toString())
                .message(dbComment.getMessage())
                .state(dbComment.getState())
                .build();

        return domainComment;
    }

}
