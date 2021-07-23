package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.monthlyreport.MonthlyReportService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequestScoped
@Secured
@Path("/worker")
@RolesAllowed(Role.EMPLOYEE)
public class WorkerResource {

    @Inject
    MonthlyReportService monthlyReportService;

    @Inject
    UserContext userContext;

    @Inject
    EmployeeService employeeService;

    @Inject
    StepEntryService stepEntryService;

    @GET
    @Path("/monthendreports")
    @Produces(MediaType.APPLICATION_JSON)
    public MonthlyReport monthlyReport() {
        Employee employee = employeeService.getEmployee(Objects.requireNonNull(userContext.user()).userId());
        MonthlyReport monthlyReport = monthlyReportService.getMonthendReportForUser(Objects.requireNonNull(employee).userId());

        if (monthlyReport == null) {
            monthlyReport = MonthlyReport.builder()
                    .employee(employee)
                    .timeWarnings(Collections.emptyList())
                    .journeyWarnings(Collections.emptyList())
                    .comments(Collections.emptyList())
                    .employeeCheckState(EmployeeState.OPEN)
                    .isAssigned(false)
                    .employeeProgresses(Collections.emptyList())
                    .otherChecksDone(false)
                    .billableTime(null)
                    .totalWorkingTime(null)
                    .compensatoryDays(0)
                    .homeofficeDays(0)
                    .vacationDays(0)
                    .build();
        }

        return monthlyReport;
    }

    @GET
    @Path("/getall")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAll() {
        return employeeService.getAll();
    }
}
