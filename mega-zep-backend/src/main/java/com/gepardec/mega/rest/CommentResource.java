package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.rest.model.NewCommentEntry;
import com.gepardec.mega.service.api.comment.CommentService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@RequestScoped
public class CommentResource implements CommentResourceAPI {

    @Inject
    CommentService commentService;

    @Override
    public int setDone(@NotNull(message = "{commentResource.comment.notNull}") final Comment comment) {
        return commentService.setDone(comment);
    }

    @Override
    public List<Comment> getAllCommentsForEmployee(String employeeEmail, String currentMonthYear) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfCurrentMonth(currentMonthYear));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfCurrentMonth(currentMonthYear));
        return commentService.findCommentsForEmployee(Employee.builder().email(employeeEmail).build(), from, to);
    }

    @Override
    public Comment newCommentForEmployee(NewCommentEntry newComment) {
        return commentService.createNewCommentForEmployee(
                newComment.stepId(),
                newComment.employee(),
                newComment.comment(),
                newComment.assigneeEmail(),
                newComment.project(),
                newComment.currentMonthYear()
        );
    }

    @Override
    public boolean deleteComment(Long id) {
        return commentService.deleteCommentWithId(id);
    }

    @Override
    public Comment updateCommentForEmployee(Comment comment) {
        return commentService.updateComment(comment.id(), comment.message());
    }

}
