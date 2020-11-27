package com.gepardec.mega.service.impl.comment;

import com.gepardec.mega.db.entity.Comment;
import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.db.entity.User;
import com.gepardec.mega.db.repository.CommentRepository;
import com.gepardec.mega.domain.mapper.CommentMapper;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import com.gepardec.mega.notification.mail.Mail;
import com.gepardec.mega.notification.mail.MailParameter;
import com.gepardec.mega.notification.mail.MailSender;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class CommentServiceImplTest {

    @Inject
    CommentService commentService;

    @Inject
    CommentMapper commentMapper;

    @InjectMock
    StepEntryService stepEntryService;

    @InjectMock
    MailSender mailSender;

    @InjectMock
    private CommentRepository commentRepository;

    @Test
    void findCommentsForEmployee_when1DbComment_thenMap1DomainComment() {
        when(commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString())).thenReturn(List.of(createComment(1L, State.OPEN)));

        List<com.gepardec.mega.domain.model.Comment> domainComments = commentService.findCommentsForEmployee(createEmployee());
        Assertions.assertFalse(domainComments.isEmpty());
        Assertions.assertEquals(1, domainComments.size());
        Assertions.assertEquals(1, domainComments.get(0).id());
    }

    @Test
    void setDone_whenNoneUpdated_then0() {
        when(commentRepository.setStatusDone(ArgumentMatchers.anyLong())).thenReturn(0);

        int updatedCount = commentService.setDone(commentMapper.mapDbCommentToDomainComment(createComment(1L, State.IN_PROGRESS)));
        Assertions.assertEquals(0, updatedCount);
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenEmployeeIsNull_thenThrowsException() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> commentService.cntFinishedAndTotalCommentsForEmployee(null),
                "Expected NullpointerException was not thrown!"
        );

        assertEquals("Employee must not be null!", thrown.getMessage());
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenReleaseDateIsNull_thenThrowsException() {
        Employee empl = Employee.builder()
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .releaseDate(null)
                .firstName("Thomas")
                .build();

        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> commentService.cntFinishedAndTotalCommentsForEmployee(empl),
                "Expected NullpointerException was not thrown!"
        );

        assertEquals("Date must not be null!", thrown.getMessage());
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenValid_thenReturnsCnt() {
        when(commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString()
        )).thenReturn(List.of(
                createComment(1L, State.IN_PROGRESS),
                createComment(2L, State.DONE),
                createComment(3L, State.OPEN)
                ));

        FinishedAndTotalComments result = commentService.cntFinishedAndTotalCommentsForEmployee(createEmployee());
        assertNotNull(result);
        assertEquals(3L, result.totalComments());
        assertEquals(1L, result.finishedComments());
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenNoFinishedComments_thenReturnsCnt() {
        when(commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString()
        )).thenReturn(List.of(
                createComment(1L, State.IN_PROGRESS),
                createComment(2L, State.OPEN),
                createComment(3L, State.OPEN)
        ));

        FinishedAndTotalComments result = commentService.cntFinishedAndTotalCommentsForEmployee(createEmployee());
        assertNotNull(result);
        assertEquals(3L, result.totalComments());
        assertEquals(0L, result.finishedComments());
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenNoComments_thenReturnsCnt() {
        when(commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString()
        )).thenReturn(Collections.emptyList());

        FinishedAndTotalComments result = commentService.cntFinishedAndTotalCommentsForEmployee(createEmployee());
        assertNotNull(result);
        assertEquals(0L, result.totalComments());
        assertEquals(0L, result.finishedComments());
    }

    @Test
    void createNewCommentForEmployee_whenEmployeeIsNull_thenThrowsException() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> commentService.cntFinishedAndTotalCommentsForEmployee(null),
                "Expected NullpointerException was not thrown!"
        );

        assertEquals("Employee must not be null!", thrown.getMessage());
    }

    @Test
    void createNewCommentForEmployee_whenValid_thenReturnCreatedComment() {
        StepEntry stepEntry = createStepEntry(1L);
        when(stepEntryService.findStepEntryForEmployeeAtStep(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(Employee.class),
                ArgumentMatchers.anyString()
        )).thenReturn(createStepEntry(1L));

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ((Comment) args[0]).setUpdatedDate(LocalDateTime.now());
            ((Comment) args[0]).setState(State.OPEN);
            return args[0];
        }).when(commentRepository).save(ArgumentMatchers.any(Comment.class));

        doNothing().when(mailSender).send(
                ArgumentMatchers.any(Mail.class),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(Locale.class),
                ArgumentMatchers.anyMap(),
                ArgumentMatchers.anyList()
        );

        Employee employee = createEmployee();
        String newComment = "My new comment!";
        com.gepardec.mega.domain.model.Comment createdComment = commentService.createNewCommentForEmployee(2L, employee, newComment, "", null);

        String creator = stepEntry.getAssignee().getFirstname();
        Map<String, String> expectedMailParameter = Map.of(
                MailParameter.CREATOR, creator,
                MailParameter.RECIPIENT, employee.firstName(),
                MailParameter.COMMENT, newComment
        );

        verify(mailSender, times(1)).send(
                Mail.COMMENT_CREATED, employee.email(), employee.firstName(), Locale.GERMAN, expectedMailParameter, List.of(creator)
        );

        assertNotNull(createdComment);
        assertEquals("My new comment!", createdComment.message());
        assertEquals(stepEntry.getAssignee().getEmail(), createdComment.author());
        assertEquals(State.OPEN, createdComment.state());
    }

    @Test
    void updateComment_whenEntityNotFound_thenThrowsException() {
        when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(null);

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> commentService.updateComment(1L, "My message!"),
                "Expected EntityNotFoundException was not thrown!"
        );

        assertEquals("No entity found for id = 1", thrown.getMessage());
    }

    @Test
    void updateComment_whenValid_thenReturnUpdatedComment() {
        Comment originalComment = createComment(1L, State.DONE);
        when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(originalComment);
        when(commentRepository.update(ArgumentMatchers.any(Comment.class))).thenReturn(null);

        com.gepardec.mega.domain.model.Comment updatedComment = commentService.updateComment(1L, "Updated message");
        assertNotNull(updatedComment);
        assertEquals("Updated message", updatedComment.message());
    }

    private Comment createComment(Long id, State state) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setCreationDate(LocalDateTime.now());
        comment.setMessage("Reisezeiten eintragen!");
        comment.setState(state);
        comment.setUpdatedDate(LocalDateTime.now());
        comment.setStepEntry(createStepEntry(1L));
        return comment;
    }

    private Employee createEmployee() {
        return Employee.builder()
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .releaseDate(LocalDate.now().toString())
                .firstName("Thomas")
                .build();
    }

    private StepEntry createStepEntry(Long id) {
        StepEntry stepEntry = new StepEntry();
        stepEntry.setId(id);
        stepEntry.setCreationDate(LocalDateTime.now());
        stepEntry.setDate(LocalDate.now());
        stepEntry.setProject("Liwest-EMS");
        stepEntry.setState(State.IN_PROGRESS);
        stepEntry.setUpdatedDate(LocalDateTime.now());
        stepEntry.setAssignee(createUser());
        return stepEntry;
    }

    private User createUser() {
        User user = new User();
        user.setEmail("christoph.ruhsam@gepardec.com");
        user.setFirstname("Christoph");
        return user;
    }
}
