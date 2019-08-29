package connector.rest.impl;

import connector.rest.api.AuthenticationApi;
import connector.rest.model.GoogleUser;
import connector.service.api.AuthenticationService;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class AuthenticationImpl implements AuthenticationApi {

    @Inject
    AuthenticationService authenticationService;

    @Override
    public Response login(GoogleUser user, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        return authenticationService.login(user, request);
    }

    @Override
    public Response logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return authenticationService.logout(request);
    }
}
