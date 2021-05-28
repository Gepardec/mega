package com.gepardec.mega.service.impl.comment;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.db.repository.CommentRepository;
import com.gepardec.mega.domain.mapper.CommentMapper;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
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
import java.util.HashMap;
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

        return dbComments
                .stream()
                .map(commentMapper::mapDbCommentToDomainComment).collect(Collectors.toList());
    }

    @Override
    public int setDone(final Comment comment) {
        com.gepardec.mega.db.entity.Comment commentDb = commentRepository.findById(comment.id());
        sendMail(Mail.COMMENT_CLOSED, commentDb);
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
        sendMail(Mail.COMMENT_CREATED, newComment);
        return commentMapper.mapDbCommentToDomainComment(newComment);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean deleteCommentWithId(Long id) {
        com.gepardec.mega.db.entity.Comment comment = commentRepository.findById(id);
        boolean deleted = commentRepository.deleteComment(id);
        if (deleted) {
            sendMail(Mail.COMMENT_DELETED, comment);
        }

        return deleted;
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Comment updateComment(Long id, String message) {
        com.gepardec.mega.db.entity.Comment commentDb = commentRepository.findById(id);
        if (commentDb == null) {
            throw new EntityNotFoundException(String.format("No entity found for id = %d", id));
        }

        commentDb.setMessage(message);
        commentRepository.update(commentDb);
        sendMail(Mail.COMMENT_MODIFIED, commentDb);
        return commentMapper.mapDbCommentToDomainComment(commentDb);
    }

    private void sendMail(Mail mail, com.gepardec.mega.db.entity.Comment comment) {
        StepEntry stepEntry = comment.getStepEntry();
        String creator = comment.getStepEntry().getAssignee().getFirstname();
        String recipient = Mail.COMMENT_CLOSED.equals(mail) ? stepEntry.getAssignee().getFirstname() : stepEntry.getOwner().getFirstname();
        String recipientEmail = Mail.COMMENT_CLOSED.equals(mail) ? stepEntry.getAssignee().getEmail() : stepEntry.getOwner().getEmail();
        Map<String, String> mailParameter = new HashMap<>() {{
            put(MailParameter.CREATOR, creator);
            put(MailParameter.RECIPIENT, recipient);
            put(MailParameter.COMMENT, comment.getMessage());
        }};

        mailSender.send(
                mail,
                recipientEmail,
                stepEntry.getOwner().getFirstname(),
                Locale.GERMAN,
                mailParameter,
                List.of(creator)
        );
    }

}
