package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.db.entity.enterprise.EnterpriseEntry;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
@TestTransaction
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

        Optional<EnterpriseEntry> enterpriseEntryValue = enterpriseEntryRepository.findByDate(LocalDate.now());

        assertAll(
                () -> assertThat(enterpriseEntryValue).isPresent(),
                () -> assertThat(enterpriseEntryValue.get().getZepTimesReleased()).isEqualTo(State.OPEN)
        );
    }
}
