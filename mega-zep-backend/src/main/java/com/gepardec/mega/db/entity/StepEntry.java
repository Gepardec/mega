package com.gepardec.mega.db.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "step_entry")
@NamedQueries({
        @NamedQuery(name = "StepEntry.findAllOwnedAndAssignedStepEntriesForEmployee", query = "SELECT s FROM StepEntry s WHERE s.date = :entryDate AND s.owner.email = :ownerEmail AND s.assignee.email = :assigneeEmail AND s.step.id = :stepId"),
        @NamedQuery(name = "StepEntry.findAllOwnedAndUnassignedStepEntriesForOtherChecks", query = "SELECT s FROM StepEntry s WHERE s.date = :entryDate AND s.owner.email = :ownerEmail AND s.owner.email <> s.assignee.email AND s.step.id <> :stepId"),
        @NamedQuery(name = "StepEntry.findAllOwnedAndUnassignedStepEntriesForPMProgress", query = "SELECT s FROM StepEntry s WHERE s.date = :entryDate AND s.owner.email = :ownerEmail AND s.step.id = :stepId"),
        @NamedQuery(name="StepEntry.findAllOwnedStepEntriesInRange", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.owner.email = :ownerEmail"),
        @NamedQuery(name="StepEntry.findAllOwnedStepEntriesInRangeForProject", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.owner.email = :ownerEmail AND s.assignee.email like :assigneEmail AND s.project like :projectId"),
        @NamedQuery(name="StepEntry.findStepEntryForEmployeeAtStepInRange", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.step.id = :stepId and s.owner.email = :ownerEmail and s.assignee.email = :assigneeEmail"),
        @NamedQuery(name="StepEntry.findStepEntryForEmployeeAndProjectAtStepInRange", query = "SELECT s FROM StepEntry s WHERE s.date BETWEEN :start AND :end AND s.step.id = :stepId and s.owner.email = :ownerEmail and s.assignee.email = :assigneeEmail and s.project like :project"),
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
     * @see State
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

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
        state = State.OPEN;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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
                ", state=" + state +
                ", owner=" + owner +
                ", assignee=" + assignee +
                ", step=" + step +
                '}';
    }
}
