package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.enterprise.EnterpriseEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class EnterpriseEntryRepository implements PanacheRepository<EnterpriseEntry> {

    public Optional<EnterpriseEntry> findByDate(LocalDate date) {
        return find("date", date).firstResultOptional();
    }

}
