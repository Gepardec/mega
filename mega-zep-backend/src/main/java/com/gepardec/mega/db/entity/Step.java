package com.gepardec.mega.db.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "STEP",
        uniqueConstraints = {
                @UniqueConstraint(name = "UIDX_ORDINAL", columnNames = {"ORDINAL"})
        },
        indexes = {
                @Index(name = "IDX_GROUP_NAME", columnList = "GROUP_NAME")
        }
)
public class Step {

    @Id
    @Column(name = "ID", insertable = false, updatable = false)
    @GeneratedValue(generator = "stepIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "stepIdGenerator", sequenceName = "SEQUENCE_STEP_ID", allocationSize = 1)
    private Long id;

    /**
     * The name of the step
     */
    @NotNull
    @Column(name = "NAME")
    @Length(min = 1, max = 255)
    private String name;

    /**
     * The role who is responsible for the step completion state
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    /**
     * The ordinal defining the order of the steps
     */
    @NotNull
    @Min(1)
    @Column(name = "ORDINAL", unique = true)
    private Integer ordinal;

    /**
     * The group id grouping sequential steps, which means the grouped steps can be worked on in parallel,
     * and all must be completed to be able to worked on the following steps.
     */
    @Length(min = 1, max = 50)
    @Column(name = "GROUP_NAME")
    private String groupName;

    /**
     * The related step entries whereby each step entry
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "step")
    @MapKeyColumn(name = "DATE")
    public Map<LocalDate, StepEntry> stepEntries = new HashMap<>(0);

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Map<LocalDate, StepEntry> getStepEntries() {
        return stepEntries;
    }

    public void setStepEntries(Map<LocalDate, StepEntry> stepEntries) {
        this.stepEntries = stepEntries;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupId) {
        this.groupName = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Step step = (Step) o;
        return (id != null) ? Objects.equals(id, step.id) : super.equals(o);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : super.hashCode();
    }
}
