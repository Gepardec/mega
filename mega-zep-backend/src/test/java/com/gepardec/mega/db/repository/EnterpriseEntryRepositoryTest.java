package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.db.entity.enterprise.EnterpriseEntry;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@QuarkusTest
class EnterpriseEntryRepositoryTest {

    @Inject
    EnterpriseEntryRepository enterpriseEntryRepository;

    @Test
    void generateEnterpriseEntry() {
        EnterpriseEntry enterpriseEntry = new EnterpriseEntry();
        enterpriseEntry.setDate(LocalDate.now());
        enterpriseEntry.setCreationDate(LocalDateTime.now());
        enterpriseEntry.setChargeabilityExternalEmployeesRecorded(State.OPEN);
        enterpriseEntry.setPayrollAccountingSent(State.OPEN);
        enterpriseEntry.setZepMonthlyReportDone(State.OPEN);
        enterpriseEntry.setZepTimesReleased(State.OPEN);

        enterpriseEntryRepository.persist(enterpriseEntry);
    }
}