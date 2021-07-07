package com.gepardec.mega.service.impl.enterpriseentry;

import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.db.repository.EnterpriseEntryRepository;
import com.gepardec.mega.rest.model.EnterpriseEntryDto;
import com.gepardec.mega.service.api.enterpriseentry.EnterpriseEntryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;

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
    public boolean setZepTimesReleased(final State state, final LocalDate from, final LocalDate to) {
        return enterpriseEntryRepository.setZepTimesReleased(state, from, to) == 1;
    }

    @Override
    public boolean setChargeabilityExternalEmployeesRecorded(final State state, final LocalDate from, final LocalDate to) {
        return enterpriseEntryRepository.setChargeabilityExternalEmployeesRecorded(state, from, to) == 1;
    }

    @Override
    public boolean setPayrollAccountingSent(final State state, final LocalDate from, final LocalDate to) {
        return enterpriseEntryRepository.setPayrollAccountingSent(state, from, to) == 1;
    }

    @Override
    public boolean zepMonthlyReportDone(final State state, final LocalDate from, final LocalDate to) {
        return enterpriseEntryRepository.zepMonthlyReportDone(state, from, to) == 1;
    }
}
