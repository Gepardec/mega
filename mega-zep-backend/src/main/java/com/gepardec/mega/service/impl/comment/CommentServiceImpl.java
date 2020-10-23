package com.gepardec.mega.service.impl.comment;

import com.gepardec.mega.db.repository.CommentRepository;
import com.gepardec.mega.domain.mapper.CommentMapper;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.api.comment.CommentService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CommentServiceImpl implements CommentService {

    @Inject
    CommentRepository commentRepository;

    @Inject
    CommentMapper commentMapper;

    @Override
    public List<Comment> findCommentsForEmployee(final Employee employee) {
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        List<com.gepardec.mega.db.entity.Comment> dbComments =
                commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(fromDate, toDate, employee.email());

        List<Comment> domainComments = dbComments
                .stream()
                .map(commentMapper::mapDbCommentToDomainComment).collect(Collectors.toList());

        return domainComments;
    }
}
