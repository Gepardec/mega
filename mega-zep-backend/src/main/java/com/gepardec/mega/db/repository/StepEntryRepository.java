package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.StepEntry;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StepEntryRepository implements PanacheRepository<StepEntry> {
}
