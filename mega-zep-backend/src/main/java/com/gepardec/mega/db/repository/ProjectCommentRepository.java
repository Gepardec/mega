package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.project.ProjectComment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@Transactional
public class ProjectCommentRepository implements PanacheRepository<ProjectComment> {

    @Inject
    EntityManager em;

    @Inject
    Logger logger;

    public List<ProjectComment> findByProjectNameAndEntryDateBetween(String projectName, LocalDate from, LocalDate to) {
        return find("#ProjectComment.findByProjectNameAndEntryDateBetween",
                Parameters
                        .with("projectName", projectName)
                        .and("start", from)
                        .and("end", to))
                .list();
    }

    public boolean update(ProjectComment comment) {
        try {
            em.merge(comment);
            return true;
        } catch (Exception exception) {
            logger.error("An exception occurred during updating enterprise entry", exception);
            return false;
        }
    }

    public ProjectComment save(ProjectComment projectComment) {
        this.persist(projectComment);
        return projectComment;
    }
}
