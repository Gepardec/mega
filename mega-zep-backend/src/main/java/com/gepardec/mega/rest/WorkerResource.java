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
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequestScoped
@RolesAllowed(Role.EMPLOYEE)
@Secured
public class WorkerResource implements WorkerResourceAPI {

    @Inject
    MonthlyReportService monthlyReportService;

    @Inject
    UserContext userContext;

    @Inject
    EmployeeService employeeService;

    @Inject
    StepEntryService stepEntryService;

    @Override
    public MonthlyReport monthlyReport() {
        Employee employee = employeeService.getEmployee(Objects.requireNonNull(userContext.user()).userId());
        LocalDate date = LocalDate.parse(employee.releaseDate()).with(TemporalAdjusters.firstDayOfNextMonth());
        return monthlyReport(date.getYear(), date.getMonthValue());
    }

    @Override
    public MonthlyReport monthlyReport(Integer year, Integer month) {
        LocalDate date = LocalDate.of(year, month, 1);

        Employee employee = employeeService.getEmployee(Objects.requireNonNull(userContext.user()).userId());

        MonthlyReport monthlyReport = monthlyReportService.getMonthendReportForUser(Objects.requireNonNull(employee).userId(), date);

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
                    .build();
        }

        return monthlyReport;
    }

    @Override
    public List<User> getAll() {
        return employeeService.getAll();
    }

}
