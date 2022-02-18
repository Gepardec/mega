package com.gepardec.mega.rest.impl;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.rest.api.CommentResource;
import com.gepardec.mega.rest.mapper.MapperManager;
import com.gepardec.mega.rest.model.CommentDto;
import com.gepardec.mega.rest.model.NewCommentEntryDto;
import com.gepardec.mega.service.api.CommentService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@RequestScoped
@Secured
public class CommentResourceImpl implements CommentResource {

    @Inject
    MapperManager mapper;

    @Inject
    CommentService commentService;

    @Override
    public Response setDone(final Comment comment) {
        return Response.ok(commentService.setDone(comment)).build();
    }

    @Override
    public Response getAllCommentsForEmployee(String employeeEmail, String currentMonthYear) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfCurrentMonth(currentMonthYear));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfCurrentMonth(currentMonthYear));

        List<Comment> commentsForEmployee = commentService.findCommentsForEmployee(Employee.builder().email(employeeEmail).build(), from, to);
        return Response.ok(mapper.mapAsList(commentsForEmployee, CommentDto.class)).build();
    }

    @Override
    public Response newCommentForEmployee(NewCommentEntryDto newComment) {
        Comment comment = commentService.createNewCommentForEmployee(
                newComment.stepId(),
                newComment.employee(),
                newComment.comment(),
                newComment.assigneeEmail(),
                newComment.project(),
                newComment.currentMonthYear()
        );
        CommentDto mappedComment = mapper.map(comment, CommentDto.class);
        return Response.ok(mappedComment).build();
    }

    @Override
    public Response deleteComment(Long id) {
        return Response.ok(commentService.deleteCommentWithId(id)).build();
    }

    @Override
    public Response updateCommentForEmployee(Comment comment) {
        Comment updatedComment = commentService.updateComment(comment.getId(), comment.getMessage());

        return Response.ok(mapper.map(updatedComment, CommentDto.class)).build();
    }
}
