package com.gepardec.mega.rest;

import com.gepardec.mega.application.security.Secured;
import com.gepardec.mega.application.security.UserContext;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Secured
@Path("/worker")
public class WorkerResource {

    @Inject
    MonthlyReportService monthlyReportService;

    @Inject
    UserContext userContext;

    @GET
    @Path("/monthendreports")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthEndReports() {
        final MonthlyReport monthlyReport = monthlyReportService.getMonthendReportForUser(userContext.getUser().userId());

        if (monthlyReport == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(monthlyReport).build();
    }
}