package com.gepardec.mega.rest.impl;

import com.gepardec.mega.annotations.Authorization;
import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.rest.api.WorkerApi;
import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.MitarbeiterType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unused")
@ApplicationScoped
public class WorkerImpl implements WorkerApi {

    @Inject
    WorkerService workerService;

    @Override
    public Response employee (final GoogleUser user, final HttpServletRequest request, final HttpServletResponse response) {
        final MitarbeiterType mitarbeiterType = workerService.getEmployee(user);
        if(mitarbeiterType != null) {
            return Response.ok(mitarbeiterType).build();
        }

        return Response.serverError().build();
    }

    @Override
    @Authorization(allowedRoles = {SessionUser.ROLE_ADMINISTRATOR, SessionUser.ROLE_CONTROLLER})
    public Response employees (final GoogleUser user, final HttpServletRequest request, final HttpServletResponse response) {
        final List<MitarbeiterType> mitarbeiterTypeList = workerService.getAllEmployees(user);
        if(mitarbeiterTypeList != null) {
            return Response.ok(mitarbeiterTypeList).build();
        }

        return Response.serverError().build();
    }

    @Override
    @Authorization(allowedRoles = {SessionUser.ROLE_ADMINISTRATOR, SessionUser.ROLE_CONTROLLER})
    public Response employeesUpdate (final List<MitarbeiterType> employees, final HttpServletRequest request, final HttpServletResponse response) {
        return Response.status(workerService.updateEmployees(employees)).build();
    }

    @Override
    @Authorization(allowedRoles = {SessionUser.ROLE_ADMINISTRATOR, SessionUser.ROLE_CONTROLLER})
    public Response employeesUpdate (final MitarbeiterType employee, final HttpServletRequest request, final HttpServletResponse response) {
        return Response.status(workerService.updateEmployee(employee)).build();
    }
}
