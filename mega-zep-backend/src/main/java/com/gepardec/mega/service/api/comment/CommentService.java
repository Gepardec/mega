package com.gepardec.mega.service.api.comment;

import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;

import java.time.LocalDate;
import java.util.List;

public interface CommentService {

    List<Comment> findCommentsForEmployee(final Employee employee, LocalDate from, LocalDate to);

    int setDone(final Comment comment);

    FinishedAndTotalComments cntFinishedAndTotalCommentsForEmployee(final Employee employee, LocalDate from, LocalDate to);

    Comment createNewCommentForEmployee(Long stepId, Employee employee, String comment, String assigneeEmail, String project, String currentMonthYear);

    boolean deleteCommentWithId(Long id);

    Comment updateComment(Long id, String message);
}
