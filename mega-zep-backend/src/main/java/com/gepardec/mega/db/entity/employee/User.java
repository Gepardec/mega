package com.gepardec.mega.db.entity.employee;

import com.gepardec.mega.domain.model.Role;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employee_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uidx_email", columnNames = {"email"}),
                @UniqueConstraint(name = "uidx_zep_id", columnNames = {"zep_id"})
        })
@NamedQueries({
        @NamedQuery(name = "User.findActiveByEmail", query = "select u from User u where u.email = :email and u.active = true"),
        @NamedQuery(name = "User.findActive", query = "select u from User u where u.active = true"),
        @NamedQuery(name = "User.findByRoles", query = "select distinct u from User u inner join u.roles role where u.active = true and role in (:roles)")
})
public class User {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(generator = "employeeIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "employeeIdGenerator", sequenceName = "sequence_user_id", allocationSize = 1)
    private Long id;

    /**
     * The creation date of the user
     */
    @NotNull
    @Column(name = "creation_date", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    /**
     * The updated date of the user
     */
    @NotNull
    @Column(name = "update_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedDate;

    /**
     * The unique email of the user
     */
    @NotNull
    @Email
    @Length(min = 1, max = 100)
    @Column(name = "email")
    private String email;

    /**
     * The firstname of the user
     */
    @NotNull
    @Length(min = 1, max = 100)
    @Column(name = "firstname")
    private String firstname;

    /**
     * The lastname of the user
     */
    @NotNull
    @Length(min = 1, max = 100)
    @Column(name = "lastname")
    private String lastname;

    /**
     * The locale of the user
     */
    @NotNull
    @Type(type = "org.hibernate.type.LocaleType")
    @Column(name = "locale", length = 10)
    private Locale locale;

    /**
     * The ZEP internal user id
     */
    @NotNull
    @Length(min = 1, max = 100)
    @Column(name = "zep_id")
    private String zepId;

    /**
     * The ZEP release date
     */
    @Column(name = "release_date", columnDefinition = "DATE")
    private LocalDate releaseDate;

    /**
     * The flag which indicates the user is active
     */
    @NotNull
    @Column(name = "active")
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stepEntry")
    private Set<Comment> comments = new HashSet<>(0);

    @NotNull
    @Size(min = 1)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_user_roles",
            joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles = new HashSet<>(0);

    /**
     * The step entries the user is assigned to
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignee")
    private Set<StepEntry> assignedStepEntries = new HashSet<>(0);

    /**
     * The step entries the user owns
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<StepEntry> ownedStepEntries = new HashSet<>(0);

    public User() {
    }

    private User(final String email) {
        this.email = email;
    }

    public static User of(final String email) {
        return new User(email);
    }

    @PrePersist
    void onPersist() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getZepId() {
        return zepId;
    }

    public void setZepId(String zepId) {
        this.zepId = zepId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Set<StepEntry> getAssignedStepEntries() {
        return assignedStepEntries;
    }

    public void setAssignedStepEntries(Set<StepEntry> assignedStepEntries) {
        this.assignedStepEntries = assignedStepEntries;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<StepEntry> getOwnedStepEntries() {
        return ownedStepEntries;
    }

    public void setOwnedStepEntries(Set<StepEntry> ownedStepEntries) {
        this.ownedStepEntries = ownedStepEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return (id != null) ? Objects.equals(id, user.id) : super.equals(o);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : super.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", updatedDate=" + updatedDate +
                ", email='" + email + '\'' +
                ", zepId='" + zepId + '\'' +
                ", releaseDate=" + releaseDate +
                ", active=" + active +
                ", assignedStepEntries=" + assignedStepEntries +
                ", ownedStepEntries=" + ownedStepEntries +
                '}';
    }
}
