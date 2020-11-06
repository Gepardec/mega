package com.gepardec.mega.service.impl.comment;

import com.gepardec.mega.db.entity.Comment;
import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.db.entity.User;
import com.gepardec.mega.db.repository.CommentRepository;
import com.gepardec.mega.domain.mapper.CommentMapper;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.service.api.comment.CommentService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@QuarkusTest
class CommentServiceImplTest {

    @Inject
    CommentService commentService;

    @Inject
    CommentMapper commentMapper;

    @InjectMock
    private CommentRepository commentRepository;

    @Test
    void findCommentsForEmployee_when1DbComment_thenMap1DomainComment() {
        when(commentRepository.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(ArgumentMatchers.any(LocalDate.class),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString())).thenReturn(List.of(createComment(1L)));

        List<com.gepardec.mega.domain.model.Comment> domainComments = commentService.findCommentsForEmployee(createEmployee());
        Assertions.assertFalse(domainComments.isEmpty());
        Assertions.assertEquals(1, domainComments.size());
        Assertions.assertEquals(1, domainComments.get(0).id());
    }

    @Test
    void setDone_whenNoneUpdated_then0() {
        when(commentRepository.setStatusDone(ArgumentMatchers.anyLong())).thenReturn(0);

        int updatedCount = commentService.setDone(commentMapper.mapDbCommentToDomainComment(createComment(1L)));
        Assertions.assertEquals(0, updatedCount);
    }

    private Comment createComment(Long id) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setCreationDate(LocalDateTime.now());
        comment.setMessage("Reisezeiten eintragen!");
        comment.setState(State.IN_PROGRESS);
        comment.setUpdatedDate(LocalDateTime.now());
        comment.setStepEntry(createStepEntry(1L));
        return comment;
    }

    private Employee createEmployee() {
        return Employee.builder()
                .userId("1")
                .email("thomas.herzog@gpeardec.com")
                .releaseDate(LocalDate.now().toString())
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
        return user;
    }
}
