package connector.security;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

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
