package connector.rest.impl;

import connector.rest.api.AuthenticationApi;
import connector.rest.model.GoogleUser;
import connector.service.api.AuthenticationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class AuthenticationImpl implements AuthenticationApi {

    @Inject
    AuthenticationService authenticationService;

    @Override
    public Response login(GoogleUser user) {
        return authenticationService.login(user);
    }
}
