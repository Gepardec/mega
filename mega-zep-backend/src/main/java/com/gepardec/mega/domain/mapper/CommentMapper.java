package com.gepardec.mega.domain.mapper;

import com.gepardec.mega.domain.model.Comment;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommentMapper {

    public Comment mapDbCommentToDomainComment(com.gepardec.mega.db.entity.employee.Comment dbComment) {
        Comment domainComment = Comment
                .builder()
                .id(dbComment.getId())
                .authorEmail(dbComment.getStepEntry().getAssignee().getEmail())
                .authorName(String.format("%s %s",
                        dbComment.getStepEntry().getAssignee().getFirstname(),
                        dbComment.getStepEntry().getAssignee().getLastname()))
                .updateDate(dbComment.getUpdatedDate().toString())
                .message(dbComment.getMessage())
                .state(dbComment.getState())
                .build();

        return domainComment;
    }

}
