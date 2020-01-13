package com.gepardec.mega.rest.impl;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.rest.Employee;
import com.gepardec.mega.rest.EmployeeTranslator;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Secured
public class WorkerResource implements WorkerApi {

    @Inject
    WorkerService workerService;

    @Inject
    SessionUser sessionUser;

    @Override
    public Response employee(@NotEmpty(message = "workerResource.email.notEmpty") final String email) {
        final Employee employee = EmployeeTranslator.toEmployee(workerService.getEmployee(email));
        if (employee != null) {
            return Response.ok(employee).build();
        }
        return Response.serverError().build();
    }

    @Override
    @RolesAllowed(allowedRoles = {Role.USER, Role.ADMINISTRATOR})
    public Response employeeMonthendReport(@NotEmpty(message = "workerResource.email.notEmpty") final String eMail) {
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
                .map(mitarbeiterType -> EmployeeTranslator.toEmployee(mitarbeiterType))
                .collect(Collectors.toList());

        if (mitarbeiterTypeList != null) {
            return Response.ok(employees).build();
        }
        return Response.serverError().build();
    }


    @Override
    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR})
    public Response employeesUpdate(@NotEmpty(message = "{workerResource.employees.notEmpty}") final List<Employee> employees) {
        List<Pair<String, String>> pairsMailReleaseDate = employees.stream()
                .map(this::toPair)
                .collect(Collectors.toList());
        return Response.status(workerService.updateEmployeesReleaseDate(pairsMailReleaseDate)).build();
    }

    @Override
    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.USER})
    public Response employeeUpdate(@NotNull(message = "{workerResource.employee.notNull}") final Employee employee) {
        if (Role.USER.equals(sessionUser.getRole()) && !sessionUser.getEmail().equals(employee.getEMail())) {
            throw new SecurityException("User with userrole can not update other users");
        }
        return Response.status(workerService.updateEmployeeReleaseDate(employee.getEMail(), employee.getReleaseDate()))
                .build();
    }

    private Pair<String, String> toPair(Employee employee) {
        return Pair.of(employee.getEMail(), employee.getReleaseDate());
    }
}