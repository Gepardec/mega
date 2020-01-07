package com.gepardec.mega.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Data
@NoArgsConstructor
@SessionScoped
public class SessionUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String email;
    private String name;
    private String authToken;
    private String idToken;
    private String authorizationCode;
    private Role role;

    public boolean isLoggedIn() {
        return id != null;
    }

    public void checkForSameUser(String eMail) {
        if (Role.USER.equals(this.getRole()) && !this.getEmail().equals(eMail)) {
            throw new SecurityException("User with userrole can not update other users");
        }
    }
}
