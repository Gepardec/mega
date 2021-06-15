package com.gepardec.mega.db.entity.project;

import com.gepardec.mega.db.entity.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
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
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_employee",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")}
    )
    private Set<User> projectLeads = new HashSet<>(0);

    /**
     * The project entries of the project
     *
     * @see ProjectEntry
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_project_entry",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_entry_id")}
    )
    private Set<ProjectEntry> projectEntries = new HashSet<>(0);

    // needed to be consistent with the database
    public void addProjectEntry(ProjectEntry projectEntry) {
        projectEntries.add(projectEntry);
        projectEntry.setProject(this);
    }

    public void removeProjectEntry(ProjectEntry projectEntry) {
        projectEntries.remove(projectEntry);
        projectEntry.setProject(null);
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

    public Set<ProjectEntry> getProjectEntries() {
        return projectEntries;
    }

    public void setProjectEntries(Set<ProjectEntry> projectEntries) {
        this.projectEntries = projectEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Project project = (Project) o;

        if (!Objects.equals(id, project.id)) {
            return false;
        }
        if (!Objects.equals(name, project.name)) {
            return false;
        }
        if (!Objects.equals(startDate, project.startDate)) {
            return false;
        }
        return projectEntries != null ? projectEntries.equals(project.projectEntries) : project.projectEntries == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
