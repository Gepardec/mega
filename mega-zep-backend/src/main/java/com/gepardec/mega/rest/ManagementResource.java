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
import org.apache.commons.lang3.StringUtils;

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
import java.util.stream.Collectors;

@Secured
@RolesAllowed(value = {Role.PROJECT_LEAD, Role.OFFICE_MANAGEMENT})
@Path("/management" )
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
            List<StepEntry> stepEntries = stepEntryService.findAllStepEntriesForEmployee(empl);
            ManagementEntry newManagementEntry = createManagementEntryForEmployee(empl, stepEntries);
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
                .filter(project -> project.leads().stream().anyMatch(lead -> lead.equalsIgnoreCase(
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
                List<StepEntry> stepEntries = stepEntryService.findAllStepEntriesForEmployeeAndProject(
                        empl, project.projectId(), userContext.user().email()
                );
                entries.add(createManagementEntryForEmployee(empl, project.projectId(), stepEntries));
            }
        }

        return entries;
    }

    private ManagementEntry createManagementEntryForEmployee(Employee employee, List<StepEntry> stepEntries) {
        return createManagementEntryForEmployee(employee, null, stepEntries);
    }

    private ManagementEntry createManagementEntryForEmployee(Employee employee, String projectId, List<StepEntry> stepEntries) {
        FinishedAndTotalComments finishedAndTotalComments = commentService.cntFinishedAndTotalCommentsForEmployee(employee);
        if (!stepEntries.isEmpty()) {
            return ManagementEntry.builder()
                    .employee(employee)
                    .customerCheckState(extractCustomerCheckState(stepEntries))
                    .employeeCheckState(extractEmployeeCheckState(stepEntries))
                    .internalCheckState(extractInternalCheckState(stepEntries))
                    .projectCheckState(extractStateForProject(stepEntries, projectId))
                    .finishedComments(finishedAndTotalComments.finishedComments())
                    .totalComments(finishedAndTotalComments.totalComments())
                    .entryDate(stepEntries.get(0).getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd" )))
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
                .entryDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd" )))
                .build();
    }

    private com.gepardec.mega.domain.model.State extractEmployeeCheckState(List<StepEntry> stepEntries) {
        boolean employeeCheckStateOpen = stepEntries.stream()
                .filter(stepEntry -> StepName.CONTROL_TIMES.name().equalsIgnoreCase(stepEntry.getStep().getName()))
                .anyMatch(stepEntry -> State.OPEN.equals(stepEntry.getState()));

        return employeeCheckStateOpen ? com.gepardec.mega.domain.model.State.OPEN : com.gepardec.mega.domain.model.State.DONE;
    }

    private com.gepardec.mega.domain.model.State extractCustomerCheckState(List<StepEntry> stepEntries) {
        boolean customerCheckStateOpen = stepEntries.stream()
                .filter(stepEntry ->
                        StepName.CONTROL_EXTERNAL_TIMES.name().equalsIgnoreCase(stepEntry.getStep().getName())
                                && StringUtils.equalsIgnoreCase(userContext.user().email(), stepEntry.getAssignee().getEmail())
                ).anyMatch(stepEntry -> State.OPEN.equals(stepEntry.getState()));

        return customerCheckStateOpen ? com.gepardec.mega.domain.model.State.OPEN : com.gepardec.mega.domain.model.State.DONE;
    }

    private com.gepardec.mega.domain.model.State extractInternalCheckState(List<StepEntry> stepEntries) {
        boolean customerCheckStateOpen = stepEntries.stream()
                .filter(stepEntry ->
                        StepName.CONTROL_INTERNAL_TIMES.name().equalsIgnoreCase(stepEntry.getStep().getName())
                                && StringUtils.equalsIgnoreCase(userContext.user().email(), stepEntry.getAssignee().getEmail())
                ).anyMatch(stepEntry -> State.OPEN.equals(stepEntry.getState()));

        return customerCheckStateOpen ? com.gepardec.mega.domain.model.State.OPEN : com.gepardec.mega.domain.model.State.DONE;
    }

    private com.gepardec.mega.domain.model.State extractStateForProject(List<StepEntry> stepEntries, String projectId) {
        return stepEntries
                .stream()
                .filter(se -> {
                    if (StringUtils.isBlank(projectId)) {
                        return StepName.CONTROL_TIME_EVIDENCES.name().equalsIgnoreCase(se.getStep().getName());
                    } else {
                        return StepName.CONTROL_TIME_EVIDENCES.name().equalsIgnoreCase(se.getStep().getName()) &&
                                StringUtils.equalsIgnoreCase(se.getProject(), projectId) &&
                                StringUtils.equalsIgnoreCase(se.getAssignee().getEmail(), userContext.user().email());
                    }
                })
                .map(StepEntry::getState)
                .anyMatch(state -> state.equals(State.OPEN)) ? com.gepardec.mega.domain.model.State.OPEN : com.gepardec.mega.domain.model.State.DONE;
    }

}
