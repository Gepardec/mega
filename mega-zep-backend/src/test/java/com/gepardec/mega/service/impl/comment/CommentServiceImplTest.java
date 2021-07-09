package com.gepardec.mega.service.impl.comment;

import com.gepardec.mega.db.entity.employee.Comment;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.db.repository.CommentRepository;
import com.gepardec.mega.domain.mapper.CommentMapper;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import com.gepardec.mega.domain.utils.DateUtils;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString())).thenReturn(List.of(createComment(1L, EmployeeState.OPEN)));

        Employee employee = createEmployee();
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));
        List<com.gepardec.mega.domain.model.Comment> domainComments = commentService.findCommentsForEmployee(
                employee, fromDate, toDate
        );
        Assertions.assertFalse(domainComments.isEmpty());
        Assertions.assertEquals(1, domainComments.size());
        Assertions.assertEquals(1, domainComments.get(0).id());
    }

    @Test
    void setDone_whenNoneUpdated_then0() {
        when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(createComment(1L, EmployeeState.IN_PROGRESS));
        when(commentRepository.setStatusDone(ArgumentMatchers.anyLong())).thenReturn(0);

        int updatedCount = commentService.setDone(commentMapper.mapDbCommentToDomainComment(createComment(1L, EmployeeState.IN_PROGRESS)));
        Assertions.assertEquals(0, updatedCount);
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenEmployeeIsNull_thenThrowsException() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> commentService.cntFinishedAndTotalCommentsForEmployee(null, null, null),
                "Expected NullpointerException was not thrown!"
        );

        assertEquals("Employee must not be null!", thrown.getMessage());
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenFromDateIsNull_thenThrowsException() {
        Employee empl = Employee.builder()
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .releaseDate(null)
                .firstname("Thomas")
                .build();

        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> commentService.cntFinishedAndTotalCommentsForEmployee(empl, null, null),
                "Expected NullpointerException was not thrown!"
        );

        assertEquals("From date must not be null!", thrown.getMessage());
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenToDateIsNull_thenThrowsException() {
        Employee empl = Employee.builder()
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .releaseDate(null)
                .firstname("Thomas")
                .build();

        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> commentService.cntFinishedAndTotalCommentsForEmployee(empl, LocalDate.now(), null),
                "Expected NullpointerException was not thrown!"
        );

        assertEquals("To date must not be null!", thrown.getMessage());
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenValid_thenReturnsCnt() {
        when(commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString()
        )).thenReturn(List.of(
                createComment(1L, EmployeeState.IN_PROGRESS),
                createComment(2L, EmployeeState.DONE),
                createComment(3L, EmployeeState.OPEN)
        ));

        Employee employee = createEmployee();
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));
        FinishedAndTotalComments result = commentService.cntFinishedAndTotalCommentsForEmployee(employee, fromDate, toDate);
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
                createComment(1L, EmployeeState.IN_PROGRESS),
                createComment(2L, EmployeeState.OPEN),
                createComment(3L, EmployeeState.OPEN)
        ));

        Employee employee = createEmployee();
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));
        FinishedAndTotalComments result = commentService.cntFinishedAndTotalCommentsForEmployee(employee, fromDate, toDate);
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
        )).thenReturn(List.of());

        Employee employee = createEmployee();
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));
        FinishedAndTotalComments result = commentService.cntFinishedAndTotalCommentsForEmployee(employee, fromDate, toDate);
        assertNotNull(result);
        assertEquals(0L, result.totalComments());
        assertEquals(0L, result.finishedComments());
    }

    @Test
    void createNewCommentForEmployee_whenEmployeeIsNull_thenThrowsException() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> commentService.cntFinishedAndTotalCommentsForEmployee(null, null, null),
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
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        )).thenReturn(createStepEntry(1L));

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ((Comment) args[0]).setUpdatedDate(LocalDateTime.now());
            ((Comment) args[0]).setState(EmployeeState.OPEN);
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
        com.gepardec.mega.domain.model.Comment createdComment = commentService.createNewCommentForEmployee(
                2L, employee, newComment, "", null, LocalDate.now().toString()
        );

        String creator = stepEntry.getAssignee().getFirstname();
        Map<String, String> expectedMailParameter = Map.of(
                MailParameter.CREATOR, creator,
                MailParameter.RECIPIENT, employee.firstname(),
                MailParameter.COMMENT, newComment
        );

        verify(mailSender, times(1)).send(
                Mail.COMMENT_CREATED, employee.email(), employee.firstname(), Locale.GERMAN, expectedMailParameter, List.of(creator)
        );

        assertNotNull(createdComment);
        assertEquals("My new comment!", createdComment.message());
        assertEquals(stepEntry.getAssignee().getEmail(), createdComment.authorEmail());
        assertEquals(EmployeeState.OPEN, createdComment.state());
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
        Comment originalComment = createComment(1L, EmployeeState.DONE);
        when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(originalComment);
        when(commentRepository.update(ArgumentMatchers.any(Comment.class))).thenReturn(null);

        com.gepardec.mega.domain.model.Comment updatedComment = commentService.updateComment(1L, "Updated message");
        assertNotNull(updatedComment);
        assertEquals("Updated message", updatedComment.message());
    }

    private Comment createComment(Long id, EmployeeState employeeState) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setCreationDate(LocalDateTime.now());
        comment.setMessage("Reisezeiten eintragen!");
        comment.setState(employeeState);
        comment.setUpdatedDate(LocalDateTime.now());
        comment.setStepEntry(createStepEntry(1L));
        return comment;
    }

    private Employee createEmployee() {
        return Employee.builder()
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .releaseDate(LocalDate.now().toString())
                .firstname("Thomas")
                .build();
    }

    private StepEntry createStepEntry(Long id) {
        StepEntry stepEntry = new StepEntry();
        stepEntry.setId(id);
        stepEntry.setCreationDate(LocalDateTime.now());
        stepEntry.setDate(LocalDate.now());
        stepEntry.setProject("Liwest-EMS");
        stepEntry.setState(EmployeeState.IN_PROGRESS);
        stepEntry.setUpdatedDate(LocalDateTime.now());
        stepEntry.setAssignee(createUser());
        stepEntry.setOwner(createUser());
        return stepEntry;
    }

    private User createUser() {
        User user = new User();
        user.setEmail("thomas.herzog@gpeardec.com");
        user.setFirstname("Thomas");
        return user;
    }
}
