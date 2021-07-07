package com.gepardec.mega.service.impl.enterpriseentry;

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
}
