package com.gepardec.mega.service.api.comment;

import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;

import java.util.List;

public interface CommentService {
    List<Comment> findCommentsForEmployee(final Employee employee);
}
