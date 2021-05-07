package com.gepardec.mega.db.entity.project;

import com.gepardec.mega.db.entity.Step;
import com.gepardec.mega.db.entity.User;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_step",
        uniqueConstraints = {
                @UniqueConstraint(name = "uidx_ordinal", columnNames = {"ordinal"})
        }
)
public class ProjectEntry {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(generator = "stepIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "stepIdGenerator", sequenceName = "sequence_step_id", allocationSize = 1)
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
     * The related project of the step entry
     */
    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Project project;

    /**
     * The preset flag for the state
     */
    @Column(name = "preset", nullable = false, columnDefinition = "BOOLEAN")
    private boolean preset;

    /**
     * The state of the project step
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state", nullable = false, columnDefinition = "INTEGER")
    private State state;

    /**
     * The related step of this project entry
     *
     * @see Step
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "step", nullable = false, columnDefinition = "INTEGER")
    private ProjectStep step;
}
