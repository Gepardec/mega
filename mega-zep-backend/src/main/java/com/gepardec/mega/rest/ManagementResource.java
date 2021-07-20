package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.db.entity.project.ProjectEntry;
import com.gepardec.mega.db.entity.project.ProjectStep;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import com.gepardec.mega.domain.model.ProjectEmployees;
import com.gepardec.mega.domain.model.ProjectState;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.StepName;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.rest.model.ManagementEntry;
import com.gepardec.mega.rest.model.PmProgress;
import com.gepardec.mega.rest.model.ProjectManagementEntry;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.projectentry.ProjectEntryService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.zep.ZepService;
import de.provantis.zep.ProjektzeitType;
import de.provantis.zep.ReadProjektzeitenResponseType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Secured
@RolesAllowed(value = {Role.PROJECT_LEAD, Role.OFFICE_MANAGEMENT})
@Path("/management")
public class ManagementResource {

    private static final String DATE_FORMAT_PATTERN = "YYYY-MM-dd";

    @Inject
    EmployeeService employeeService;

    @Inject
    StepEntryService stepEntryService;

    @Inject
    CommentService commentService;

    @Inject
    UserContext userContext;

    @Inject
    ProjectEntryService projectEntryService;

    @Inject
    ZepService zepService;

    @GET
    @Path("/officemanagemententries/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ManagementEntry> getAllOfficeManagementEntries(@PathParam("year") Integer year, @PathParam("month") Integer month) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        List<ManagementEntry> officeManagementEntries = new ArrayList<>();
        List<Employee> activeEmployees = employeeService.getAllActiveEmployees();

        for (Employee empl : activeEmployees) {
            List<StepEntry> stepEntries = stepEntryService.findAllStepEntriesForEmployee(empl, from, to);

            String entryDate = LocalDate.of(year, month, 1).minusMonths(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));

            List<StepEntry> allOwnedStepEntriesForPMProgress = stepEntryService.findAllOwnedAndUnassignedStepEntriesForPMProgress(empl.email(), entryDate);
            List<PmProgress> pmProgresses = new ArrayList<>();

            allOwnedStepEntriesForPMProgress
                    .forEach(stepEntry -> pmProgresses.add(
                            PmProgress.builder()
                                    .project(stepEntry.getProject())
                                    .assigneeEmail(stepEntry.getAssignee().getEmail())
                                    .state(stepEntry.getState())
                                    .stepId(stepEntry.getStep().getId())
                                    .firstname(stepEntry.getAssignee().getFirstname())
                                    .lastname(stepEntry.getAssignee().getLastname())
                                    .build()
                    ));

            ManagementEntry newManagementEntry = createManagementEntryForEmployee(empl, stepEntries, from, to, pmProgresses);

            if (newManagementEntry != null) {
                officeManagementEntries.add(newManagementEntry);
            }

        }

        return officeManagementEntries;
    }

    @GET
    @Path("/projectmanagemententries/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectManagementEntry> getAllProjectManagementEntries(@PathParam("year") Integer year, @PathParam("month") Integer month) {
        if (userContext == null || userContext.user() == null) {
            throw new IllegalStateException("User context does not exist or user is null.");
        }

        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());

        List<ProjectEmployees> projectEmployees = stepEntryService.getProjectEmployeesForPM(from, to, userContext.user().email());
        List<ProjectManagementEntry> projectManagementEntries = new ArrayList<>();

        Map<String, Employee> employees = createEmployeeCache();

        for (ProjectEmployees currentProject : projectEmployees) {
            List<ManagementEntry> entries = createManagementEntriesForProject(currentProject, employees, from, to);
            List<ProjectEntry> projectEntries = projectEntryService.findByNameAndDate(currentProject.projectId(), from, to);

            if (!entries.isEmpty() && !projectEntries.isEmpty()) {
                projectManagementEntries.add(ProjectManagementEntry.builder()
                        .projectName(currentProject.projectId())
                        .controlProjectState(ProjectState.byName(getProjectEntryForProjectStep(projectEntries, ProjectStep.CONTROL_PROJECT).getState().name()))
                        .controlBillingState(ProjectState.byName((getProjectEntryForProjectStep(projectEntries, ProjectStep.CONTROL_BILLING).getState().name())))
                        .presetControlProjectState(getProjectEntryForProjectStep(projectEntries, ProjectStep.CONTROL_PROJECT).isPreset())
                        .presetControlBillingState(getProjectEntryForProjectStep(projectEntries, ProjectStep.CONTROL_BILLING).isPreset())
                        .entries(entries)
                        .build()
                );
            }
        }
        return projectManagementEntries;
    }

    /*
      @GET
    @Path("projectworkinghours/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReadProjektzeitenResponseType getBillableProjectHours(@PathParam("year") Integer year, @PathParam("month") Integer month) {
        //ZepServiceImpl function(): getProjectTimesForEmployeePerProject  ?
        LocalDate curDate = LocalDate.of(year, month, 1);
        beanUnderTest = new ZepServiceImpl(new EmployeeMapper(), logger, zepSoapPortType, zepSoapProvider, projectEntryMapper);
        List<Project> project = beanUnderTest.getProjectsForMonthYear(curDate);

        ReadProjektzeitenResponseType readProjektzeitenResponse = beanUnderTest.getProjectTimesForEmployeePerProject(project.get(0), curDate);

        if (readProjektzeitenResponse == null) {
            return null;

        }

        return readProjektzeitenResponse;

    }

     */

    private ProjectEntry getProjectEntryForProjectStep(List<ProjectEntry> projectEntries, ProjectStep projectStep) {
        return projectEntries.stream()
                .filter(p -> p.getStep() == projectStep)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No project entry found for project step '%s'", projectStep)));
    }

    private Map<String, Employee> createEmployeeCache() {
        return employeeService.getAllActiveEmployees().stream()
                .collect(Collectors.toMap(Employee::userId, employee -> employee));
    }

    private List<ManagementEntry> createManagementEntriesForProject(ProjectEmployees projectEmployees, Map<String, Employee> employees, LocalDate from, LocalDate to) {
        if (userContext == null || userContext.user() == null) {
            throw new IllegalStateException("User context does not exist or user is null.");
        }

        List<ManagementEntry> entries = new ArrayList<>();

        for (String userId : projectEmployees.employees()) {

            if (employees.containsKey(userId)) {
                Employee empl = employees.get(userId);
                List<StepEntry> stepEntries = stepEntryService.findAllStepEntriesForEmployeeAndProject(
                        empl, projectEmployees.projectId(), userContext.user().email(), from, to
                );

                ManagementEntry entry = createManagementEntryForEmployee(empl, projectEmployees.projectId(), stepEntries, from, to, null);

                if (entry != null) {
                    entries.add(entry);
                }
            }
        }

        return entries;
    }

    private ManagementEntry createManagementEntryForEmployee(Employee employee, List<StepEntry> stepEntries, LocalDate from, LocalDate to, List<PmProgress> pmProgresses) {
        return createManagementEntryForEmployee(employee, null, stepEntries, from, to, pmProgresses);
    }

    private ManagementEntry createManagementEntryForEmployee(Employee employee, String projectId, List<StepEntry> stepEntries, LocalDate from, LocalDate to, List<PmProgress> pmProgresses) {
        FinishedAndTotalComments finishedAndTotalComments = commentService.cntFinishedAndTotalCommentsForEmployee(employee, from, to);
        ReadProjektzeitenResponseType readProjektzeitenResponseType = zepService.getProjectTimesForEmployeePerProject(projectId, from);

        List<ProjektzeitType> projektzeitTypes = readProjektzeitenResponseType.getProjektzeitListe().getProjektzeiten();

        Duration totalBillable = projektzeitTypes.stream()
                .filter(pzt -> pzt.getUserId().equals(employee.userId()))
                .filter(ProjektzeitType::isIstFakturierbar)
                .map(pzt -> LocalTime.parse(pzt.getDauer()))
                .map(lt -> Duration.between(LocalTime.MIN, lt))
                .reduce(Duration.ZERO, Duration::plus);

        Duration totalNonBillable = projektzeitTypes.stream()
                .filter(pzt -> pzt.getUserId().equals(employee.userId()))
                .filter(Predicate.not(ProjektzeitType::isIstFakturierbar))
                .map(pzt -> LocalTime.parse(pzt.getDauer()))
                .map(lt -> Duration.between(LocalTime.MIN, lt))
                .reduce(Duration.ZERO, Duration::plus);



        if (!stepEntries.isEmpty()) {
            return ManagementEntry.builder()
                    .employee(employee)
                    .customerCheckState(extractCustomerCheckState(stepEntries))
                    .employeeCheckState(extractEmployeeCheckState(stepEntries))
                    .internalCheckState(extractInternalCheckState(stepEntries))
                    .projectCheckState(extractStateForProject(stepEntries, projectId))
                    .employeeProgresses(pmProgresses)
                    .finishedComments(finishedAndTotalComments.finishedComments())
                    .totalComments(finishedAndTotalComments.totalComments())
                    .entryDate(stepEntries.get(0).getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .billableTime(DurationFormatUtils.formatDuration(totalBillable.toMillis(),"HH:mm:ss"))
                    .nonBillableTime(DurationFormatUtils.formatDuration(totalNonBillable.toMillis(),"HH:mm:ss"))
                    .build();
        }

        return null;
    }

    private com.gepardec.mega.domain.model.State extractEmployeeCheckState(List<StepEntry> stepEntries) {
        boolean employeeCheckStateOpen = stepEntries.stream()
                .filter(stepEntry -> StepName.CONTROL_TIMES.name().equalsIgnoreCase(stepEntry.getStep().getName()))
                .anyMatch(stepEntry -> EmployeeState.OPEN.equals(stepEntry.getState()));

        return employeeCheckStateOpen ? com.gepardec.mega.domain.model.State.OPEN : com.gepardec.mega.domain.model.State.DONE;
    }

    private com.gepardec.mega.domain.model.State extractCustomerCheckState(List<StepEntry> stepEntries) {
        if (userContext == null || userContext.user() == null) {
            throw new IllegalStateException("User context does not exist or user is null.");
        }

        boolean customerCheckStateOpen = stepEntries.stream()
                .filter(stepEntry ->
                        StepName.CONTROL_EXTERNAL_TIMES.name().equalsIgnoreCase(stepEntry.getStep().getName())
                                && StringUtils.equalsIgnoreCase(userContext.user().email(), stepEntry.getAssignee().getEmail())
                ).anyMatch(stepEntry -> EmployeeState.OPEN.equals(stepEntry.getState()));

        return customerCheckStateOpen ? com.gepardec.mega.domain.model.State.OPEN : com.gepardec.mega.domain.model.State.DONE;
    }

    private com.gepardec.mega.domain.model.State extractInternalCheckState(List<StepEntry> stepEntries) {
        if (userContext == null || userContext.user() == null) {
            throw new IllegalStateException("User context does not exist.");
        }

        boolean customerCheckStateOpen = stepEntries.stream()
                .filter(stepEntry ->
                        StepName.CONTROL_INTERNAL_TIMES.name().equalsIgnoreCase(stepEntry.getStep().getName())
                                && StringUtils.equalsIgnoreCase(userContext.user().email(), stepEntry.getAssignee().getEmail())
                ).anyMatch(stepEntry -> EmployeeState.OPEN.equals(stepEntry.getState()));

        return customerCheckStateOpen ? com.gepardec.mega.domain.model.State.OPEN : com.gepardec.mega.domain.model.State.DONE;
    }

    private com.gepardec.mega.domain.model.State extractStateForProject(List<StepEntry> stepEntries, String projectId) {
        if (userContext == null || userContext.user() == null) {
            throw new IllegalStateException("User context does not exist or user is null.");
        }

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
                .anyMatch(state -> state.equals(EmployeeState.OPEN)) ? com.gepardec.mega.domain.model.State.OPEN : com.gepardec.mega.domain.model.State.DONE;
    }
}
