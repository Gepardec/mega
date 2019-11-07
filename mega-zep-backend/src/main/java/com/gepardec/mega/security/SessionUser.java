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

    public static final int ROLE_USER = 0;
    public static final int ROLE_ADMINISTRATOR = 1;
    public static final int ROLE_CONTROLLER = 2;

    private String id;
    private String email;
    private String name;
    private String authToken;
    private String idToken;
    private String authorizationCode;
    private int role = -1;

    public void invalidate(){
        id = null;
        email = null;
        name = null;
        authToken = null;
        idToken = null;
        authorizationCode = null;
        role = -1;
    }
}
