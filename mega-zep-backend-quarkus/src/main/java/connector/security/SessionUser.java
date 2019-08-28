package connector.security;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class SessionUser implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Integer ROLE_USER = 0;
    public static final Integer ROLE_ADMINISTRATOR = 1;
    public static final Integer ROLE_CONTROLLER = 2;
}
