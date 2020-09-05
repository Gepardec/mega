package com.gepardec.mega.db.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "COMMENT")
public class Comment {

    @Id
    @Column(name = "ID", insertable = false, updatable = false)
    @GeneratedValue(generator = "commentIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "commentIdGenerator", sequenceName = "SEQUENCE_COMMENT_ID", allocationSize = 1)
    private Long id;

    /**
     * The creation date of the comment
     */
    @NotNull
    @Column(name = "CREATION_DATE", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    /**
     * The updated date of the comment
     */
    @NotNull
    @Column(name = "UPDATE_DATE", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedDate;

    /**
     * The message of the comment for the related step entry
     */
    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "MESSAGE", updatable = false)
    private String message;

    /**
     * The state of the comment
     *
     * @see State
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private State state;

    /**
     * The step entry the comment is for
     *
     * @see StepEntry
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STEP_ENTRY_ID",
            referencedColumnName = "ID",
            foreignKey = @ForeignKey(name = "FK_STEP_ENTRY", value = ConstraintMode.CONSTRAINT))
    private StepEntry stepEntry;

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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
}
