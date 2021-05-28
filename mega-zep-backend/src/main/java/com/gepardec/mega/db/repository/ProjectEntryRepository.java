package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.project.ProjectEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ProjectEntryRepository implements PanacheRepository<ProjectEntry> {

    @Inject
    EntityManager em;

    public List<ProjectEntry> findByNameAndDate(String projectName, LocalDate from, LocalDate to) {
        return find("#ProjectEntry.findAllProjectEntriesForProjectNameInRange",
                Parameters
                        .with("projectName", projectName)
                        .and("start", from)
                        .and("end", to))
                .list();
    }
}
