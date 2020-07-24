package com.gepardec.mega.application.security;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Objects;

@SessionScoped
public class SessionUser implements Serializable {
    private String userId;
    private String email;
    private String idToken;
    // TODO: User can have more than one role
    private Role role;
    private boolean logged;

    public SessionUser() {
        // nop
    }

    public void init(final String userId, final String email, final String idToken, final Integer recht) {
        this.userId = Objects.requireNonNull(userId, "SessionUser must have an userId");
        this.email = Objects.requireNonNull(email, "SessionUser must have an email");
        this.idToken = Objects.requireNonNull(idToken, "SessionUser must have an idToken");
        this.role = Role.forId(recht).orElse(null);
        this.logged = true;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getIdToken() {
        return idToken;
    }

    public Role getRole() {
        return role;
    }

    public boolean isLogged() {
        return logged;
    }
}
