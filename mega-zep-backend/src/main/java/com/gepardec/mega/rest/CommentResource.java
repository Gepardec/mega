package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.rest.model.NewCommentEntry;
import com.gepardec.mega.service.api.comment.CommentService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@RequestScoped
@Secured
public class CommentResource implements CommentResourceAPI {

    @Inject
    CommentService commentService;

    @Override
    public int setDone(final Comment comment) {
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
