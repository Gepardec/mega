package com.gepardec.mega.rest;

import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.aplication.security.RolesAllowed;
import com.gepardec.mega.aplication.security.Secured;
import com.gepardec.mega.aplication.security.SessionUser;
import com.gepardec.mega.rest.model.Employee;
import com.gepardec.mega.rest.model.MonthlyReport;
import com.gepardec.mega.rest.translator.EmployeeTranslator;
import com.gepardec.mega.rest.translator.MonthlyReportTranslator;
import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.MitarbeiterType;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestScoped
@Secured
@Path("/worker")
public class WorkerResource {

    @Inject
    WorkerService workerService;

    @Inject
    SessionUser sessionUser;

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
    @GET
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployees() {
        final List<MitarbeiterType> mitarbeiterTypeList = workerService.getAllActiveEmployees();
        List<Employee> employees = mitarbeiterTypeList.stream()
                .map(EmployeeTranslator::toEmployee)
                .collect(Collectors.toList());

        return Response.ok(employees).build();
    }

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
    @PUT
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setEmployees(@NotEmpty(message = "{workerResource.employees.notEmpty}") final List<Employee> employees) {
        return Response.ok().entity(workerService.updateEmployeesReleaseDate(employees)).build();
    }

    @GET
    @Path("/monthendreports")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthEndReports() {
        com.gepardec.mega.monthlyreport.MonthlyReport monthlyReport = workerService.getMonthendReportForUser(sessionUser.getUserId());

        if (monthlyReport == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Employee employee = EmployeeTranslator.toEmployee(monthlyReport.getMitarbeiter());
        final MonthlyReport result = MonthlyReportTranslator.to(monthlyReport.getTimeWarnings(), monthlyReport.getJourneyWarnings(), employee);
        return Response.ok(result).build();
    }
}