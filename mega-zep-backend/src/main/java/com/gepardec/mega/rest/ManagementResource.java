package com.gepardec.mega.rest;

import com.gepardec.mega.application.constant.DateTimeConstants;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    @Inject
    UserContext userContext;

    @Inject
    ProjectService projectService;

    @GET
    @Path("/officemanagemententries")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ManagementEntry> getAllOfficeManagementEntries() {
        List<ManagementEntry> officeManagementEntries = new ArrayList<>();
        List<Employee> activeEmployees = employeeService.getAllActiveEmployees();
        for (Employee empl : activeEmployees) {
            ManagementEntry newManagementEntry = createManagementEntryForEmployee(
                    empl,
                    (employee) -> stepEntryService.findAllStepEntriesForEmployee(empl)
            );
            if (newManagementEntry != null) {
                officeManagementEntries.add(newManagementEntry);
            }
        }

        return officeManagementEntries;
    }

    @GET
    @Path("/projectmanagemententries")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectManagementEntry> getAllProjectManagementEntries() {
        List<Project> projects = projectService
                .getProjectsForMonthYear(LocalDate.now(), List.of(ProjectFilter.IS_LEADS_AVAILABLE))
                .stream()
                .filter(project -> project.leads().stream().allMatch(lead -> lead.equalsIgnoreCase(
                        userContext.user().userId()
                )))
                .collect(Collectors.toList());

        List<ProjectManagementEntry> projectManagementEntries = new ArrayList<>();
        Map<String, Employee> employees = createEmployeeCache();
        for (Project currentProject : projects) {
            List<ManagementEntry> entries = createManagementEntriesForProject(currentProject, employees);
            projectManagementEntries.add(ProjectManagementEntry.builder()
                    .projectName(currentProject.projectId())
                    .entries(entries)
                    .build()
            );
        }

        return projectManagementEntries;
    }

    private Map<String, Employee> createEmployeeCache() {
        return employeeService.getAllActiveEmployees().stream()
                .collect(Collectors.toMap(Employee::userId, employee -> employee));
    }

    private List<ManagementEntry> createManagementEntriesForProject(Project project, Map<String, Employee> employees) {
        List<ManagementEntry> entries = new ArrayList<>();
        for (String userId : project.employees()) {
            if (employees.containsKey(userId)) {
                Employee empl = employees.get(userId);
                entries.add(createManagementEntryForEmployee(
                        empl,
                        project.projectId(),
                        (employee -> stepEntryService.findAllStepEntriesForEmployeeAndProject(
                                employee, project.projectId(), userContext.user().email()
                        ))
                ));
            }
        }

        return entries;
    }

    private ManagementEntry createManagementEntryForEmployee(Employee employee, Function<Employee, List<StepEntry>> findStepEntries) {
        return createManagementEntryForEmployee(employee, null, findStepEntries);
    }

    private ManagementEntry createManagementEntryForEmployee(Employee employee, String projectId, Function<Employee, List<StepEntry>> findStepEntries) {
        List<StepEntry> stepEntries = findStepEntries.apply(employee);
        FinishedAndTotalComments finishedAndTotalComments = commentService.cntFinishedAndTotalCommentsForEmployee(employee);
        if (!stepEntries.isEmpty()) {
            return ManagementEntry.builder()
                    .employee(employee)
                    .customerCheckState(extractStateForStep(stepEntries, StepName.CONTROL_EXTERNAL_TIMES, projectId))
                    .employeeCheckState(extractStateForStep(stepEntries, StepName.CONTROL_TIMES, projectId))
                    .internalCheckState(extractStateForStep(stepEntries, StepName.CONTROL_INTERNAL_TIMES, projectId))
                    .projectCheckState(extractStateForStep(stepEntries, StepName.CONTROL_TIME_EVIDENCES, projectId))
                    .finishedComments(finishedAndTotalComments.finishedComments())
                    .totalComments(finishedAndTotalComments.totalComments())
                    .entryDate(stepEntries.get(0).getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .build();
        }

        return ManagementEntry.builder()
                .employee(employee)
                .customerCheckState(com.gepardec.mega.domain.model.State.DONE)
                .employeeCheckState(com.gepardec.mega.domain.model.State.DONE)
                .internalCheckState(com.gepardec.mega.domain.model.State.DONE)
                .projectCheckState(com.gepardec.mega.domain.model.State.DONE)
                .finishedComments(finishedAndTotalComments.finishedComments())
                .totalComments(finishedAndTotalComments.totalComments())
                .entryDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }

    private com.gepardec.mega.domain.model.State extractStateForStep(List<StepEntry> stepEntries, StepName step, String projectId) {
        List<State> res = stepEntries
                .stream()
                .filter(se -> {
                    if (projectId == null) {
                        return step.name().equalsIgnoreCase(se.getStep().getName());
                    } else {
                        return step.name().equalsIgnoreCase(se.getStep().getName()) && projectId.equalsIgnoreCase(se.getProject());
                    }
                })
                .map(StepEntry::getState)
                .collect(Collectors.toList());

        if(res.size() == 1) {
            return com.gepardec.mega.domain.model.State.valueOf(res.get(0).name());
        } else if(res.size() > 1) {
            return res.stream().anyMatch(state -> state.equals(State.OPEN)) ?
                    com.gepardec.mega.domain.model.State.OPEN : com.gepardec.mega.domain.model.State.DONE;
        }

        return com.gepardec.mega.domain.model.State.OPEN;
    }
}
