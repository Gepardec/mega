package com.gepardec.mega.rest.impl;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.rest.api.WorkerApi;
import com.gepardec.mega.security.Authorization;
import com.gepardec.mega.security.Role;
import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.MitarbeiterType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
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
    @Authorization(allowedRoles = {Role.USER})
    public Response employeeMonthendReport(GoogleUser user) {
        sessionUser.checkForSameUser(user.getEmail());
        final MonthlyReport monthlyReport = workerService.getMonthendReportForUser(user);
        if (monthlyReport != null) {
            return Response.ok(monthlyReport).build();
        }
        return Response.serverError().build();
    }


    @Override
    @Authorization(allowedRoles = {Role.ADMINISTRATOR})
    public Response employees() {
        final List<MitarbeiterType> mitarbeiterTypeList = workerService.getAllActiveEmployees();
        if (mitarbeiterTypeList != null) {
            return Response.ok(mitarbeiterTypeList).build();
        }
        return Response.serverError().build();
    }


    @Override
    @Authorization(allowedRoles = {Role.ADMINISTRATOR})
    public Response employeesUpdate(final List<MitarbeiterType> employees) {
        return Response.status(workerService.updateEmployees(employees)).build();
    }


    @Override
    @Authorization(allowedRoles = {Role.ADMINISTRATOR})
    public Response employeeUpdate(final MitarbeiterType employee) {
        sessionUser.checkForSameUser(employee.getEmail());
        return Response.status(workerService.updateEmployee(employee))
                .build();
    }
}