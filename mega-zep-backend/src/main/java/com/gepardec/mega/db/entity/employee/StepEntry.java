package com.gepardec.mega.db.entity.employee;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "step_entry")
@NamedQueries({
        @NamedQuery(name = "StepEntry.findAllOwnedAndAssignedStepEntriesForEmployee", query = "SELECT s FROM StepEntry s WHERE s.date = :entryDate AND s.owner.email = :ownerEmail AND s.assignee.email = :assigneeEmail AND s.step.id = :stepId"),
        @NamedQuery(name = "StepEntry.findAllOwnedAndUnassignedStepEntriesForOtherChecks", query = "SELECT s FROM StepEntry s WHERE s.date = :entryDate AND s.owner.email = :ownerEmail AND s.owner.email <> s.assignee.email AND s.step.id not IN (:stepId)"),
        @NamedQuery(name = "StepEntry.findAllOwnedAndUnassignedStepEntriesForPMProgress", query = "SELECT s FROM StepEntry s WHERE s.date = :entryDate AND s.owner.email = :ownerEmail AND s.step.id = :stepId"),
        @NamedQuery(name = "StepEntry.findAllOwnedStepEntriesInRange", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.owner.email = :ownerEmail"),
        @NamedQuery(name = "StepEntry.findAllOwnedStepEntriesInRangeForProject", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.owner.email = :ownerEmail AND s.assignee.email like :assigneEmail AND s.project like :projectId"),
        @NamedQuery(name = "StepEntry.findStepEntryForEmployeeAtStepInRange", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.step.id = :stepId and s.owner.email = :ownerEmail and s.assignee.email = :assigneeEmail"),
        @NamedQuery(name = "StepEntry.findStepEntryForEmployeeAndProjectAtStepInRange", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.step.id = :stepId and s.owner.email = :ownerEmail and s.assignee.email = :assigneeEmail and s.project like :project"),
        @NamedQuery(name = "StepEntry.findAllStepEntriesForPMInRange", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.step.id = :stepId and s.assignee.email = :assigneEmail"),
        @NamedQuery(name = "StepEntry.findAllStepEntriesForAllPMInRange", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.step.id = :stepId")
})
public class StepEntry {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(generator = "stepEntryIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "stepEntryIdGenerator", sequenceName = "sequence_step_entry_id", allocationSize = 1)
    private Long id;

    /**
     * The creation date of the step entry
     */
    @NotNull
    @Column(name = "creation_date", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    /**
     * The update date of the step entry
     */
    @NotNull
    @Column(name = "update_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedDate;

    /**
     * The date (=month) the step entry is for
     */
    @NotNull
    @Column(name = "entry_date", updatable = false, columnDefinition = "DATE")
    private LocalDate date;

    /**
     * The related project of the step entry
     */
    @Length(max = 255)
    @Column(name = "project", updatable = false)
    private String project;

    /**
     * Teh state of the step entry
     *
     * @see EmployeeState
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EmployeeState employeeState;

    /**
     * The owner of the step entry who is the user who is responsible for the validity of the entry
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_employee_user_id",
            referencedColumnName = "id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_owner_employee_user_id", value = ConstraintMode.CONSTRAINT))
    private User owner;

    /**
     * The assignee of the step entry who is the employee who marks the step entry done
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_employee_user_id",
            referencedColumnName = "id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_assignee_employee_user_id", value = ConstraintMode.CONSTRAINT))
    private User assignee;

    /**
     * The related step of this step entry
     *
     * @see Step
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id",
            referencedColumnName = "id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_step_id", value = ConstraintMode.CONSTRAINT))
    private Step step;

    @PrePersist
    void onPersist() {
        employeeState = EmployeeState.OPEN;
        creationDate = updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public EmployeeState getState() {
        return employeeState;
    }

    public void setState(EmployeeState employeeState) {
        this.employeeState = employeeState;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StepEntry stepEntry = (StepEntry) o;
        return (id != null) ? Objects.equals(id, stepEntry.id) : super.equals(o);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : super.hashCode();
    }

    @Override
    public String toString() {
        return "StepEntry{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", updatedDate=" + updatedDate +
                ", date=" + date +
                ", project='" + project + '\'' +
                ", state=" + employeeState +
                ", owner=" + owner +
                ", assignee=" + assignee +
                ", step=" + step +
                '}';
    }
}
