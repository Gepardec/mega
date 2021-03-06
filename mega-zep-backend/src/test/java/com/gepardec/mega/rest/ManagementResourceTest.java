package com.gepardec.mega.rest;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.Step;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.db.entity.project.ProjectEntry;
import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.db.entity.project.ProjectStep;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.FinishedAndTotalComments;
import com.gepardec.mega.domain.model.ProjectEmployees;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.SecurityContext;
import com.gepardec.mega.domain.model.StepName;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.rest.model.ManagementEntry;
import com.gepardec.mega.rest.model.ProjectManagementEntry;
import com.gepardec.mega.service.api.comment.CommentService;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.service.api.projectentry.ProjectEntryService;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import com.gepardec.mega.zep.ZepService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.wildfly.common.Assert;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ManagementResourceTest {

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
    private SecurityContext securityContext;

    @InjectMock
    private UserContext userContext;

    @InjectMock
    ZepService zepService;

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
                .thenReturn(List.of(Employee.builder().releaseDate("2020-01-01").email("marko.gattringer@gepardec.com").build()));

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

        List<ManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/management/officemanagemententries/2020/01")
                .as(new TypeRef<>() {
                });

        assertEquals(1L, result.size());
        ManagementEntry entry = result.get(0);
        assertEquals(com.gepardec.mega.domain.model.State.DONE, entry.customerCheckState());
        assertEquals(com.gepardec.mega.domain.model.State.OPEN, entry.internalCheckState());
        assertEquals(com.gepardec.mega.domain.model.State.OPEN, entry.employeeCheckState());
        assertEquals(com.gepardec.mega.domain.model.State.DONE, entry.projectCheckState());
        assertEquals("marko.gattringer@gepardec.com", entry.employee().email());
        assertEquals("2020-01-01", entry.employee().releaseDate());
        assertEquals(3L, entry.totalComments());
        assertEquals(2L, entry.finishedComments());
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

        List<ManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/management/officemanagemententries/2020/10")
                .as(new TypeRef<>() {
                });

        assertEquals(0L, result.size());
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

        List<ManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/management/officemanagemententries/2020/11")
                .as(new TypeRef<>() {
                });

        assertEquals(0L, result.size());
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

        Employee mgattringer = createEmployee("008-mgattringer", "marko.gattringer@gepardec.com", "Marko", "Gattringer");
        Employee jgartner = createEmployee("030-jgartner", "julian.gartner@gepardec.com", "Julian", "Gartner");

        List<String> employees = List.of(mgattringer.userId(), jgartner.userId());
        List<String> leads = List.of("005-wbruckmueller");
        ProjectEmployees rgkkcc = createProject("ÖGK-RGKKCC-2020", employees);
        ProjectEmployees rgkkwc = createProject("ÖGK-RGKK2WC-2020", employees);
        when(stepEntryService.getProjectEmployeesForPM(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString()))
                .thenReturn(List.of(rgkkcc, rgkkwc));

        when(employeeService.getAllActiveEmployees()).thenReturn(List.of(mgattringer, jgartner));

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

        // TODO add concrete parameter values instead of any
        when(projectEntryService.findByNameAndDate(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(projectEntries);

        when(zepService.getProjectTimesForEmployeePerProject(
                ArgumentMatchers.anyString(), ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(Collections.emptyList());

        List<ProjectManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/10")
                .as(new TypeRef<>() {
                });

        assertEquals(2, result.size());
        Optional<ProjectManagementEntry> projectRgkkcc = result.stream()
                .filter(p -> rgkkcc.projectId().equalsIgnoreCase(p.projectName()))
                .findFirst();

        // assert project management entry
        Assert.assertTrue(projectRgkkcc.isPresent());
        assertEquals(com.gepardec.mega.domain.model.ProjectState.NOT_RELEVANT, projectRgkkcc.get().controlProjectState());
        assertEquals(com.gepardec.mega.domain.model.ProjectState.DONE, projectRgkkcc.get().controlBillingState());
        assertTrue(projectRgkkcc.get().presetControlProjectState());
        assertFalse(projectRgkkcc.get().presetControlBillingState());

        List<ManagementEntry> rgkkccEntries = projectRgkkcc.get().entries();
        Optional<ManagementEntry> entryMgattringer = rgkkccEntries.stream()
                .filter(m -> mgattringer.userId().equalsIgnoreCase(m.employee().userId()))
                .findFirst();
        // assert management entry
        Assert.assertTrue(entryMgattringer.isPresent());
        ManagementEntry entry = entryMgattringer.get();
        assertEquals(com.gepardec.mega.domain.model.State.DONE, entry.customerCheckState());
        assertEquals(com.gepardec.mega.domain.model.State.OPEN, entry.internalCheckState());
        assertEquals(com.gepardec.mega.domain.model.State.OPEN, entry.employeeCheckState());
        assertEquals(com.gepardec.mega.domain.model.State.DONE, entry.projectCheckState());
        assertEquals(mgattringer.email(), entry.employee().email());
        assertEquals(mgattringer.releaseDate(), entry.employee().releaseDate());
        assertEquals(3L, entry.totalComments());
        assertEquals(2L, entry.finishedComments());

    }

    @Test
    void getAllProjectManagementEntries_whenNoProjectsFound_thenReturnsEmptyList() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        List<ProjectManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/11")
                .as(new TypeRef<>() {
                });

        assertEquals(0L, result.size());
    }

    @Test
    void getAllProjectManagementEntries_whenNoEmployeesAssignedToProject_thenReturnResultList() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        List<String> leads = List.of("005-wbruckmueller");
        ProjectEmployees rgkkcc = createProject("ÖGK-RGKKCC-2020", List.of());
        when(stepEntryService.getProjectEmployeesForPM(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString()))
                .thenReturn(List.of(rgkkcc));

        List<ProjectManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/11")
                .as(new TypeRef<>() {
                });

        assertEquals(0L, result.size());
    }

    @Test
    void getAllProjectManagementEntries_whenNoStepEntriesFound_thenReturnsResultList() {
        final User user = createUserForRole(Role.PROJECT_LEAD);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Employee mgattringer = createEmployee("008-mgattringer", "marko.gattringer@gepardec.com", "Marko", "Gattringer");
        Employee jgartner = createEmployee("030-jgartner", "julian.gartner@gepardec.com", "Julian", "Gartner");

        List<String> employees = List.of(mgattringer.userId(), jgartner.userId());
        ProjectEmployees rgkkcc = createProject("ÖGK-RGKKCC-2020", employees);
        when(stepEntryService.getProjectEmployeesForPM(ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.anyString()))
                .thenReturn(List.of(rgkkcc));

        when(stepEntryService.findAllStepEntriesForEmployee(ArgumentMatchers.any(Employee.class), ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class)))
                .thenReturn(List.of());

        List<ProjectManagementEntry> result = given().contentType(ContentType.JSON)
                .get("/management/projectmanagemententries/2020/11")
                .as(new TypeRef<>() {
                });

        assertEquals(0L, result.size());
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
        user.setEmail("werner.bruckmueller@gpeardec.com");
        return user;
    }

    private User createUserForRole(final Role role) {
        return User.builder()
                .dbId(1)
                .userId("005-wbruckmueller")
                .email("werner.bruckmueller@gpeardec.com")
                .firstname("Werner")
                .lastname("Bruckmüller")
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
}
