package com.gepardec.mega.service.impl.enterpriseentry;

import com.gepardec.mega.db.entity.enterprise.EnterpriseEntry;
import com.gepardec.mega.rest.model.EnterpriseEntryDto;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class EnterpriseEntryMapper {

    public EnterpriseEntryDto map(final Optional<EnterpriseEntry> enterpriseEntry) {
        if (enterpriseEntry.isEmpty()) {
            return null;
        }
        return EnterpriseEntryDto.builder()
                .creationDate(enterpriseEntry.get().getCreationDate())
                .date(enterpriseEntry.get().getDate())
                .chargeabilityExternalEmployeesRecorded(enterpriseEntry.get().getChargeabilityExternalEmployeesRecorded())
                .payrollAccountingSent(enterpriseEntry.get().getPayrollAccountingSent())
                .zepMonthlyReportDone(enterpriseEntry.get().getZepMonthlyReportDone())
                .zepTimesReleased(enterpriseEntry.get().getZepTimesReleased())
                .build();
    }
}
