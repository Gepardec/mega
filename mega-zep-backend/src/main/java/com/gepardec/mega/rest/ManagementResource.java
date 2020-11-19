package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.domain.model.*;
import com.gepardec.mega.rest.model.ManagementEntry;
import com.gepardec.mega.rest.model.ProjectManagementEntry;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Secured
@RolesAllowed(allowedRoles = {Role.ADMINISTRATOR, Role.CONTROLLER})
@Path("/management")
public class ManagementResource {

    @Inject
    EmployeeService employeeService;

    @Inject
    StepEntryService stepEntryService;

    @Inject
    CommentService commentService;

    @GET
    @Path("/officemanagemententries")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ManagementEntry> getAllOfficeManagementEntries() {
        List<ManagementEntry> officeManagementEntries = new ArrayList<>();
        List<Employee> activeEmployees = employeeService.getAllActiveEmployees();
        for(Employee empl : activeEmployees) {
            ManagementEntry newManagementEntry = createManagemenEntryForEmployee(empl);
            if(newManagementEntry != null) {
                officeManagementEntries.add(newManagementEntry);
            }
        }

        return officeManagementEntries;
    }

    @Inject
    UserContext userContext;

    @Inject
    ProjectService projectService;

    @GET
    @Path("/projectmanagemententries")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectManagementEntry> getAllProjectManagementEntries() {
        List<Project> projects = projectService
                .getProjectsForMonthYear(LocalDate.now(), List.of(ProjectFilter.IS_LEADS_AVAILABLE))
                .stream()
                .filter(project -> project.leads().stream().allMatch(lead -> lead.equalsIgnoreCase(
                        "005-wbruckmueller" // FIXME: userContext.user().userId()
                )))
                .collect(Collectors.toList());

        List<ProjectManagementEntry> projectManagementEntries = new ArrayList<>();
        for(Project currentProject : projects) {
            List<ManagementEntry> entries = new ArrayList<>();
            for(String userId : currentProject.employees()) {
                Employee empl = employeeService.getEmployee(userId);
                ManagementEntry newManagementEntry = createManagemenEntryForEmployee(empl);
                if(newManagementEntry != null) {
                    entries.add(newManagementEntry);
                }
            }

            projectManagementEntries.add(ProjectManagementEntry.builder()
                    .projectName(currentProject.projectId())
                    .entries(entries)
                    .build()
            );
        }

        return projectManagementEntries;
    }

    private ManagementEntry createManagemenEntryForEmployee(Employee employee) {
        List<StepEntry> stepEntries = stepEntryService.findAllStepEntriesForEmployee(employee);
        if(!stepEntries.isEmpty()) {
            FinishedAndTotalComments finishedAndTotalComments = commentService.cntFinishedAndTotalCommentsForEmployee(employee);
            return ManagementEntry.builder()
                    .employee(employee)
                    .customerCheckState(extractStateForStep(stepEntries, StepName.CONTROL_EXTERNAL_TIMES))
                    .employeeCheckState(extractStateForStep(stepEntries, StepName.CONTROL_TIMES))
                    .internalCheckState(extractStateForStep(stepEntries, StepName.CONTROL_INTERNAL_TIMES))
                    .projectCheckState(extractStateForStep(stepEntries, StepName.CONTROL_TIME_EVIDENCES))
                    .finishedComments(finishedAndTotalComments.finishedComments())
                    .totalComments(finishedAndTotalComments.totalComments())
                    .build();
        }

        return null;
    }

    private com.gepardec.mega.domain.model.State extractStateForStep(List<StepEntry> stepEntries, StepName step) {
        List<State> res = stepEntries
                .stream()
                .filter(se -> step.name().equalsIgnoreCase(se.getStep().getName()))
                .map(StepEntry::getState)
                .collect(Collectors.toList());

        if(res.size() != 1) {
            return com.gepardec.mega.domain.model.State.OPEN;
        }

        return com.gepardec.mega.domain.model.State.valueOf(res.get(0).name());
    }
}
