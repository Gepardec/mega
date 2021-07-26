package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.db.entity.enterprise.EnterpriseEntry;
import com.gepardec.mega.db.repository.EnterpriseEntryRepository;
import com.gepardec.mega.service.api.init.EnterpriseSyncService;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.service.api.user.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Dependent
@Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
public class EnterpriseSyncServiceImpl implements EnterpriseSyncService {

    @Inject
    Logger logger;

    @Inject
    UserService userService;

    @Inject
    ProjectService projectService;

    @Inject
    EnterpriseEntryRepository enterpriseEntryRepository;

    @Override
    public boolean generateEnterpriseEntries() {
        return generateEnterpriseEntries(LocalDate.now().minusMonths(1).withDayOfMonth(1));
    }

    @Override
    public boolean generateEnterpriseEntries(LocalDate date) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        logger.info("Started enterprise entry generation: {}", Instant.ofEpochMilli(stopWatch.getStartTime()));

        date = date.withDayOfMonth(1);
        logger.info("Processing date: {}", date);

        Optional<EnterpriseEntry> savedEnterpriseEntry = enterpriseEntryRepository.findByDate(date);

        if (savedEnterpriseEntry.isEmpty()) {
            EnterpriseEntry enterpriseEntry = new EnterpriseEntry();
            enterpriseEntry.setDate(date);
            enterpriseEntry.setCreationDate(LocalDateTime.now());
            enterpriseEntry.setChargeabilityExternalEmployeesRecorded(State.OPEN);
            enterpriseEntry.setPayrollAccountingSent(State.OPEN);
            enterpriseEntry.setZepMonthlyReportDone(State.OPEN);
            enterpriseEntry.setZepTimesReleased(State.OPEN);

            enterpriseEntryRepository.persist(enterpriseEntry);
        } else {
            logger.debug("Enterprise entry for month {} already exists.", date.getMonth());
        }

        stopWatch.stop();

        logger.info("Enterprise entry generation took: {}ms", stopWatch.getTime());
        logger.info("Finished enterprise entry generation: {}", Instant.ofEpochMilli(stopWatch.getStartTime() + stopWatch.getTime()));

        return enterpriseEntryRepository.findByDate(date).isPresent();
    }
}
