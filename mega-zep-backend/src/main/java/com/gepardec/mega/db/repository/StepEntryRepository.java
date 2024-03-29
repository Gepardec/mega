package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.domain.model.StepName;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StepEntryRepository implements PanacheRepository<StepEntry> {



    public Optional<StepEntry> findAllOwnedAndAssignedStepEntriesForEmployee(LocalDate entryDate, String ownerAndAssigneeEmail) {
        return find("#StepEntry.findAllOwnedAndAssignedStepEntriesForEmployee",
                Parameters
                        .with("entryDate", entryDate)
                        .and("ownerEmail", ownerAndAssigneeEmail)
                        .and("assigneeEmail", ownerAndAssigneeEmail)
                        .and("stepId", StepName.CONTROL_TIMES.getId()))
                .singleResultOptional();
    }

    public List<StepEntry> findAllOwnedAndUnassignedStepEntriesForOtherChecks(LocalDate entryDate, String ownerEmail) {
        List<StepEntry> entries = find("#StepEntry.findAllOwnedAndUnassignedStepEntriesForOtherChecks",
                Parameters
                        .with("entryDate", entryDate)
                        .and("ownerEmail", ownerEmail)
                        .and("stepId", List.of(StepName.CONTROL_TIMES.getId(), StepName.ACCEPT_TIMES.getId())))
                .list();

        entries.addAll(find("#StepEntry.findAllOwnedAndAssignedStepEntriesForEmployee",
                Parameters
                        .with("entryDate", entryDate)
                        .and("ownerEmail", ownerEmail)
                        .and("assigneeEmail", ownerEmail)
                        .and("stepId", StepName.CONTROL_TIME_EVIDENCES.getId()))
                .list());

        return entries;
    }

    public List<StepEntry> findAllOwnedAndUnassignedStepEntriesForPMProgress(LocalDate entryDate, String ownerEmail) {
        return find("#StepEntry.findAllOwnedAndUnassignedStepEntriesForPMProgress",
                Parameters
                        .with("entryDate", entryDate)
                        .and("ownerEmail", ownerEmail)
                        .and("stepId", StepName.CONTROL_TIME_EVIDENCES.getId()))
                .list();
    }

    public List<StepEntry> findAllOwnedStepEntriesInRange(LocalDate startDate, LocalDate endDate, String ownerEmail) {
        return find("#StepEntry.findAllOwnedStepEntriesInRange",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("ownerEmail", ownerEmail)
        ).list();
    }

    public List<StepEntry> findAllOwnedStepEntriesInRange(LocalDate startDate, LocalDate endDate, String ownerEmail, String projectId, String assigneEmail) {
        return find("#StepEntry.findAllOwnedStepEntriesInRangeForProject",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("ownerEmail", ownerEmail)
                        .and("assigneEmail", assigneEmail)
                        .and("projectId", projectId))
                .list();
    }

    @Transactional
    public int closeAssigned(LocalDate startDate, LocalDate endDate, String ownerEmail, Long stepId) {
        return update("UPDATE StepEntry s SET s.employeeState = :employeeState WHERE s.id IN (SELECT s.id FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.owner.email = :ownerEmail AND s.step.id = :stepId)",
                Parameters
                        .with("employeeState", EmployeeState.DONE)
                        .and("start", startDate)
                        .and("end", endDate)
                        .and("ownerEmail", ownerEmail)
                        .and("stepId", stepId));
    }

    @Transactional
    public int closeAssigned(LocalDate startDate, LocalDate endDate, String ownerEmail, String assigneeEmail, Long stepId, String project) {
        return update("UPDATE StepEntry s SET s.employeeState = :employeeState WHERE s.id IN (SELECT s.id FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.owner.email = :ownerEmail AND s.step.id = :stepId AND s.project like :project AND s.assignee.email = :assigneeEmail)",
                Parameters
                        .with("employeeState", EmployeeState.DONE)
                        .and("start", startDate)
                        .and("end", endDate)
                        .and("ownerEmail", ownerEmail)
                        .and("assigneeEmail", assigneeEmail)
                        .and("project", project)
                        .and("stepId", stepId));
    }

    public Optional<StepEntry> findStepEntryForEmployeeAtStepInRange(LocalDate startDate, LocalDate endDate, String ownerEmail, Long stepId, String assigneeEmail) {
        return find("#StepEntry.findStepEntryForEmployeeAtStepInRange",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("ownerEmail", ownerEmail)
                        .and("stepId", stepId)
                        .and("assigneeEmail", assigneeEmail))
                .singleResultOptional();
    }

    public Optional<StepEntry> findStepEntryForEmployeeAndProjectAtStepInRange(LocalDate startDate, LocalDate endDate, String ownerEmail, Long stepId, String assigneeEmail, String project) {
        return find("#StepEntry.findStepEntryForEmployeeAndProjectAtStepInRange",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("ownerEmail", ownerEmail)
                        .and("stepId", stepId)
                        .and("assigneeEmail", assigneeEmail)
                        .and("project", project))
                .singleResultOptional();
    }

    public List<StepEntry> findAllStepEntriesForPMInRange(LocalDate startDate, LocalDate endDate, String assigneEmail) {
        return find("#StepEntry.findAllStepEntriesForPMInRange",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("assigneEmail", assigneEmail)
                        .and("stepId", StepName.CONTROL_TIME_EVIDENCES.getId())
        ).list();
    }

    public List<StepEntry> findAllStepEntriesForAllPMInRange(LocalDate startDate, LocalDate endDate) {
        return find("#StepEntry.findAllStepEntriesForAllPMInRange",
                Parameters
                        .with("start", startDate)
                        .and("end", endDate)
                        .and("stepId", StepName.CONTROL_TIME_EVIDENCES.getId())
        ).list();
    }
}
