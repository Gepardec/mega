package com.gepardec.mega.service.api.stepentry;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.ProjectEmployees;
import com.gepardec.mega.domain.model.StepEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StepEntryService {
    Optional<EmployeeState> findEmployeeCheckState(final Employee employee, LocalDate date);

    Optional<EmployeeState> findEmployeeCheckState(final Employee employee);

    List<com.gepardec.mega.db.entity.employee.StepEntry> findAllOwnedAndUnassignedStepEntriesForOtherChecks(final Employee employee);

    List<com.gepardec.mega.db.entity.employee.StepEntry> findAllOwnedAndUnassignedStepEntriesForPMProgress(final String email, final String date);

    void addStepEntry(final StepEntry stepEntry);

    boolean setOpenAndAssignedStepEntriesDone(Employee employee, Long stepId, LocalDate from, LocalDate to);

    boolean closeStepEntryForEmployeeInProject(Employee employee, Long stepId, String project, String assigneeEmail, String currentMonthYear);

    List<com.gepardec.mega.db.entity.employee.StepEntry> findAllStepEntriesForEmployee(Employee employee, LocalDate from, LocalDate to);

    List<com.gepardec.mega.db.entity.employee.StepEntry> findAllStepEntriesForEmployeeAndProject(Employee employee, String projectId, String assigneEmail, LocalDate from, LocalDate to);

    com.gepardec.mega.db.entity.employee.StepEntry findStepEntryForEmployeeAtStep(Long stepId, Employee employee, String assigneeEmail, String currentMonthYear);

    com.gepardec.mega.db.entity.employee.StepEntry findStepEntryForEmployeeAndProjectAtStep(Long stepId, Employee employee, String assigneeEmail, String project, String currentMonthYear);

    List<ProjectEmployees> getProjectEmployeesForPM(final LocalDate from, final LocalDate to, final String assigneEmail);

    List<ProjectEmployees> getAllProjectEmployeesForPM(final LocalDate from, final LocalDate to);
}
