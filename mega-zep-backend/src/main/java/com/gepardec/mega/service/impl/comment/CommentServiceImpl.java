package com.gepardec.mega.service.impl.comment;

import com.gepardec.mega.db.entity.Comment;
import com.gepardec.mega.db.repository.CommentRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.CommentDTO;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.api.comment.CommentService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CommentServiceImpl implements CommentService {

    @Inject
    CommentRepository commentRepository;

    @Override
    public List<CommentDTO> findCommentsForEmployee(Employee employee) {
        LocalDateTime fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate())).atTime(0, 0, 0);
        LocalDateTime toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate())).atTime(0, 0, 0);

        List<Comment> comments =
                commentRepository
                        .find("SELECT c " +
                                        "FROM Comment c " +
                                        "WHERE c.creationDate BETWEEN ?1 AND ?2 AND c.stepEntry.owner.email = ?3",
                                fromDate, toDate, employee.email())
                        .list();

        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();

            commentDTO.setAuthor(comment.getStepEntry().getAssignee().getEmail());
            commentDTO.setCreationDate(comment.getCreationDate().toString());
            commentDTO.setMessage(comment.getMessage());
            commentDTO.setState(comment.getState());

            commentDTOs.add(commentDTO);
        }

        return commentDTOs;
    }
}
