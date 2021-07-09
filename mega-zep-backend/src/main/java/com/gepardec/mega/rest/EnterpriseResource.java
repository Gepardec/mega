package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.rest.model.EnterpriseEntryDto;
import com.gepardec.mega.service.api.enterpriseentry.EnterpriseEntryService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Secured
@RolesAllowed(value = {Role.PROJECT_LEAD, Role.OFFICE_MANAGEMENT})
@Path("/enterprise")
public class EnterpriseResource {

    @Inject
    EnterpriseEntryService enterpriseEntryService;

    @GET
    @Path("/entriesformonthyear/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public EnterpriseEntryDto getEnterpriseEntryForMonthYear(@PathParam("year") Integer year, @PathParam("month") Integer month) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        return enterpriseEntryService.findByDate(from, to);
    }

    @PUT
    @Path("/entry/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updateEnterpriseEntry(@PathParam("year") Integer year, @PathParam("month") Integer month, @RequestBody final EnterpriseEntryDto entryDto) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        return enterpriseEntryService.update(entryDto, from, to);
    }
}
