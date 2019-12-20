package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.security.Role;
import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.zep.service.api.AuthenticationService;
import de.provantis.zep.*;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

@RequestScoped
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    Logger logger;

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    RequestHeaderType requestHeaderType;

    @Inject
    SessionUser sessionUser;

    //TODO: do we really need 2 different return types
    @Override
    public Response login(GoogleUser user) {
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Google user missing").build();
        }
        //TODO: is google user really the logged in user, if user is logged in
        if (sessionUser.isLoggedIn()) {
            return Response.ok(user).build();
        }

        // get ZEP employee
        final ReadMitarbeiterRequestType empl = new ReadMitarbeiterRequestType();
        empl.setRequestHeader(requestHeaderType);

        final ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(empl);
        //TODO: contact ZEP for enabling search-criteria for mail-address
        final MitarbeiterType mt = rmrt.getMitarbeiterListe().getMitarbeiter().stream()
                .filter(emp -> user.getEmail().equals(emp.getEmail()))
                .findFirst().orElse(null);

        // invalidate session when theres no appropriate employee
        if (mt == null) {
            throw new SecurityException(String.format("employee with id %s not found in ZEP", user.getEmail()));
        }

        sessionUser.setAuthorizationCode(user.getAuthorizationCode());
        sessionUser.setEmail(user.getEmail());
        sessionUser.setAuthToken(user.getAuthToken());
        sessionUser.setIdToken(user.getIdToken());
        sessionUser.setName(user.getName());
        sessionUser.setRole(Role.fromInt(mt.getRechte()).orElse(null));

        logger.info("Authentication of user with name {} successful", user.getName());
        return Response.ok(mt).build();
    }

    @Override
    public Response logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return Response.ok().build();
    }
}
