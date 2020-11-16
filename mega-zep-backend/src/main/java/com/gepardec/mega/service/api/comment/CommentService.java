package com.gepardec.mega.service.api.comment;

import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface CommentService {
    List<Comment> findCommentsForEmployee(final Employee employee);
    int setDone(final Comment comment);
    FinishedAndTotalComments cntFinishedAndTotalCommentsForEmployee(final Employee employee);
    Comment createNewCommentForEmployee(Long stepId, Employee employee, String comment);
    boolean deleteCommentWithId(Long id);
    Comment updateComment(Long id, String message);
}
