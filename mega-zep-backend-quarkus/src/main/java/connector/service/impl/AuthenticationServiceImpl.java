package connector.service.impl;

import connector.rest.model.GoogleUser;
import connector.service.api.AuthenticationService;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public Response login(GoogleUser user) {
        return null;
    }
}
