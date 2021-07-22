package com.gepardec.mega.db.entity.project;

import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.db.entity.employee.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "project_entry")
@NamedQuery(
        name = "ProjectEntry.findAllProjectEntriesForProjectNameInRange",
        query = "SELECT pe FROM ProjectEntry pe WHERE pe.project.name = :projectName AND pe.date BETWEEN :start AND :end")
@NamedQuery(
        name = "ProjectEntry.findProjectEntryByNameAndEntryDateAndStep",
        query = "SELECT pe FROM ProjectEntry pe WHERE pe.project.name = :projectName AND pe.date = :entryDate AND pe.step = :projectStep"
)
public class ProjectEntry {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(generator = "projectEntryIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "projectEntryIdGenerator", sequenceName = "sequence_project_entry_id", allocationSize = 1)
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
     * The update date of the project entry
     */
    @NotNull
    @Column(name = "update_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedDate;

    /**
     * The date (=month) the project entry is for
     */
    @NotNull
    @Column(name = "entry_date", updatable = false, columnDefinition = "DATE")
    private LocalDate date;

    /**
     * The owner of the project entry who is the user who is responsible for the validity of the entry
     *
     * @see User
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "owner_employee_user_id",
            referencedColumnName = "id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_owner_employee_user_id_project", value = ConstraintMode.CONSTRAINT))
    private User owner;

    /**
     * The assignee of the project entry who is the employee who marks the project entry done
     *
     * @see User
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "assignee_employee_user_id",
            referencedColumnName = "id",
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_assignee_employee_user_id_project", value = ConstraintMode.CONSTRAINT))
    private User assignee;

    /**
     * The related project of the project entry
     *
     * @see Project
     */
    @ManyToOne(targetEntity = Project.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    /**
     * The preset flag for the state
     */
    @Column(name = "preset", nullable = false, columnDefinition = "BOOLEAN")
    private boolean preset;

    /**
     * The state of the project step
     *
     * @see State
     */
    private State state;

    /**
     * The related step of this project entry
     *
     * @see ProjectStep
     */
    private ProjectStep step;

    @PrePersist
    void onPersist() {
        creationDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        state = preset ? State.NOT_RELEVANT : State.OPEN;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ProjectStep getStep() {
        return step;
    }

    public void setStep(ProjectStep step) {
        this.step = step;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectEntry that = (ProjectEntry) o;

        if (preset != that.preset) {
            return false;
        }
        if (!Objects.equals(id, that.id)) {
            return false;
        }
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        if (!Objects.equals(creationDate, that.creationDate)) {
            return false;
        }
        if (!Objects.equals(updatedDate, that.updatedDate)) {
            return false;
        }
        if (!Objects.equals(date, that.date)) {
            return false;
        }
        if (!Objects.equals(owner, that.owner)) {
            return false;
        }
        if (!Objects.equals(assignee, that.assignee)) {
            return false;
        }
        if (!Objects.equals(project, that.project)) {
            return false;
        }
        if (state != that.state) {
            return false;
        }
        return step == that.step;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (assignee != null ? assignee.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + (preset ? 1 : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (step != null ? step.hashCode() : 0);
        return result;
    }
}
