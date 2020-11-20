package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.StepName;
import com.gepardec.mega.rest.model.ManagementEntry;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Secured
@RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
@Path("/employees")
public class EmployeeResource {

    @Inject
    EmployeeService employeeService;

    @Inject
    StepEntryService stepEntryService;

    @Inject
    CommentService commentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> list() {
        return employeeService.getAllActiveEmployees();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<String> update(@NotEmpty(message = "{workerResource.employees.notEmpty}") final List<Employee> employees) {
        return employeeService.updateEmployeesReleaseDate(employees);
    }

    @GET
    @Path("/officemanagemententries")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ManagementEntry> getAllOfficeManagementEntries() {
        List<ManagementEntry> officeManagementEntries = new ArrayList<>();
        List<Employee> activeEmployees = employeeService.getAllActiveEmployees();
        for (Employee empl : activeEmployees) {
            List<StepEntry> stepEntries = stepEntryService.findAllStepEntriesForEmployee(empl);
            if (!stepEntries.isEmpty()) {
                FinishedAndTotalComments finishedAndTotalComments = commentService.cntFinishedAndTotalCommentsForEmployee(empl);
                officeManagementEntries.add(ManagementEntry.builder()
                        .employee(empl)
                        .customerCheckState(extractStateForStep(stepEntries, StepName.CONTROL_EXTERNAL_TIMES))
                        .employeeCheckState(extractStateForStep(stepEntries, StepName.CONTROL_TIMES))
                        .internalCheckState(extractStateForStep(stepEntries, StepName.CONTROL_INTERNAL_TIMES))
                        .projectCheckState(extractStateForStep(stepEntries, StepName.CONTROL_TIME_EVIDENCES))
                        .finishedComments(finishedAndTotalComments.finishedComments())
                        .totalComments(finishedAndTotalComments.totalComments())
                        .build()
                );
            }
        }

        return officeManagementEntries;
    }

    private com.gepardec.mega.domain.model.State extractStateForStep(List<StepEntry> stepEntries, StepName step) {
        List<State> res = stepEntries
                .stream()
                .filter(se -> step.name().equalsIgnoreCase(se.getStep().getName()))
                .map(StepEntry::getState)
                .collect(Collectors.toList());

        if (res.isEmpty()) {
            return com.gepardec.mega.domain.model.State.OPEN;
        } else if (res.size() > 1) {
            return res.stream().allMatch(state -> state == State.DONE) ? com.gepardec.mega.domain.model.State.DONE : com.gepardec.mega.domain.model.State.OPEN;
        }

        return com.gepardec.mega.domain.model.State.valueOf(res.get(0).name());
    }
}
