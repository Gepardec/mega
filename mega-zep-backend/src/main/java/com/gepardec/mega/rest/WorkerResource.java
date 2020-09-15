package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

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
    public MonthlyReport monthlyReport() {
        Employee employee = employeeService.getEmployee(userContext.user().userId());
        MonthlyReport monthlyReport = monthlyReportService.getMonthendReportForUser(employee.userId());

        if (monthlyReport == null) {
            List empty = Collections.emptyList();
            monthlyReport = MonthlyReport.of(employee, empty, empty, empty, State.OPEN, false);
        }

        return monthlyReport;
    }
}
