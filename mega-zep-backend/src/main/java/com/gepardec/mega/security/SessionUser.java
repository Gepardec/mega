package com.gepardec.mega.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@SessionScoped
public class SessionUser implements Serializable {
    private String id;
    private String email;
    private String idToken;
    // TODO: User can have more than one role
    private Role role;
    private boolean logged;

<<<<<<<HEAD
=======

    public void init(final String email, final String idToken, final int recht) {
        this.email = Objects.requireNonNull(email, "SessionUser must have an email");
        this.idToken = Objects.requireNonNull(idToken, "SessionUser must have an idToken");
        this.role = Role.forId(recht).orElse(null);
        this.logged = true;
    }

>>>>>>>origin/feature/backend-refactoring

    public boolean isLoggedIn() {
        return logged;
    }

<<<<<<<HEAD

    public void checkForSameUser(String eMail) {
        if (Role.USER.equals(this.getRole()) && !this.getEmail().equals(eMail)) {
            throw new SecurityException("User with userrole can not update other users");
        }
    }
=======
        >>>>>>>origin/feature/backend-refactoring
}
