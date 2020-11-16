package com.gepardec.mega.service.impl.comment;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.repository.CommentRepository;
import com.gepardec.mega.domain.mapper.CommentMapper;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.notification.mail.MailSender;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CommentServiceImpl implements CommentService {

    @Inject
    CommentRepository commentRepository;

    @Inject
    CommentMapper commentMapper;

    @Inject
    MailSender mailSender;

    @Inject
    StepEntryService stepEntryService;

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

    @Override
    public int setDone(final Comment comment) {
        //TODO: Send email
        return commentRepository.setStatusDone(comment.id());
    }

    @Override
    public FinishedAndTotalComments cntFinishedAndTotalCommentsForEmployee(Employee employee) {
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        List<com.gepardec.mega.db.entity.Comment> allComments =
                commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(fromDate, toDate, employee.email());

        long finishedCommands = allComments.stream().filter(comment -> State.DONE.equals(comment.getState())).count();
        return FinishedAndTotalComments.builder()
                .finishedComments(finishedCommands)
                .totalComments((long) allComments.size())
                .build();
    }

    @Override
    public Comment createNewCommentForEmployee(Long stepId, Employee employee, String comment) {
        com.gepardec.mega.db.entity.StepEntry stepEntry = stepEntryService.findStepEntryForEmployeeAtStep(stepId, employee);
        com.gepardec.mega.db.entity.Comment newComment = new com.gepardec.mega.db.entity.Comment();
        newComment.setMessage(comment);
        newComment.setStepEntry(stepEntry);
        commentRepository.save(newComment);

        return commentMapper.mapDbCommentToDomainComment(newComment);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean deleteCommentWithId(Long id) {
        return commentRepository.deleteComment(id);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Comment updateComment(Long id, String message) {
        com.gepardec.mega.db.entity.Comment commentDb = commentRepository.findById(id); // TODO ACHTUNG: kann null sein!
        commentDb.setMessage(message);
        commentRepository.update(commentDb);

        return commentMapper.mapDbCommentToDomainComment(commentDb);
    }
}
