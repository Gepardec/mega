package com.gepardec.mega.service.impl.enterpriseentry;

import com.gepardec.mega.db.entity.enterprise.EnterpriseEntry;
import com.gepardec.mega.db.repository.EnterpriseEntryRepository;
import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.rest.model.EnterpriseEntryDto;
import com.gepardec.mega.service.api.enterpriseentry.EnterpriseEntryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class EnterpriseEntryServiceImpl implements EnterpriseEntryService {

    @Inject
    EnterpriseEntryMapper enterpriseEntryMapper;

    @Inject
    EnterpriseEntryRepository enterpriseEntryRepository;

    @Override
    public EnterpriseEntryDto findByDate(LocalDate from, LocalDate to) {
        return enterpriseEntryMapper.map(
                enterpriseEntryRepository.findByDate(from, to)
        );
    }

    @Override
    public boolean update(EnterpriseEntryDto updatedEntryDto, LocalDate from, LocalDate to) {
        Optional<EnterpriseEntry> optionalEntry = enterpriseEntryRepository.findByDate(from, to);

        if (optionalEntry.isPresent()) {
            EnterpriseEntry entry = optionalEntry.get();
            entry.setChargeabilityExternalEmployeesRecorded(State.valueOf(updatedEntryDto.chargeabilityExternalEmployeesRecorded().name()));
            entry.setPayrollAccountingSent(State.valueOf(updatedEntryDto.payrollAccountingSent().name()));
            entry.setZepMonthlyReportDone(State.valueOf(updatedEntryDto.zepMonthlyReportDone().name()));
            entry.setZepTimesReleased(State.valueOf(updatedEntryDto.zepTimesReleased().name()));

            return enterpriseEntryRepository.updateEntry(entry);
        }
        return false;
    }
}
