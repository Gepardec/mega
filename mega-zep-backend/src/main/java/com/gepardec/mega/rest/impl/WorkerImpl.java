package com.gepardec.mega.rest.impl;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.rest.api.WorkerApi;
import com.gepardec.mega.security.RolesAllowed;
import com.gepardec.mega.security.Role;
import com.gepardec.mega.security.Secured;
import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.MitarbeiterType;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Secured
public class WorkerImpl implements WorkerApi {

    @Inject
    WorkerService workerService;

    @Inject
    SessionUser sessionUser;

    @Override
    public Response employee(final GoogleUser user) {
        final MitarbeiterType mitarbeiterType = workerService.getEmployee(user);
        if (mitarbeiterType != null) {
            return Response.ok(mitarbeiterType).build();
        }
        return Response.serverError().build();
    }

    @Override
    //TODO: add restriction for user
    public Response employeeMonthendReport(GoogleUser user) {
        //TODO: check if user = sessionuser
        final MonthlyReport monthlyReport = workerService.getMonthendReportForUser(user);
        if (monthlyReport != null) {
            return Response.ok(monthlyReport).build();
        }
        return Response.serverError().build();
    }


    @Override
    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR})
    public Response employees() {
        final List<MitarbeiterType> mitarbeiterTypeList = workerService.getAllActiveEmployees();
        if (mitarbeiterTypeList != null) {
            return Response.ok(mitarbeiterTypeList).build();
        }
        return Response.serverError().build();
    }


    @Override
    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR})
    public Response employeesUpdate(final List<MitarbeiterType> employees) {
        return Response.status(workerService.updateEmployees(employees)).build();
    }


    @Override
    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.USER})
    public Response employeeUpdate(final MitarbeiterType employee) {
        if (Role.USER.equals(sessionUser.getRole()) && !sessionUser.getEmail().equals(employee.getEmail())) {
            throw new SecurityException("User with userrole can not update other users");
        }
        return Response.status(workerService.updateEmployee(employee))
                .build();
    }
}