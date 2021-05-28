package com.gepardec.mega.db.entity.project;

import com.gepardec.mega.db.entity.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_entry")
@NamedQuery(
        name = "ProjectEntry.findAllProjectEntriesForProjectNameInRange",
        query = "SELECT pe FROM ProjectEntry pe WHERE pe.project.name = :projectName AND pe.date BETWEEN :start AND :end")
public class ProjectEntry {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(generator = "projectIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "projectIdGenerator", sequenceName = "sequence_project_entry_id", allocationSize = 1)
    private Long id;

    /**
     * The name of the project
     */
    @NotNull
    @Column(name = "name")
    @Length(min = 1, max = 255)
    private String name;

    /**
     * The creation date of the project entry
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
     * The owner of the step entry who is the user who is responsible for the validity of the entry
     *
     * @see User
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
     *
     * @see User
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_employee_user_id",
            referencedColumnName = "id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_assignee_employee_user_id", value = ConstraintMode.CONSTRAINT))
    private User assignee;

    /**
     * The related project of the project entry
     *
     * @see Project
     */
    @ManyToOne
    @JoinColumn(name = "project", nullable = false)
    private Project project;

    /**
     * The preset flag for the state
     */
    @Column(name = "preset", nullable = false, columnDefinition = "BOOLEAN")
    private boolean preset;

    /**
     * The state of the project step
     *
     * @see ProjectState
     */
    private ProjectState state;

    /**
     * The related step of this project entry
     *
     * @see ProjectStep
     */
    private ProjectStep step;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isPreset() {
        return preset;
    }

    public void setPreset(boolean preset) {
        this.preset = preset;
    }

    public ProjectState getState() {
        return state;
    }

    public void setState(ProjectState state) {
        this.state = state;
    }

    public ProjectStep getStep() {
        return step;
    }

    public void setStep(ProjectStep step) {
        this.step = step;
    }
}
