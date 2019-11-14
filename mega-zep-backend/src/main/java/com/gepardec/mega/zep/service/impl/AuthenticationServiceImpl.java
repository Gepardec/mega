package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.zep.service.api.AuthenticationService;
import de.provantis.zep.*;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    Logger LOG;

    @Inject
    @Named("ZepAuthorizationSOAPPortType")
    ZepSoapPortType zepSoapPortType;

    @Inject
    @Named("ZepAuthorizationRequestHeaderType")
    RequestHeaderType requestHeaderType;

    @Inject
    SessionUser sessionUser;

    @Override
    public Response login (GoogleUser user, HttpServletRequest request) {
        if (isUserLoggedIn()) {
            return Response.ok(user).build();
        }

        // get ZEP employee
        final ReadMitarbeiterRequestType empl = new ReadMitarbeiterRequestType();
        empl.setRequestHeader(requestHeaderType);

        if(zepSoapPortType != null) {
            final ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(empl);
            final MitarbeiterType mt = rmrt.getMitarbeiterListe().getMitarbeiter().stream().filter(emp -> user.getEmail().equals(emp.getEmail())).findFirst().orElse(null);

            // invalidate session when theres no appropriate employee
            if (mt == null) {
                invalidateSession();
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            try {
                LOG.info("Authentication of user with name " + user.getName());
                sessionUser.setAuthorizationCode(user.getAuthorizationCode());
                sessionUser.setEmail(user.getEmail());
                sessionUser.setAuthToken(user.getAuthToken());
                sessionUser.setIdToken(user.getIdToken());
                sessionUser.setName(user.getName());
                sessionUser.setRole(mt.getRechte());
            }
            catch (Exception e) {
                LOG.info("Authentication of user with name " + user.getName() + " failed: " + e);
                invalidateSession();
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            LOG.info("Authentication of user with name " + user.getName() + " successful");
            return Response.ok(mt).build();

            //logger.info("Authentication of user with name " + user.getName() + " successful");
            //return Response.ok(user).build();
        } else {
            LOG.error("Zep connection not possible.");
        }

        // Return status 500
        return Response.serverError().build();
    }

    @Override
    public Response logout(HttpServletRequest request) {
        invalidateSession();
        return Response.ok().build();
    }

    private boolean isUserLoggedIn() {
        return sessionUser.getId() != null;
    }

    private void invalidateSession(){
        sessionUser.invalidate();
    }
}