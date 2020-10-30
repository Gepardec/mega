package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.Comment;
import com.gepardec.mega.db.entity.State;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {

    public List<Comment> findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail(LocalDate startDate, LocalDate endDate, String email) {
        return find("SELECT c FROM Comment c WHERE c.stepEntry.owner.email = :email AND ((c.stepEntry.date BETWEEN :start AND :end) OR (c.stepEntry.date < :start AND c.state = :state))",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("state", State.OPEN)
                        .and("email", email))
                .list();
    }

    @Transactional
    public int setStatusDone(Long id) {
        return update("UPDATE Comment c SET c.state = :state WHERE id = :id",
                Parameters
                        .with("id", id)
                        .and("state", State.DONE));
    }
}
