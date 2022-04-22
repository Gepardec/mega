package com.gepardec.mega.service.impl.comment;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.db.repository.CommentRepository;
import com.gepardec.mega.domain.mapper.CommentMapper;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.notification.mail.Mail;
import com.gepardec.mega.notification.mail.MailParameter;
import com.gepardec.mega.notification.mail.MailSender;
import com.gepardec.mega.service.api.CommentService;
import com.gepardec.mega.service.api.StepEntryService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        LocalDate fromDate = DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate());
        LocalDate toDate = DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate());
        List<Comment> domainComments = commentService.findCommentsForEmployee(
                employee, fromDate, toDate
        );
        assertThat(domainComments).hasSize(1);
        assertThat(domainComments.get(0).getId()).isEqualTo(1);
    }

    @Test
    void setDone_whenNoneUpdated_then0() {
        when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(createComment(1L, EmployeeState.IN_PROGRESS));
        when(commentRepository.setStatusDone(ArgumentMatchers.anyLong())).thenReturn(0);

        int updatedCount = commentService.setDone(commentMapper.mapDbCommentToDomainComment(createComment(1L, EmployeeState.IN_PROGRESS)));
        assertThat(updatedCount).isZero();
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenEmployeeIsNull_thenThrowsException() {
        assertThatThrownBy(() -> commentService.cntFinishedAndTotalCommentsForEmployee(null, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Employee must not be null!");
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenFromDateIsNull_thenThrowsException() {
        Employee employee = Employee.builder()
                .userId("1")
                .email("max.mustermann@gpeardec.com")
                .releaseDate(null)
                .firstname("Max")
                .build();

        assertThatThrownBy(() -> commentService.cntFinishedAndTotalCommentsForEmployee(employee, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("From date must not be null!");
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenToDateIsNull_thenThrowsException() {
        Employee employee = Employee.builder()
                .userId("1")
                .email("max.mustermann@gpeardec.com")
                .releaseDate(null)
                .firstname("Max")
                .build();

        assertThatThrownBy(() -> commentService.cntFinishedAndTotalCommentsForEmployee(employee, LocalDate.now(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("To date must not be null!");
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
        LocalDate fromDate = DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate());
        LocalDate toDate = DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate());
        FinishedAndTotalComments result = commentService.cntFinishedAndTotalCommentsForEmployee(employee, fromDate, toDate);
        assertThat(result).isNotNull();
        assertThat(result.getTotalComments()).isEqualTo(3L);
        assertThat(result.getFinishedComments()).isEqualTo(1L);
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
        LocalDate fromDate = DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate());
        LocalDate toDate = DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate());
        FinishedAndTotalComments result = commentService.cntFinishedAndTotalCommentsForEmployee(employee, fromDate, toDate);
        assertThat(result).isNotNull();
        assertThat(result.getTotalComments()).isEqualTo(3L);
        assertThat(result.getFinishedComments()).isZero();
    }

    @Test
    void cntFinishedAndTotalCommentsForEmployee_whenNoComments_thenReturnsCnt() {
        when(commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.anyString()
        )).thenReturn(List.of());

        Employee employee = createEmployee();
        LocalDate fromDate = DateUtils.getFirstDayOfFollowingMonth(employee.getReleaseDate());
        LocalDate toDate = DateUtils.getLastDayOfFollowingMonth(employee.getReleaseDate());
        FinishedAndTotalComments result = commentService.cntFinishedAndTotalCommentsForEmployee(employee, fromDate, toDate);
        assertThat(result).isNotNull();
        assertThat(result.getTotalComments()).isZero();
        assertThat(result.getFinishedComments()).isZero();
    }

    @Test
    void createNewCommentForEmployee_whenEmployeeIsNull_thenThrowsException() {
        assertThatThrownBy(() -> commentService.cntFinishedAndTotalCommentsForEmployee(null, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Employee must not be null!");
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
            ((com.gepardec.mega.db.entity.employee.Comment) args[0]).setUpdatedDate(LocalDateTime.now());
            ((com.gepardec.mega.db.entity.employee.Comment) args[0]).setState(EmployeeState.OPEN);
            return args[0];
        }).when(commentRepository).save(ArgumentMatchers.any(com.gepardec.mega.db.entity.employee.Comment.class));

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
        Comment createdComment = commentService.createNewCommentForEmployee(
                2L, employee, newComment, "", null, LocalDate.now().toString()
        );

        String creator = stepEntry.getAssignee().getFirstname();
        Map<String, String> expectedMailParameter = Map.of(
                MailParameter.CREATOR, creator,
                MailParameter.RECIPIENT, employee.getFirstname(),
                MailParameter.COMMENT, newComment
        );

        verify(mailSender, times(1)).send(
                Mail.COMMENT_CREATED, employee.getEmail(), employee.getFirstname(), Locale.GERMAN, expectedMailParameter, List.of(creator)
        );

        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getMessage()).isEqualTo("My new comment!");
        assertThat(createdComment.getAuthorEmail()).isEqualTo(stepEntry.getAssignee().getEmail());
        assertThat(createdComment.getState()).isEqualTo(EmployeeState.OPEN);
    }

    @Test
    void updateComment_whenEntityNotFound_thenThrowsException() {
        when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(null);

        assertThatThrownBy(() -> commentService.updateComment(1L, "My message!"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("No entity found for id = 1");
    }

    @Test
    void updateComment_whenValid_thenReturnUpdatedComment() {
        com.gepardec.mega.db.entity.employee.Comment originalComment = createComment(1L, EmployeeState.DONE);
        when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(originalComment);
        when(commentRepository.update(ArgumentMatchers.any(com.gepardec.mega.db.entity.employee.Comment.class))).thenReturn(null);

        Comment updatedComment = commentService.updateComment(1L, "Updated message");
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getMessage()).isEqualTo("Updated message");
    }

    private com.gepardec.mega.db.entity.employee.Comment createComment(Long id, EmployeeState employeeState) {
        com.gepardec.mega.db.entity.employee.Comment comment = new com.gepardec.mega.db.entity.employee.Comment();
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
                .email("max.mustermann@gpeardec.com")
                .releaseDate(LocalDate.now().toString())
                .firstname("Max")
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
        user.setEmail("max.mustermann@gpeardec.com");
        user.setFirstname("Max");
        return user;
    }
}
