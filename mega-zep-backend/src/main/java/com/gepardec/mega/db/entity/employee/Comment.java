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
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comment")
@NamedQueries({
        @NamedQuery(name = "Comment.findAllCommentsBetweenStartDateAndEndDateAndAllOpenCommentsBeforeStartDateForEmail", query = "SELECT c FROM Comment c WHERE c.stepEntry.owner.email = :email AND ((c.stepEntry.date BETWEEN :start AND :end) OR (c.stepEntry.date < :start AND c.employeeState = :state))"),
        @NamedQuery(name = "Comment.findAllCommentsBetweenStartAndEndDateForEmail", query = "SELECT c FROM Comment c WHERE c.stepEntry.owner.email = :email AND ((c.stepEntry.date BETWEEN :start AND :end) OR (c.stepEntry.date < :start))")
})
public class Comment {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(generator = "commentIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "commentIdGenerator", sequenceName = "sequence_comment_id", allocationSize = 1)
    private Long id;

    /**
     * The creation date of the comment
     */
    @NotNull
    @Column(name = "creation_date", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    /**
     * The updated date of the comment
     */
    @NotNull
    @Column(name = "update_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedDate;

    /**
     * The message of the comment for the related step entry
     */
    @NotNull
    @Length(min = 1, max = 500)
    @Column(name = "message", length = 500)
    private String message;

    /**
     * The state of the comment
     *
     * @see EmployeeState
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EmployeeState employeeState;

    /**
     * The step entry the comment is for
     *
     * @see StepEntry
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_entry_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_step_entry", value = ConstraintMode.CONSTRAINT))
    private StepEntry stepEntry;

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

    public StepEntry getStepEntry() {
        return stepEntry;
    }

    public void setStepEntry(StepEntry stepEntry) {
        this.stepEntry = stepEntry;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EmployeeState getState() {
        return employeeState;
    }

    public void setState(EmployeeState employeeState) {
        this.employeeState = employeeState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        return (id != null) ? Objects.equals(id, comment.id) : super.equals(o);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", updatedDate=" + updatedDate +
                ", message='" + message + '\'' +
                ", state=" + employeeState +
                ", stepEntry=" + stepEntry +
                '}';
    }
}
