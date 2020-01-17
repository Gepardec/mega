package com.gepardec.mega.rest;

import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.rest.model.Employee;
import com.gepardec.mega.rest.translator.EmployeeTranslator;
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
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response employee(@NotEmpty(message = "{workerResource.email.notEmpty}") final String email) {
        final MitarbeiterType mitarbeiter = workerService.getEmployee(email);
        if (mitarbeiter != null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final Employee employee = EmployeeTranslator.toEmployee(mitarbeiter);
        return Response.ok(employee).build();
    }

    @POST
    @Path("/employee/monthendReport")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response employeeMonthendReport() {
        MonthlyReport monthlyReport = workerService.getMonthendReportForUser(sessionUser.getEmail());

        if (monthlyReport == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(monthlyReport).build();
    }

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
    @POST
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response employees() {
        final List<MitarbeiterType> mitarbeiterTypeList = workerService.getAllActiveEmployees();
        List<Employee> employees = mitarbeiterTypeList.stream()
                .map(EmployeeTranslator::toEmployee)
                .collect(Collectors.toList());

        return Response.ok(employees).build();
    }

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
    @PUT
    @Path("/employees/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response employeesUpdate(@NotEmpty(message = "{workerResource.employees.notEmpty}") final List<Employee> employees) {
        List<Pair<String, String>> pairsMailReleaseDate = employees.stream()
                .map(this::toPair)
                .collect(Collectors.toList());
        // TODO: Service throws exception if update fails and if no response data is necessary, then no response data is returned
        return Response.status(workerService.updateEmployeesReleaseDate(pairsMailReleaseDate)).build();
    }

    @RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.USER})
    @PUT
    @Path("/employee/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response employeeUpdate(@NotNull(message = "{workerResource.employee.notNull}") final Employee employee) {
        if (Role.USER.equals(sessionUser.getRole()) && !sessionUser.getEmail().equals(employee.getEMail())) {
            throw new SecurityException("User with userrole can not update other users");
        }
        // TODO: Service throws exception if update fails and if no response data is necessary, then no response data is returned
        return Response.status(workerService.updateEmployeeReleaseDate(employee.getEMail(), employee.getReleaseDate()))
                .build();
    }

    private Pair<String, String> toPair(Employee employee) {
        return Pair.of(employee.getEMail(), employee.getReleaseDate());
    }
}