package connector.service.api;

import connector.rest.model.GoogleUser;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

public interface AuthenticationService {
    Response login(GoogleUser user, HttpServletRequest request);
    Response logout(HttpServletRequest request);
}
