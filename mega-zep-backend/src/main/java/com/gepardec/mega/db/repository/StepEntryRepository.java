package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.StepEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class StepEntryRepository implements PanacheRepository<StepEntry> {

    public List<StepEntry> findAllOwnedAndAssignedStepEntriesInRange(LocalDate startDate, LocalDate endDate, String ownerAndAssigneeEmail) {
        return find("SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.owner.email = :ownerEmail AND s.assignee.email = :assigneeEmail",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("ownerEmail", ownerAndAssigneeEmail)
                        .and("assigneeEmail", ownerAndAssigneeEmail))
                .list();
    }

    public List<StepEntry> findAllOwnedAndUnassignedStepEntriesInRange(LocalDate startDate, LocalDate endDate, String ownerEmail) {
        return find("SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.owner.email = :ownerEmail AND s.owner.email <> s.assignee.email",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("ownerEmail", ownerEmail))
                .list();
    }
}
