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
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(generator = "projectIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "projectIdGenerator", sequenceName = "sequence_project_id", allocationSize = 1)
    private Long id;

    /**
     * The name of the project
     */
    @NotNull
    @Column(name = "name", unique = true)
    @Length(min = 1, max = 255)
    private String name;

    /**
     * The start date of the project
     */
    @NotNull
    @Column(name = "start_date", updatable = false, nullable = false, columnDefinition = "DATE")
    private LocalDate startDate;

    /**
     * The end date of the project
     */
    @Column(name = "end_date", columnDefinition = "DATE")
    private LocalDate endDate;

    /**
     * The project leads of the project
     *
     * @see User
     */
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_project",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")}
    )
    private Set<User> projectLeads = new HashSet<>(0);

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectEntry> items = new HashSet<>(0);

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

    public Set<User> getProjectLeads() {
        return projectLeads;
    }

    public void setProjectLeads(Set<User> projectLeads) {
        this.projectLeads = projectLeads;
    }

    public Set<ProjectEntry> getItems() {
        return items;
    }

    public void setItems(Set<ProjectEntry> items) {
        this.items = items;
    }
}
