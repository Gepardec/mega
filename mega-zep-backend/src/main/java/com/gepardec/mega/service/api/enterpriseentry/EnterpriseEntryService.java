package com.gepardec.mega.service.api.enterpriseentry;

import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.rest.model.EnterpriseEntryDto;

import java.time.LocalDate;

public interface EnterpriseEntryService {

    EnterpriseEntryDto findByDate(final LocalDate from, final LocalDate to);

    boolean setZepTimesReleased(final State state, final LocalDate from, final LocalDate to);

    boolean setChargeabilityExternalEmployeesRecorded(final State state, final LocalDate from, final LocalDate to);

    boolean setPayrollAccountingSent(final State state, final LocalDate from, final LocalDate to);

    boolean zepMonthlyReportDone(final State state, final LocalDate from, final LocalDate to);
}
