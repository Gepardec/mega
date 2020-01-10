package com.gepardec.mega.rest.impl;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.rest.Employee;
import com.gepardec.mega.rest.EmployeeAdapter;
import com.gepardec.mega.rest.api.WorkerApi;
import com.gepardec.mega.security.Role;
import com.gepardec.mega.security.RolesAllowed;
import com.gepardec.mega.security.Secured;
import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.MitarbeiterType;
import org.apache.commons.lang3.tuple.Pair;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestScoped
@Secured
public class WorkerImpl implements WorkerApi {

    @Inject
    WorkerService workerService;

    @Inject
    SessionUser sessionUser;

    @Override
    public Response employee(final String eMail) {
        final Optional<Employee> employee = EmployeeAdapter.toEmployee(workerService.getEmployee(eMail));
        if (employee.isPresent()) {
            return Response.ok(employee.get()).build();
        }
        return Response.serverError().build();
    }

    @Override
    @RolesAllowed(allowedRoles = {Role.USER, Role.ADMINISTRATOR})
    public Response employeeMonthendReport(final String eMail) {
        MonthlyReport monthlyReport;
        if (Role.ADMINISTRATOR.equals(sessionUser.getRole())) {
            monthlyReport = workerService.getMonthendReportForUser(eMail);
        } else {
            monthlyReport = workerService.getMonthendReportForUser(sessionUser.getEmail());
        }

        if (monthlyReport != null) {
            return Response.ok(monthlyReport).build();
        }
        return Response.serverError().build();
    }


    @Override
    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR})
    public Response employees() {
        final List<MitarbeiterType> mitarbeiterTypeList = workerService.getAllActiveEmployees();
        List<Employee> employees = mitarbeiterTypeList.stream()
                .map(mitarbeiterType -> EmployeeAdapter.toEmployee(mitarbeiterType).orElse(null))
                .collect(Collectors.toList());

        if (mitarbeiterTypeList != null) {
            return Response.ok(employees).build();
        }
        return Response.serverError().build();
    }


    @Override
    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR})
    public Response employeesUpdate(final List<Employee> employees) {
        List<Pair<String, String>> pairsMailReleaseDate = employees.stream()
                .map(this::toPair)
                .collect(Collectors.toList());
        return Response.status(workerService.updateEmployeesReleaseDate(pairsMailReleaseDate)).build();
    }


    private Pair<String, String> toPair(Employee employee) {
        return Pair.of(employee.getEMail(), employee.getReleaseDate());
    }


    @Override
    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.USER})
    public Response employeeUpdate(final Employee employee) {
        if (Role.USER.equals(sessionUser.getRole()) && !sessionUser.getEmail().equals(employee.getEMail())) {
            throw new SecurityException("User with userrole can not update other users");
        }
        return Response.status(workerService.updateEmployeeReleaseDate(employee.getEMail(), employee.getReleaseDate()))
                .build();
    }
}