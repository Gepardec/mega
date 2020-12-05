package com.gepardec.mega.service.impl.comment;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.repository.CommentRepository;
import com.gepardec.mega.domain.mapper.CommentMapper;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.notification.mail.Mail;
import com.gepardec.mega.notification.mail.MailParameter;
import com.gepardec.mega.notification.mail.MailSender;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
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
    public List<Comment> findCommentsForEmployee(final Employee employee, LocalDate from, LocalDate to) {
        List<com.gepardec.mega.db.entity.Comment> dbComments =
                commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(from, to, employee.email());

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
    public FinishedAndTotalComments cntFinishedAndTotalCommentsForEmployee(Employee employee, LocalDate from, LocalDate to) {
        Objects.requireNonNull(employee, "Employee must not be null!");
        Objects.requireNonNull(from, "From date must not be null!");
        Objects.requireNonNull(to, "To date must not be null!");

        List<com.gepardec.mega.db.entity.Comment> allComments =
                commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(
                        from, to, employee.email()
                );

        long finishedCommands = allComments.stream().filter(comment -> State.DONE.equals(comment.getState())).count();
        return FinishedAndTotalComments.builder()
                .finishedComments(finishedCommands)
                .totalComments((long) allComments.size())
                .build();
    }

    @Override
    public Comment createNewCommentForEmployee(Long stepId, Employee employee, String comment, String assigneeEmail, String project, String currentMonthYear) {
        Objects.requireNonNull(employee);
        com.gepardec.mega.db.entity.StepEntry stepEntry = StringUtils.isBlank(project) ?
                stepEntryService.findStepEntryForEmployeeAtStep(stepId, employee, assigneeEmail, currentMonthYear) :
                stepEntryService.findStepEntryForEmployeeAndProjectAtStep(stepId, employee, assigneeEmail, project, currentMonthYear);
        com.gepardec.mega.db.entity.Comment newComment = new com.gepardec.mega.db.entity.Comment();
        newComment.setMessage(comment);
        newComment.setStepEntry(stepEntry);
        commentRepository.save(newComment);
        sendMail(employee, newComment);
        return commentMapper.mapDbCommentToDomainComment(newComment);
    }

    private void sendMail(Employee employee, com.gepardec.mega.db.entity.Comment comment) {
        String creator = comment.getStepEntry().getAssignee().getFirstname();
        Map<String, String> mailParameter = Map.of(
                MailParameter.CREATOR, creator,
                MailParameter.RECIPIENT, employee.firstname(),
                MailParameter.COMMENT, comment.getMessage()
        );

        mailSender.send(Mail.COMMENT_CREATED, employee.email(), employee.firstname(), Locale.GERMAN, mailParameter, List.of(creator));
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean deleteCommentWithId(Long id) {
        return commentRepository.deleteComment(id);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Comment updateComment(Long id, String message) {
        com.gepardec.mega.db.entity.Comment commentDb = commentRepository.findById(id);
        if(commentDb == null) {
            throw new EntityNotFoundException(String.format("No entity found for id = %d", id));
        }

        commentDb.setMessage(message);
        commentRepository.update(commentDb);

        return commentMapper.mapDbCommentToDomainComment(commentDb);
    }
}
