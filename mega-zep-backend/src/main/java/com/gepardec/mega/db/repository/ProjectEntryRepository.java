package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.project.ProjectEntry;
import com.gepardec.mega.db.entity.project.ProjectStep;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ProjectEntryRepository implements PanacheRepository<ProjectEntry> {

    @Inject
    EntityManager em;

    @Inject
    UserTransaction tx;

    public List<ProjectEntry> findByNameAndDate(String projectName, LocalDate from, LocalDate to) {
        return find("#ProjectEntry.findAllProjectEntriesForProjectNameInRange",
                Parameters
                        .with("projectName", projectName)
                        .and("start", from)
                        .and("end", to))
                .list();
    }

    public boolean updateProjectEntry(ProjectEntry projectEntity) {
        try {
            tx.begin();
            em.merge(projectEntity);
            tx.commit();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public ProjectEntry findByNameAndEntryDateAndStep(String projectName, LocalDate entryDate, ProjectStep projectStep) {
        return find("#ProjectEntry.findProjectEntryByNameAndEntryDateAndStep",
                Parameters
                        .with("projectName", projectName)
                        .and("entryDate", entryDate)
                        .and("projectStep", projectStep))
                .firstResult();
    }
}
