package com.gepardec.mega.rest.model;

import com.gepardec.mega.aplication.security.Role;

/**
 * Represents the logged user in mega.
 */
public class User {

    private String email;

    private String firstname;

    private String lastname;

    private Role role;

    public User() {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
