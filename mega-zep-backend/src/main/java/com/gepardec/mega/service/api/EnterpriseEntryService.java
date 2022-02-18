package com.gepardec.mega.service.api;

import com.gepardec.mega.rest.model.EnterpriseEntryDto;

import java.time.LocalDate;

public interface EnterpriseEntryService {

    EnterpriseEntryDto findByDate(final LocalDate from, final LocalDate to);

    boolean update(final EnterpriseEntryDto updatedEntryDto, final LocalDate from, final LocalDate to);
}
