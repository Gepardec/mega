package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.rest.model.EnterpriseEntryDto;
import com.gepardec.mega.service.api.enterpriseentry.EnterpriseEntryService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@ApplicationScoped
public class EnterpriseResource implements EnterpriseResourceAPI {

    @Inject
    EnterpriseEntryService enterpriseEntryService;

    @Override
    public EnterpriseEntryDto getEnterpriseEntryForMonthYear(Integer year, Integer month) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        return enterpriseEntryService.findByDate(from, to);
    }

    @Override
    public boolean updateEnterpriseEntry(Integer year, Integer month, final EnterpriseEntryDto entryDto) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        return enterpriseEntryService.update(entryDto, from, to);
    }
}
