package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.employee.Comment;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@Transactional
public class CommentRepository implements PanacheRepository<Comment> {

    @Inject
    EntityManager em;

    public List<Comment> findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(LocalDate startDate, LocalDate endDate, String email) {
        return find("#Comment.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("state", EmployeeState.OPEN)
                        .and("email", email))
                .list();
    }

    @Transactional
    public int setStatusDone(Long id) {
        return update("UPDATE Comment c SET c.employeeState = :state WHERE id = :id",
                Parameters
                        .with("id", id)
                        .and("state", EmployeeState.DONE));
    }

    @Transactional
    public Comment save(final Comment comment) {
        this.persist(comment);
        return comment;
    }

    @Transactional
    public Comment update(final Comment comment) {
        return em.merge(comment);
    }

    @Transactional
    public boolean deleteComment(Long id) {
        return deleteById(id);
    }
}
