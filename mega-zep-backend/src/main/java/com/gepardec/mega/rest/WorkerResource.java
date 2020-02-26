package com.gepardec.mega.rest;

import com.gepardec.mega.aplication.security.ForbiddenException;
import com.gepardec.mega.aplication.security.*;
import com.gepardec.mega.rest.model.Employee;
import com.gepardec.mega.rest.model.MonthlyReport;
import com.gepardec.mega.rest.translator.EmployeeTranslator;
import com.gepardec.mega.rest.translator.MonthlyReportTranslator;
import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.MitarbeiterType;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @POST
    @Path("/employee")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response employee(@NotEmpty(message = "{workerResource.email.notEmpty}") final String email) {
        final MitarbeiterType mitarbeiter = workerService.getEmployee(email);
        if (mitarbeiter == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiter);
        return Response.ok(employee).build();
    }

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
    @POST
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    public Response employees() {
        final List<MitarbeiterType> mitarbeiterTypeList = workerService.getAllActiveEmployees();
        List<Employee> employees = mitarbeiterTypeList.stream()
                .map(EmployeeTranslator::toEmployee)
                .collect(Collectors.toList());

        return Response.ok(employees).build();
    }

    @POST
    @Path("/employee/monthendReport")
    @Produces(MediaType.APPLICATION_JSON)
    public Response employeeMonthendReport() {
        com.gepardec.mega.monthlyreport.MonthlyReport monthlyReport = workerService.getMonthendReportForUser(sessionUser.getEmail());

        if (monthlyReport == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Employee employee = EmployeeTranslator.toEmployee(monthlyReport.getMitarbeiter());
        final MonthlyReport result = MonthlyReportTranslator.to(monthlyReport.getTimeWarnings(), monthlyReport.getJourneyWarnings(), employee);
        return Response.ok(result).build();
    }

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
    @PUT
    @Path("/employees/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response employeesUpdate(@NotEmpty(message = "{workerResource.employees.notEmpty}") final List<Employee> employees) {
        Map<String, String> emailReleaseDates = employees.stream()
                .collect(Collectors.toMap(Employee::getEmail, Employee::getReleaseDate));
        final List<String> failedEmails = workerService.updateEmployeesReleaseDate(emailReleaseDates);
        return Response.ok().entity(failedEmails).build();
    }

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.USER})
    @PUT
    @Path("/employee/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response employeeUpdate(@NotNull(message = "{workerResource.employee.notNull}") final Employee employee) {
        if (Role.USER.equals(sessionUser.getRole()) && !sessionUser.getEmail().equals(employee.getEmail())) {
            throw new ForbiddenException("User with role 'USER' can not update other users");
        }
        workerService.updateEmployeeReleaseDate(employee.getEmail(), employee.getReleaseDate());
        return Response.ok().build();
    }
}