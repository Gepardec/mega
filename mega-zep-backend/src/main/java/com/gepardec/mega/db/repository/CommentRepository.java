package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.Comment;
import com.gepardec.mega.db.entity.State;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {

    public List<Comment> findAllCommentsBetweenFromDateAndToDateAndAllOpenCommentsBeforeFromDate(LocalDate fromDate, LocalDate toDate) {
        return find("SELECT c FROM Comment c WHERE (c.stepEntry.date BETWEEN ?1 AND ?2) OR (c.stepEntry.date < ?1 AND c.state = ?3)",
                fromDate, toDate, State.OPEN)
                .list();
    }

}
