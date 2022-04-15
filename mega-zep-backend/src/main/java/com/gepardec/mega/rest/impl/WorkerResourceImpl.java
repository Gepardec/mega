package com.gepardec.mega.rest.impl;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.rest.api.WorkerResource;
import com.gepardec.mega.rest.mapper.MapperManager;
import com.gepardec.mega.rest.model.MonthlyReportDto;
import com.gepardec.mega.service.api.EmployeeService;
import com.gepardec.mega.service.api.MonthlyReportService;
import com.gepardec.mega.service.api.StepEntryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.Objects;

@RequestScoped
@RolesAllowed(Role.EMPLOYEE)
@Secured
public class WorkerResourceImpl implements WorkerResource {

    @Inject
    MonthlyReportService monthlyReportService;

    @Inject
    UserContext userContext;

    @Inject
    EmployeeService employeeService;

    @Inject
    StepEntryService stepEntryService;

    @Inject
    MapperManager mapper;

    @Override
    public Response monthlyReport() {
        Employee employee = employeeService.getEmployee(Objects.requireNonNull(userContext.getUser()).getUserId());

        LocalDate date = LocalDate.parse(employee.getReleaseDate()).with(TemporalAdjusters.firstDayOfNextMonth());
        return monthlyReport(date.getYear(), date.getMonthValue());
    }

    @Override
    public Response monthlyReport(Integer year, Integer month) {
        LocalDate date = LocalDate.of(year, month, 1);

        Employee employee = employeeService.getEmployee(Objects.requireNonNull(userContext.getUser()).getUserId());

        MonthlyReport monthlyReport = monthlyReportService.getMonthendReportForUser(Objects.requireNonNull(employee).getUserId(), date);

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
                    .billableTime("00:00")
                    .totalWorkingTime("00:00")
                    .compensatoryDays(0)
                    .homeofficeDays(0)
                    .vacationDays(0)
                    .nursingDays(0)
                    .build();
        }

        return Response.ok(mapper.map(monthlyReport, MonthlyReportDto.class)).build();
    }
}
