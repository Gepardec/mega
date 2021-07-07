package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.rest.model.EnterpriseEntryDto;
import com.gepardec.mega.service.api.enterpriseentry.EnterpriseEntryService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    @Path("/zeptimesreleased/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean setZepTimesReleased(@PathParam("year") Integer year, @PathParam("month") Integer month,
                                       @QueryParam("state") State state) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        return enterpriseEntryService.setZepTimesReleased(state, from, to);
    }

    @PUT
    @Path("/chargeabilityexternalemployeesrecorded/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean setChargeabilityExternalEmployeesRecorded(@PathParam("year") Integer year, @PathParam("month") Integer month,
                                                             @QueryParam("state") State state) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        return enterpriseEntryService.setChargeabilityExternalEmployeesRecorded(state, from, to);
    }

    @PUT
    @Path("/payrollaccountingsent/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean setPayrollAccountingSent(@PathParam("year") Integer year, @PathParam("month") Integer month,
                                            @QueryParam("state") State state) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        return enterpriseEntryService.setPayrollAccountingSent(state, from, to);
    }

    @PUT
    @Path("/zepmonthlyreportdone/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean setZepMonthlyReportDone(@PathParam("year") Integer year, @PathParam("month") Integer month,
                                           @QueryParam("state") State state) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        return enterpriseEntryService.zepMonthlyReportDone(state, from, to);
    }
}
