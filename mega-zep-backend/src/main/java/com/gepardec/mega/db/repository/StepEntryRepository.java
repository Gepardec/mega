package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.StepEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class StepEntryRepository implements PanacheRepository<StepEntry> {

//    @Inject
//    EntityManager em;

    public List<StepEntry> findAllStepEntriesBetweenFromDateAndToDateWhereAssigneeEmailAndOwnerEmailEqualEmail(LocalDate fromDate, LocalDate toDate, String email) {
        return find("SELECT s FROM StepEntry s WHERE s.date BETWEEN ?1 AND ?2 AND s.owner.email = ?3 AND s.assignee.email = ?4", fromDate, toDate, email, email)
                .list();
    }

    public List<StepEntry> findAllStepEntriesBetweenFromDateAndToDateWhereOwnerEmailEqualsEmailAndDoesNotEqualAssigneeEmail(LocalDate fromDate, LocalDate toDate, String email) {
        return find("SELECT s FROM StepEntry s WHERE s.date BETWEEN ?1 AND ?2 AND s.owner.email = ?3 AND s.owner.email <> s.assignee.email", fromDate, toDate, email)
                .list();
    }

//    public List<StepEntry> findAllStepEntriesBetweenFromDateAndToDateWhereAssigneeEmailAndOwnerEmailEqualEmail(final LocalDate fromDate, final LocalDate toDate, final String email) {
//        return em.createQuery("SELECT s FROM StepEntry s WHERE s.date BETWEEN :a1 AND :a2 AND s.owner.email = :a3 AND s.assignee.email = :a4", StepEntry.class)
//                .setParameter("a1", fromDate)
//                .setParameter("a2", toDate)
//                .setParameter("a3", email)
//                .setParameter("a4", email)
//                .getResultList();
//    }
//
//    public List<StepEntry> findAllStepEntriesBetweenFromDateAndToDateWhereOwnerEmailEqualsEmailAndDoesNotEqualAssigneeEmail(final LocalDate fromDate, final LocalDate toDate, final String email) {
//        return em.createQuery("SELECT s FROM StepEntry s WHERE s.date BETWEEN :a1 AND :a2 AND s.owner.email = :a3 AND s.owner.email <> s.assignee.email", StepEntry.class)
//                .setParameter("a1", fromDate)
//                .setParameter("a2", toDate)
//                .setParameter("a3", email)
//                .getResultList();
//    }

}
