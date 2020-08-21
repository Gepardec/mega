package com.gepardec.mega.db.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "step_employee")
public class StepEntry {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_employee_id",
            referencedColumnName = "id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_owner_employee", value = ConstraintMode.CONSTRAINT))
    private Employee owner;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_employee_id",
            referencedColumnName = "id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_assignee_employee", value = ConstraintMode.CONSTRAINT))
    private Employee assignee;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id",
            referencedColumnName = "id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_step", value = ConstraintMode.CONSTRAINT))
    private Step step;

    @NotNull
    @Column(name = "date", updatable = false)
    private LocalDate date;

    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "project", updatable = false)
    private String project;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @PrePersist
    void onPersist() {
        state = State.OPEN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    public Employee getAssignee() {
        return assignee;
    }

    public void setAssignee(Employee assignee) {
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
}
