package com.gepardec.mega.rest;

import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.Step;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.db.entity.project.ProjectEntry;
import com.gepardec.mega.db.entity.project.ProjectStep;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import com.gepardec.mega.domain.model.ProjectEmployees;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.SecurityContext;
import com.gepardec.mega.domain.model.StepName;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.rest.model.ManagementEntryDto;
import com.gepardec.mega.rest.model.ProjectManagementEntryDto;
import com.gepardec.mega.service.api.CommentService;
import com.gepardec.mega.service.api.EmployeeService;
import com.gepardec.mega.service.api.ProjectService;
import com.gepardec.mega.service.api.ProjectEntryService;
import com.gepardec.mega.service.api.StepEntryService;
import com.gepardec.mega.zep.ZepService;
import de.provantis.zep.ProjektzeitType;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
class ManagementResourceTest {

    @InjectMock
    EmployeeService employeeService;

    @InjectMock
    StepEntryService stepEntryService;

    @InjectMock
    CommentService commentService;

    @InjectMock
    ProjectService projectService;

    @InjectMock
    ProjectEntryService projectEntryService;

    @InjectMock
    ZepService zepService;

    @InjectMock
    private SecurityContext securityContext;

    @InjectMock
    private UserContext userContext;

    @Test
    void getAllOfficeManagementEntries_whenNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        when(userContext.user()).thenReturn(createUserForRole(Role.OFFICE_MANAGEMENT));
        given().contentType(ContentType.JSON)
                .get("/management/officemanagemententries/2020/11")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void getAllOfficeManagementEntries_whenPOST_thenReturnsHttpStatusMETHOD_NOT_ALLOWED() {
        given().contentType(ContentType.JSON)
                .post("/management/officemanagemententries/2020/11")
                .then().assertThat().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void getAllOfficeManagementEntries_whenValid_thenReturnsListOfEntries() {
        final User user = createUserForRole(Role.OFFICE_MANAGEMENT);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        when(employeeService.getAllActiveEmployees())
                .thenReturn(List.of(Employee.builder().releaseDate("2020-01-01").email("no-reply@gepardec.com").build()));

        List<StepEntry> entries = List.of(
                createStepEntryForStep(StepName.CONTROL_EXTERNAL_TIMES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_INTERNAL_TIMES, EmployeeState.OPEN),
                createStepEntryForStep(StepName.CONTROL_TIME_EVIDENCES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_TIMES, EmployeeState.OPEN)
        );

        when(commentService.cntFinishedAndTotalCommentsForEmployee(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());

        when(stepEntryService.findAllStepEntriesForEmployee(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(entries);

        when(zepService.getProjectTimesForEmployeePerProject(
                ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(Collections.emptyList());

        List<ManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/officemanagemententries/2020/01")
                .as(new TypeRef<>() {
                });

        assertThat(result).hasSize(1);
        ManagementEntryDto entry = result.get(0);
        assertThat(entry.customerCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.DONE);
        assertThat(entry.internalCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.OPEN);
        assertThat(entry.employeeCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.OPEN);
        assertThat(entry.projectCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.DONE);
        assertThat(entry.employee().getEmail()).isEqualTo("no-reply@gepardec.com");
        assertThat(entry.employee().getReleaseDate()).isEqualTo("2020-01-01");
        assertThat(entry.totalComments()).isEqualTo(3L);
        assertThat(entry.finishedComments()).isEqualTo(2L);
    }

    @Test
    void getAllOfficeManagementEntries_whenNoActiveEmployeesFound_thenReturnsEmptyResultList() {
        final User user = createUserForRole(Role.OFFICE_MANAGEMENT);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        when(employeeService.getAllActiveEmployees()).thenReturn(List.of());

        List<StepEntry> entries = List.of(
                createStepEntryForStep(StepName.CONTROL_EXTERNAL_TIMES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_INTERNAL_TIMES, EmployeeState.OPEN),
                createStepEntryForStep(StepName.CONTROL_TIME_EVIDENCES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_TIMES, EmployeeState.OPEN)
        );

        when(commentService.cntFinishedAndTotalCommentsForEmployee(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());

        when(stepEntryService.findAllStepEntriesForEmployee(ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(entries);

        List<ManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/officemanagemententries/2020/10")
                .as(new TypeRef<>() {
                });

        assertThat(result).isEmpty();
    }

    @Test
    void getAllOfficeManagementEntries_whenNoStepEntriesFound_thenReturnsEmptyResultList() {
        final User user = createUserForRole(Role.OFFICE_MANAGEMENT);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        when(commentService.cntFinishedAndTotalCommentsForEmployee(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());

        when(stepEntryService.findAllStepEntriesForEmployee(ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(List.of());

        when(employeeService.getAllActiveEmployees())
                .thenReturn(List.of());

        List<ManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/officemanagemententries/2020/11")
                .as(new TypeRef<>() {
                });

        assertThat(result).isEmpty();
    }

    @Test
    void getAllProjectManagementEntries_whenNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        when(userContext.user()).thenReturn(createUserForRole(Role.PROJECT_LEAD));
        given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/11")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void getAllProjectManagementEntries_whenPOST_thenReturnsStatusMETHOD_NOT_ALLOWED() {
        given().contentType(ContentType.JSON)
                .post("/management/projectmanagemententries/2020/11")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void getAllProjectManagementEntries_whenValid_thenReturnsListOfEntries() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Employee employee1 = createEmployee("008", "no-reply@gepardec.com", "Max", "Mustermann");
        Employee employee2 = createEmployee("030", "no-reply@gepardec.com", "Max", "Mustermann");

        List<String> employees = List.of(employee1.getUserId(), employee2.getUserId());
        List<String> leads = List.of("005");
        ProjectEmployees rgkkcc = createProject("ÖGK-RGKKCC-2020", employees);
        ProjectEmployees rgkkwc = createProject("ÖGK-RGKK2WC-2020", employees);
        when(stepEntryService.getProjectEmployeesForPM(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString()))
                .thenReturn(List.of(rgkkcc, rgkkwc));

        when(employeeService.getAllActiveEmployees()).thenReturn(List.of(employee1, employee2));

        List<StepEntry> stepEntries = List.of(
                createStepEntryForStep(StepName.CONTROL_EXTERNAL_TIMES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_INTERNAL_TIMES, EmployeeState.OPEN),
                createStepEntryForStep(StepName.CONTROL_TIME_EVIDENCES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_TIMES, EmployeeState.OPEN)
        );

        List<ProjectEntry> projectEntries = List.of(
                createProjectEntryForStepWithStateAndPreset(ProjectStep.CONTROL_PROJECT, State.NOT_RELEVANT, true),
                createProjectEntryForStepWithStateAndPreset(ProjectStep.CONTROL_BILLING, State.DONE, false)
        );

        when(commentService.cntFinishedAndTotalCommentsForEmployee(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());

        when(stepEntryService.findAllStepEntriesForEmployeeAndProject(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(stepEntries);

        when(projectEntryService.findByNameAndDate(ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(projectEntries);

        when(zepService.getProjectTimesForEmployeePerProject(
                ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(getProjectTimeTypeList());

        when(zepService.getBillableTimesForEmployee(ArgumentMatchers.anyList(), ArgumentMatchers.any(Employee.class))).thenReturn("02:00");
        when(zepService.getBillableTimesForEmployee(ArgumentMatchers.anyList(), ArgumentMatchers.any(Employee.class), ArgumentMatchers.anyBoolean())).thenReturn("02:00");

        List<ProjectManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/10")
                .as(new TypeRef<>() {
                });

        assertThat(result).hasSize(2);
        Optional<ProjectManagementEntryDto> projectRgkkcc = result.stream()
                .filter(p -> rgkkcc.projectId().equalsIgnoreCase(p.projectName()))
                .findFirst();

        // assert project management entry
        assertThat(projectRgkkcc).isPresent();
        assertThat(projectRgkkcc.get().controlProjectState()).isEqualTo(com.gepardec.mega.domain.model.ProjectState.NOT_RELEVANT);
        assertThat(projectRgkkcc.get().controlBillingState()).isEqualTo(com.gepardec.mega.domain.model.ProjectState.DONE);
        assertThat(projectRgkkcc.get().presetControlProjectState()).isTrue();
        assertThat(projectRgkkcc.get().presetControlBillingState()).isFalse();

        List<ManagementEntryDto> rgkkccEntries = projectRgkkcc.get().entries();
        Optional<ManagementEntryDto> entrymmustermann = rgkkccEntries.stream()
                .filter(m -> employee1.getUserId().equalsIgnoreCase(m.employee().getUserId()))
                .findFirst();
        // assert management entry
        assertThat(entrymmustermann).isPresent();
        ManagementEntryDto entry = entrymmustermann.get();
        assertThat(entry.customerCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.DONE);
        assertThat(entry.internalCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.OPEN);
        assertThat(entry.employeeCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.OPEN);
        assertThat(entry.projectCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.DONE);
        assertThat(entry.employee().getEmail()).isEqualTo(employee1.getEmail());
        assertThat(entry.employee().getReleaseDate()).isEqualTo(employee1.getReleaseDate());
        assertThat(entry.totalComments()).isEqualTo(3L);
        assertThat(entry.finishedComments()).isEqualTo(2L);
    }

    @Test
    void getProjectManagementEntries_whenProjectTimes_thenCorrectAggregatedWorkTimes() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Employee employee1 = createEmployee("008", "no-reply@gepardec.com", "Max", "Mustermann");
        Employee employee2 = createEmployee("030", "no-reply@gepardec.com", "Max", "Mustermann");

        List<String> employees = List.of(employee1.getUserId(), employee2.getUserId());
        List<String> leads = List.of("005");
        ProjectEmployees rgkkcc = createProject("ÖGK-RGKKCC-2020", employees);
        ProjectEmployees rgkkwc = createProject("ÖGK-RGKK2WC-2020", employees);
        when(stepEntryService.getProjectEmployeesForPM(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString()))
                .thenReturn(List.of(rgkkcc, rgkkwc));

        when(employeeService.getAllActiveEmployees()).thenReturn(List.of(employee1, employee2));

        List<StepEntry> stepEntries = List.of(
                createStepEntryForStep(StepName.CONTROL_EXTERNAL_TIMES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_INTERNAL_TIMES, EmployeeState.OPEN),
                createStepEntryForStep(StepName.CONTROL_TIME_EVIDENCES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_TIMES, EmployeeState.OPEN)
        );

        List<ProjectEntry> projectEntries = List.of(
                createProjectEntryForStepWithStateAndPreset(ProjectStep.CONTROL_PROJECT, State.NOT_RELEVANT, true),
                createProjectEntryForStepWithStateAndPreset(ProjectStep.CONTROL_BILLING, State.DONE, false)
        );

        when(commentService.cntFinishedAndTotalCommentsForEmployee(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());

        when(stepEntryService.findAllStepEntriesForEmployeeAndProject(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(stepEntries);

        when(projectEntryService.findByNameAndDate(ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(projectEntries);

        when(zepService.getProjectTimesForEmployeePerProject(
                ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(getProjectTimeTypeList());

        when(zepService.getBillableTimesForEmployee(ArgumentMatchers.anyList(), ArgumentMatchers.any(Employee.class))).thenReturn("01:00");
        when(zepService.getBillableTimesForEmployee(ArgumentMatchers.anyList(), ArgumentMatchers.any(Employee.class), ArgumentMatchers.eq(true))).thenReturn("02:00");

        List<ProjectManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/09")
                .as(new TypeRef<>() {
                });

        assertThat(result).hasSize(2);
        Optional<ProjectManagementEntryDto> projectRgkkcc = result.stream()
                .filter(p -> rgkkcc.projectId().equalsIgnoreCase(p.projectName()))
                .findFirst();

        // assert project management entry
        assertThat(projectRgkkcc).isPresent();
        assertThat(projectRgkkcc.get().controlProjectState()).isEqualTo(com.gepardec.mega.domain.model.ProjectState.NOT_RELEVANT);
        assertThat(projectRgkkcc.get().controlBillingState()).isEqualTo(com.gepardec.mega.domain.model.ProjectState.DONE);
        assertThat(projectRgkkcc.get().presetControlProjectState()).isTrue();
        assertThat(projectRgkkcc.get().presetControlBillingState()).isFalse();

        List<ManagementEntryDto> rgkkccEntries = projectRgkkcc.get().entries();
        Optional<ManagementEntryDto> entrymmustermann = rgkkccEntries.stream()
                .filter(m -> employee1.getUserId().equalsIgnoreCase(m.employee().getUserId()))
                .findFirst();
        // assert management entry
        assertThat(entrymmustermann).isPresent();
        ManagementEntryDto entry = entrymmustermann.get();
        assertThat(entry.customerCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.DONE);
        assertThat(entry.internalCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.OPEN);
        assertThat(entry.employeeCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.OPEN);
        assertThat(entry.projectCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.DONE);
        assertThat(entry.employee().getEmail()).isEqualTo(employee1.getEmail());
        assertThat(entry.employee().getReleaseDate()).isEqualTo(employee1.getReleaseDate());
        assertThat(entry.totalComments()).isEqualTo(3L);
        assertThat(entry.finishedComments()).isEqualTo(2L);

        // assert billable/non billable time
        assertThat(result.get(0).aggregatedBillableWorkTimeInSeconds()).isEqualTo(Duration.ofMinutes(240));
        assertThat(result.get(0).aggregatedNonBillableWorkTimeInSeconds()).isEqualTo(Duration.ofMinutes(120));
    }

    @Test
    void getProjectManagementEntries_whenNoProjectTimes_thenZeroAggregatedWorkTimes() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Employee employee1 = createEmployee("008", "no-reply@gepardec.com", "Max", "Mustermann");
        Employee employee2 = createEmployee("030", "no-reply@gepardec.com", "Max", "Mustermann");

        List<String> employees = List.of(employee1.getUserId(), employee2.getUserId());
        List<String> leads = List.of("005");
        ProjectEmployees rgkkcc = createProject("ÖGK-RGKKCC-2020", employees);
        ProjectEmployees rgkkwc = createProject("ÖGK-RGKK2WC-2020", employees);
        when(stepEntryService.getProjectEmployeesForPM(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString()))
                .thenReturn(List.of(rgkkcc, rgkkwc));

        when(employeeService.getAllActiveEmployees()).thenReturn(List.of(employee1, employee2));

        List<StepEntry> stepEntries = List.of(
                createStepEntryForStep(StepName.CONTROL_EXTERNAL_TIMES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_INTERNAL_TIMES, EmployeeState.OPEN),
                createStepEntryForStep(StepName.CONTROL_TIME_EVIDENCES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_TIMES, EmployeeState.OPEN)
        );

        List<ProjectEntry> projectEntries = List.of(
                createProjectEntryForStepWithStateAndPreset(ProjectStep.CONTROL_PROJECT, State.NOT_RELEVANT, true),
                createProjectEntryForStepWithStateAndPreset(ProjectStep.CONTROL_BILLING, State.DONE, false)
        );

        when(commentService.cntFinishedAndTotalCommentsForEmployee(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());

        when(stepEntryService.findAllStepEntriesForEmployeeAndProject(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(stepEntries);

        when(projectEntryService.findByNameAndDate(ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(projectEntries);

        when(zepService.getProjectTimesForEmployeePerProject(
                ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(getProjectTimeTypeList());

        when(zepService.getBillableTimesForEmployee(ArgumentMatchers.anyList(), ArgumentMatchers.any(Employee.class))).thenReturn("00:00");
        when(zepService.getBillableTimesForEmployee(ArgumentMatchers.anyList(), ArgumentMatchers.any(Employee.class), ArgumentMatchers.eq(true))).thenReturn("00:00");

        List<ProjectManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/09")
                .as(new TypeRef<>() {
                });

        assertThat(result).hasSize(2);
        Optional<ProjectManagementEntryDto> projectRgkkcc = result.stream()
                .filter(p -> rgkkcc.projectId().equalsIgnoreCase(p.projectName()))
                .findFirst();

        // assert project management entry
        assertThat(projectRgkkcc).isPresent();
        assertThat(projectRgkkcc.get().controlProjectState()).isEqualTo(com.gepardec.mega.domain.model.ProjectState.NOT_RELEVANT);
        assertThat(projectRgkkcc.get().controlBillingState()).isEqualTo(com.gepardec.mega.domain.model.ProjectState.DONE);
        assertThat(projectRgkkcc.get().presetControlProjectState()).isTrue();
        assertThat(projectRgkkcc.get().presetControlBillingState()).isFalse();

        List<ManagementEntryDto> rgkkccEntries = projectRgkkcc.get().entries();
        Optional<ManagementEntryDto> entrymmustermann = rgkkccEntries.stream()
                .filter(m -> employee1.getUserId().equalsIgnoreCase(m.employee().getUserId()))
                .findFirst();
        // assert management entry
        assertThat(entrymmustermann).isPresent();
        ManagementEntryDto entry = entrymmustermann.get();
        assertThat(entry.customerCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.DONE);
        assertThat(entry.internalCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.OPEN);
        assertThat(entry.employeeCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.OPEN);
        assertThat(entry.projectCheckState()).isEqualTo(com.gepardec.mega.domain.model.State.DONE);
        assertThat(entry.employee().getEmail()).isEqualTo(employee1.getEmail());
        assertThat(entry.employee().getReleaseDate()).isEqualTo(employee1.getReleaseDate());
        assertThat(entry.totalComments()).isEqualTo(3L);
        assertThat(entry.finishedComments()).isEqualTo(2L);

        // assert billable/non billable time
        assertThat(result.get(0).aggregatedBillableWorkTimeInSeconds()).isEqualTo(Duration.ofMinutes(0));
        assertThat(result.get(0).aggregatedNonBillableWorkTimeInSeconds()).isEqualTo(Duration.ofMinutes(0));
    }

    @Test
    void getProjectManagementEntries_whenManagementEntryIsNull_thenNoNullPointerException() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Employee employee1 = createEmployee("008", "no-reply@gepardec.com", "Max", "Mustermann");
        Employee employee2 = createEmployee("030", "no-reply@gepardec.com", "Max", "Mustermann");

        List<String> employees = List.of(employee1.getUserId(), employee2.getUserId());
        List<String> leads = List.of("005");
        ProjectEmployees rgkkcc = createProject("ÖGK-RGKKCC-2020", employees);
        ProjectEmployees rgkkwc = createProject("ÖGK-RGKK2WC-2020", employees);
        when(stepEntryService.getProjectEmployeesForPM(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString()))
                .thenReturn(List.of(rgkkcc, rgkkwc));

        when(employeeService.getAllActiveEmployees()).thenReturn(List.of(employee1, employee2));

        List<StepEntry> stepEntries = List.of(
                createStepEntryForStep(StepName.CONTROL_EXTERNAL_TIMES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_INTERNAL_TIMES, EmployeeState.OPEN),
                createStepEntryForStep(StepName.CONTROL_TIME_EVIDENCES, EmployeeState.DONE),
                createStepEntryForStep(StepName.CONTROL_TIMES, EmployeeState.OPEN)
        );

        List<ProjectEntry> projectEntries = List.of(
                createProjectEntryForStepWithStateAndPreset(ProjectStep.CONTROL_PROJECT, State.NOT_RELEVANT, true),
                createProjectEntryForStepWithStateAndPreset(ProjectStep.CONTROL_BILLING, State.DONE, false)
        );

        when(commentService.cntFinishedAndTotalCommentsForEmployee(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(FinishedAndTotalComments.builder().finishedComments(2L).totalComments(3L).build());

        when(stepEntryService.findAllStepEntriesForEmployeeAndProject(
                ArgumentMatchers.any(Employee.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class))
        ).thenReturn(stepEntries);

        when(projectEntryService.findByNameAndDate(ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(projectEntries);

        when(zepService.getProjectTimesForEmployeePerProject(
                ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(getProjectTimeTypeList());

        List<ProjectManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/09")
                .as(new TypeRef<>() {
                });

        // assert billable/non billable time
        assertThat(result.get(0).aggregatedBillableWorkTimeInSeconds()).isEqualTo(Duration.ofMinutes(0));
        assertThat(result.get(0).aggregatedNonBillableWorkTimeInSeconds()).isEqualTo(Duration.ofMinutes(0));
    }

    @Test
    void getAllProjectManagementEntries_whenNoProjectsFound_thenReturnsEmptyList() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        List<ProjectManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/11")
                .as(new TypeRef<>() {
                });

        assertThat(result).isEmpty();
    }

    @Test
    void getAllProjectManagementEntries_whenNoEmployeesAssignedToProject_thenReturnResultList() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        ProjectEmployees rgkkcc = createProject("ÖGK-RGKKCC-2020", List.of());
        when(stepEntryService.getProjectEmployeesForPM(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString()))
                .thenReturn(List.of(rgkkcc));

        List<ProjectManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/11")
                .as(new TypeRef<>() {
                });

        assertThat(result).isEmpty();
    }

    @Test
    void getAllProjectManagementEntries_whenNoStepEntriesFound_thenReturnsResultList() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Employee employee1 = createEmployee("008", "no-reply@gepardec.com", "Max", "Mustermann");
        Employee employee2 = createEmployee("030", "no-reply@gepardec.com", "Max", "Mustermann");

        List<String> employees = List.of(employee1.getUserId(), employee2.getUserId());
        ProjectEmployees rgkkcc = createProject("ÖGK-RGKKCC-2020", employees);
        when(stepEntryService.getProjectEmployeesForPM(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString()))
                .thenReturn(List.of(rgkkcc));

        when(stepEntryService.findAllStepEntriesForEmployee(ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(List.of());

        List<ProjectManagementEntryDto> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/11")
                .as(new TypeRef<>() {
                });

        assertThat(result).isEmpty();
    }

    private Step createStep(StepName stepName) {
        Step step = new Step();
        step.setName(stepName.name());
        return step;
    }

    private StepEntry createStepEntryForStep(StepName stepName, EmployeeState employeeState) {
        StepEntry stepEntry = new StepEntry();
        stepEntry.setStep(createStep(stepName));
        stepEntry.setState(employeeState);
        stepEntry.setDate(LocalDate.now());
        stepEntry.setAssignee(createUser());
        return stepEntry;
    }

    private com.gepardec.mega.db.entity.employee.User createUser() {
        com.gepardec.mega.db.entity.employee.User user = new com.gepardec.mega.db.entity.employee.User();
        user.setEmail("no-reply@gpeardec.com");
        return user;
    }

    private User createUserForRole(final Role role) {
        return User.builder()
                .dbId(1)
                .userId("005")
                .email("no-reply@gpeardec.com")
                .firstname("Max")
                .lastname("Mustermann")
                .roles(Set.of(role))
                .build();
    }

    private ProjectEmployees createProject(String projectId, List<String> employees) {
        return ProjectEmployees.builder()
                .projectId(projectId)
                .employees(employees)
                .build();
    }

    private Employee createEmployee(String userId, String email, String firstname, String lastname) {
        return Employee.builder()
                .userId(userId)
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .build();
    }

    private ProjectEntry createProjectEntryForStepWithStateAndPreset(ProjectStep step, State state, boolean preset) {
        ProjectEntry p = new ProjectEntry();
        p.setStep(step);
        p.setState(state);
        p.setPreset(preset);
        return p;
    }

    private List<ProjektzeitType> getProjectTimeTypeList() {
        List<ProjektzeitType> timeType = new ArrayList<>();
        ProjektzeitType projektzeitType = new ProjektzeitType();
        projektzeitType.setDauer("4");
        projektzeitType.setVon("08:00");
        projektzeitType.setBis("12:00");
        projektzeitType.setIstFakturierbar(true);

        ProjektzeitType projektzeitType1 = new ProjektzeitType();
        projektzeitType1.setDauer("0");
        projektzeitType1.setVon("00:00");
        projektzeitType1.setBis("00:00");

        ProjektzeitType projektzeitType2 = new ProjektzeitType();
        projektzeitType2.setDauer("2");
        projektzeitType2.setVon("13:00");
        projektzeitType2.setBis("15:00");
        projektzeitType2.setIstFakturierbar(false);

        timeType.add(projektzeitType);
        timeType.add(projektzeitType1);
        timeType.add(projektzeitType2);

        return timeType;
    }
}
