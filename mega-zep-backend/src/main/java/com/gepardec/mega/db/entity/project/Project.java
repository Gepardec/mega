package com.gepardec.mega.db.entity.project;

import com.gepardec.mega.db.entity.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project",
        uniqueConstraints = {
                @UniqueConstraint(name = "uidx_ordinal", columnNames = {"ordinal"})
        }
)
public class Project {

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
     * The start date of the project
     */
    @NotNull
    @Column(name = "start_date", updatable = false, nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDate startDate;

    /**
     * The end date of the project
     */
    @NotNull
    @Column(name = "end_date", columnDefinition = "TIMESTAMP")
    private LocalDate endDate;

    /**
     * The id of the project
     */
    @NotNull
    @Min(1)
    @Column(name = "project_id", unique = true)
    private Integer projectId;

    /**
     * The project leads of the project
     */
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_project",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")}
    )
    private Set<User> projectLeads = new HashSet<>(0);

    @OneToMany(mappedBy = "project_step", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProjectEntry> projectEntries = new HashSet<>(0);

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Set<User> getProjectLeads() {
        return projectLeads;
    }

    public void setProjectLeads(Set<User> projectLeads) {
        this.projectLeads = projectLeads;
    }

    public Set<ProjectEntry> getProjectSteps() {
        return projectEntries;
    }

    public void setProjectSteps(Set<ProjectEntry> projectEntries) {
        this.projectEntries = projectEntries;
    }


}
