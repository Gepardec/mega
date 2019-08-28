package connector.service.api;

import connector.rest.model.GoogleUser;

import javax.ws.rs.core.Response;

public interface AuthenticationService {
    Response login(GoogleUser user);
}
