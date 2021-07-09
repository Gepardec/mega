package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.enterprise.EnterpriseEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class EnterpriseEntryRepository implements PanacheRepository<EnterpriseEntry> {

    @Inject
    EntityManager em;

    public Optional<EnterpriseEntry> findByDate(LocalDate from, LocalDate to) {
        return find("#EnterpriseEntry.findByDate",
                Parameters.with("start", from)
                        .and("end", to))
                .singleResultOptional();
    }

    public Optional<EnterpriseEntry> findByDate(LocalDate date) {
        return find("date", date).firstResultOptional();
    }

    public boolean updateEntry(EnterpriseEntry entry) {
        try {
            em.merge(entry);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
