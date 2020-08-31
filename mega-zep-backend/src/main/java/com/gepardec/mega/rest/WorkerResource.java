package com.gepardec.mega.rest;

import com.gepardec.mega.application.security.Secured;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

@RequestScoped
@Secured
@Path("/worker")
public class WorkerResource {

    @Inject
    MonthlyReportService monthlyReportService;

    @Inject
    UserContext userContext;

    @Inject
    EmployeeService employeeService;

    @GET
    @Path("/monthendreports")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthEndReports() {
        Employee employee = employeeService.getEmployee(userContext.user().userId());
        MonthlyReport monthlyReport = monthlyReportService.getMonthendReportForUser(employee.userId());

        if (monthlyReport == null) {
            monthlyReport = new MonthlyReport();
            monthlyReport.setEmployee(employee);
            monthlyReport.setJourneyWarnings(Collections.emptyList());
            monthlyReport.setTimeWarnings(Collections.emptyList());
        }

        return Response.ok(monthlyReport).build();
    }
}
