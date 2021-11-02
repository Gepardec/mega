package com.gepardec.mega.service.impl.enterpriseentry;

import com.gepardec.mega.db.entity.enterprise.EnterpriseEntry;
import com.gepardec.mega.domain.model.ProjectState;
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
                .chargeabilityExternalEmployeesRecorded(ProjectState.byName(enterpriseEntry.get().getChargeabilityExternalEmployeesRecorded().name()))
                .payrollAccountingSent(ProjectState.byName(enterpriseEntry.get().getPayrollAccountingSent().name()))
                .zepMonthlyReportDone(ProjectState.byName(enterpriseEntry.get().getZepMonthlyReportDone().name()))
                .zepTimesReleased(ProjectState.byName(enterpriseEntry.get().getZepTimesReleased().name()))
                .build();
    }
}
